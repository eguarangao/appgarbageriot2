package com.example.app_garbager_iot.Services;

import com.example.app_garbager_iot.Model.Container;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContainerServices {
    @GET("container/{id}")
    Call<List<Container>> getByFindContainer(@Path("id")String id);
    @GET("container")
    Call<List<Container>> getContainer();
    @POST("container")
    Call<String>addContainer(@Body Container container);

}
