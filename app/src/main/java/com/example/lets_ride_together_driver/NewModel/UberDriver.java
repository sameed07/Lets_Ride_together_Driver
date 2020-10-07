package com.example.lets_ride_together_driver.NewModel;

public class UberDriver {
    private String name, phone, avatarUri, rates, carType;


    public UberDriver() {

    }

    public UberDriver(String name, String phone, String avatarUri, String rates, String carType) {
        this.name = name;
        this.phone = phone;
        this.avatarUri = avatarUri;
        this.rates = rates;
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
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
}
