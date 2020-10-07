package com.example.lets_ride_together_driver.NewModel;

import com.google.gson.annotations.SerializedName;

public class User {
    private String uId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city, rates;
    private String profile_img, status;
    @SerializedName("car_type")
    private String carType;


    public User() {
    }

    public User(String uId, String name, String email, String phone, String password, String city, String rates, String carType, String profile_img, String status) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.city = city;
        this.rates = rates;
        this.carType = carType;
        this.profile_img = profile_img;
        this.status = status;
    }


    public User(String uId, String name, String email, String phone, String password, String city, String profile_img) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profile_img = profile_img;
        this.city = city;
    }


    public User(String uId, String name, String email, String phone, String password, String city, String rates, String carType, String profile_img) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.city = city;
        this.rates = rates;
        this.carType = carType;
        this.profile_img = profile_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
//    public String getProfile_img() {
//        return profile_img;
//    }
//
//    public void setProfile_img(String profile_img) {
//        this.profile_img = profile_img;
//    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
