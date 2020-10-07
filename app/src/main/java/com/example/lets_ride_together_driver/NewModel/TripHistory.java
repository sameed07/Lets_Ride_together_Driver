package com.example.lets_ride_together_driver.NewModel;

public class TripHistory {


    String distance, time, address, customerId, customerName, avatarUri;

    public TripHistory() {
    }

    public TripHistory(String distance, String time, String address, String customerId, String customerName, String avatarUri) {
        this.distance = distance;
        this.time = time;
        this.address = address;
        this.customerId = customerId;
        this.customerName = customerName;
        this.avatarUri = avatarUri;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}