package com.chailijun.mweather.data;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 国内城市
 */
@Entity
public class City implements CityBase{

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("id")
    @Property(nameInDb = "cityId")
    private String cityId;      //城市id
    @Property(nameInDb = "cityEn")
    private String cityEn;      //城市英文
    @Property(nameInDb = "cityZh")
    private String cityZh;      //城市中文

    @Property(nameInDb = "countryCode")
    private String countryCode; //国家代码
    @Property(nameInDb = "countryEn")
    private String countryEn;   //国家英文
    @Property(nameInDb = "countryZh")
    private String countryZh;   //国家中文

    @Property(nameInDb = "provinceEn")
    private String provinceEn;  //省英文
    @Property(nameInDb = "provinceZh")
    private String provinceZh;  //省中文

    @Property(nameInDb = "leaderEn")
    private String leaderEn;    //所属上级市英文
    @Property(nameInDb = "leaderZh")
    private String leaderZh;    //所属上级市中文

    @Property(nameInDb = "lat")
    private String lat;         //纬度
    @Property(nameInDb = "lon")
    private String lon;         //经度

    public String getLon() {
        return this.lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLeaderZh() {
        return this.leaderZh;
    }

    public void setLeaderZh(String leaderZh) {
        this.leaderZh = leaderZh;
    }

    public String getLeaderEn() {
        return this.leaderEn;
    }

    public void setLeaderEn(String leaderEn) {
        this.leaderEn = leaderEn;
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

    public String getCountryZh() {
        return this.countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    public String getCountryEn() {
        return this.countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 352833066)
    public City(Long id, String cityId, String cityEn, String cityZh,
                String countryCode, String countryEn, String countryZh,
                String provinceEn, String provinceZh, String leaderEn, String leaderZh,
                String lat, String lon) {
        this.id = id;
        this.cityId = cityId;
        this.cityEn = cityEn;
        this.cityZh = cityZh;
        this.countryCode = countryCode;
        this.countryEn = countryEn;
        this.countryZh = countryZh;
        this.provinceEn = provinceEn;
        this.provinceZh = provinceZh;
        this.leaderEn = leaderEn;
        this.leaderZh = leaderZh;
        this.lat = lat;
        this.lon = lon;
    }

    @Generated(hash = 750791287)
    public City() {
    }
}
