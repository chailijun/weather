package com.chailijun.mweather.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * 已经选择的城市
 */

@Entity
public class SelectCity implements Serializable{

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "updateTime")
    private long updateTime;        //更新时间

    @Property(nameInDb = "isShow")
    private boolean isShow;         //该城市是否作为默认显示城市

    @Property(nameInDb = "isLocation")
    private boolean isLocation;     //该城市是否是定位城市

    @Property(nameInDb = "cityId")
    private String cityId;
    @Property(nameInDb = "cityEn")
    private String cityEn;
    @Property(nameInDb = "cityZh")
    private String cityZh;

    @Property(nameInDb = "provinceEn")
    private String provinceEn;  //省英文（国内城市为所属省，国外城市为所属国家）
    @Property(nameInDb = "provinceZh")
    private String provinceZh;  //省中文


    @Property(nameInDb = "weatherSet")
    private String weatherSet;      //天气内容json字符串


    public String getWeatherSet() {
        return this.weatherSet;
    }


    public void setWeatherSet(String weatherSet) {
        this.weatherSet = weatherSet;
    }


    public String getProvinceZh() {
        return this.provinceZh;
    }


    public void setProvinceZh(String provinceZh) {
        this.provinceZh = provinceZh;
    }


    public String getProvinceEn() {
        return this.provinceEn;
    }


    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }


    public String getCityZh() {
        return this.cityZh;
    }


    public void setCityZh(String cityZh) {
        this.cityZh = cityZh;
    }


    public String getCityEn() {
        return this.cityEn;
    }


    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }


    public String getCityId() {
        return this.cityId;
    }


    public void setCityId(String cityId) {
        this.cityId = cityId;
    }


    public boolean getIsLocation() {
        return this.isLocation;
    }


    public void setIsLocation(boolean isLocation) {
        this.isLocation = isLocation;
    }


    public boolean getIsShow() {
        return this.isShow;
    }


    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }


    public long getUpdateTime() {
        return this.updateTime;
    }


    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Generated(hash = 1687365066)
    public SelectCity(Long id, long updateTime, boolean isShow, boolean isLocation,
            String cityId, String cityEn, String cityZh, String provinceEn,
            String provinceZh, String weatherSet) {
        this.id = id;
        this.updateTime = updateTime;
        this.isShow = isShow;
        this.isLocation = isLocation;
        this.cityId = cityId;
        this.cityEn = cityEn;
        this.cityZh = cityZh;
        this.provinceEn = provinceEn;
        this.provinceZh = provinceZh;
        this.weatherSet = weatherSet;
    }


    @Generated(hash = 58876853)
    public SelectCity() {
    }

}
