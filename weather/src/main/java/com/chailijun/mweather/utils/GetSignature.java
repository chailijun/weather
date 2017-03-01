package com.chailijun.mweather.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

/**
 * 获取签名
 */
public class GetSignature {

    public static void CheckSignature(Context context) {
        byte[] signature = getSign(context);
        String publickey = getPublicKey(signature);
        Logger.d("CheckSignature", "publickey = " + publickey);
    }

    private static byte[] getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名读取签名
            if (packageName.equals("com.chailijun.mweather")) { //根据你自己的包名替换
                return info.signatures[0].toByteArray();
            }
        }
        return null;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static byte[] getPackageArchiveInfo(String apkFile, int flags){

        //这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        Object pkgParserPkg = null;
        Class[] typeArgs = null;
        Object[] valueArgs = null;
        try {
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Constructor<?> packageParserConstructor = null;
            Object packageParser = null;
            //由于SDK版本问题，这里需做适配，来生成不同的构造函数
            if (Build.VERSION.SDK_INT > 20) {
                //无参数 constructor
                packageParserConstructor = packageParserClass.getDeclaredConstructor();
                packageParser = packageParserConstructor.newInstance();
                packageParserConstructor.setAccessible(true);//允许访问

                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = int.class;
                Method pkgParser_parsePackageMtd = packageParserClass.getDeclaredMethod("parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[2];
                valueArgs[0] = new File(apkFile);
                valueArgs[1] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(packageParser, valueArgs);
            } else {
                //低版本有参数 constructor
                packageParserConstructor = packageParserClass.getDeclaredConstructor(String.class);
                Object[] fileArgs = { apkFile };
                packageParser = packageParserConstructor.newInstance(fileArgs);
                packageParserConstructor.setAccessible(true);//允许访问

                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = int.class;

                Method pkgParser_parsePackageMtd = packageParserClass.getDeclaredMethod("parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[4];
                valueArgs[0] = new File(apkFile);
                valueArgs[1] = apkFile;
                valueArgs[2] = metrics;
                valueArgs[3] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(packageParser, valueArgs);
            }

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = int.class;
            Method pkgParser_collectCertificatesMtd = packageParserClass.getDeclaredMethod("collectCertificates", typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(packageParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            return info[0].toByteArray();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static String getPublicKey(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            return cert.getPublicKey().toString();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}