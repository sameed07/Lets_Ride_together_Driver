package com.example.lets_ride_together_driver.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {

    @GET
    Call<String> getPath(@Url String url);
}
