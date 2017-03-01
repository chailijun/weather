package com.chailijun.mweather.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * 国内景点
 */
@Entity
public class Scenic {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("id")
    @Property(nameInDb = "scenicId")
    private String scenicId;//景点ID
    @Property(nameInDb = "name")
    private String name;    //景点名称
    @Property(nameInDb = "city")
    private String city;    //景点所属城市
    @Property(nameInDb = "province")
    private String province;//景点所属省

    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScenicId() {
        return this.scenicId;
    }
    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1138767274)
    public Scenic(Long id, String scenicId, String name, String city,
            String province) {
        this.id = id;
        this.scenicId = scenicId;
        this.name = name;
        this.city = city;
        this.province = province;
    }
    @Generated(hash = 1765431978)
    public Scenic() {
    }
}
