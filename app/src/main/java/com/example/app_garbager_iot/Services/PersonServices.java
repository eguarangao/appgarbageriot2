package com.example.app_garbager_iot.Services;
import retrofit2.Call;
import com.example.app_garbager_iot.Model.PersonModel;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PersonServices {
    @GET("person")
    Call<List<PersonModel>>getPerson();
    @POST("person")
    Call<PersonModel>addPerson(@Body PersonModel personModel);
    @GET("person/email/{email}")
    Call<List<PersonModel>>findByEmail(@Path("email") String email);

}

