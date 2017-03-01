package com.chailijun.mweather.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 国际城市
 */
@Entity
public class WorldCity implements CityBase {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("id")
    @Property(nameInDb = "cityId")
    private String cityId;      //城市/地区代码

    @Property(nameInDb = "cityEn")
    private String cityEn;      //城市名英文
    @Property(nameInDb = "cityZh")
    private String cityZh;      //城市名中文

    @Property(nameInDb = "continent")
    private String continent;   //洲

    @Property(nameInDb = "countryCode")
    private String countryCode; //国家代码
    @Property(nameInDb = "countryEn")
    private String countryEn;   //国家名称英文
    @Property(nameInDb = "countryZh")
    private String countryZh;   //国家名称中文

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

    public String getContinent() {
        return this.continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
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

    public String getCountryZh() {
        return this.countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    @Generated(hash = 577451021)
    public WorldCity(Long id, String cityId, String cityEn, String cityZh,
                     String continent, String countryCode, String countryEn,
                     String countryZh, String lat, String lon) {
        this.id = id;
        this.cityId = cityId;
        this.cityEn = cityEn;
        this.cityZh = cityZh;
        this.continent = continent;
        this.countryCode = countryCode;
        this.countryEn = countryEn;
        this.countryZh = countryZh;
        this.lat = lat;
        this.lon = lon;
    }

    @Generated(hash = 1627752894)
    public WorldCity() {
    }
}
