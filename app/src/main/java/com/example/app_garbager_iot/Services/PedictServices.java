package com.example.app_garbager_iot.Services;

import com.example.app_garbager_iot.Model.Capture;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PedictServices {
    @GET("controller/prediction.php")
    Call<List<Capture>> getPredict(@Query("predit") String id);
}
