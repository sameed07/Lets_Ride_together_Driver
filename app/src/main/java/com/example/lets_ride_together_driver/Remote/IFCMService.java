package com.example.lets_ride_together_driver.Remote;

import com.example.lets_ride_together_driver.NewModel.DataMessage;
import com.example.lets_ride_together_driver.NewModel.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAGdp8O98:APA91bFlaf5XTz2qtTYnRI4xUyHIg1neb-Yx3-zpD-3frzVLQY2MIWb2YJR92Lw5xMRw0QuyKmnj8XURvbUSeLa-L21IcewY5uu94P9rkSzWmzjsuxTV7Kvg5ea5CKWEi8pL9C6OgE-W"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);
}
