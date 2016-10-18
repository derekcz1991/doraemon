package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by derek on 09/10/2016.
 */
public class NearbyItem extends BaseModel {

    private long id;
    private int kind;
    private long uid;
    @SerializedName("long")
    private double longitude;
    @SerializedName("lat")
    private double latitude;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    @SerializedName("geohash_code")
    private String geohashCode;
    private int adcode;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("username")
    private String userName;
    @SerializedName("avatar")
    private String avatarUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeohashCode() {
        return geohashCode;
    }

    public void setGeohashCode(String geohashCode) {
        this.geohashCode = geohashCode;
    }

    public int getAdcode() {
        return adcode;
    }

    public void setAdcode(int adcode) {
        this.adcode = adcode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
