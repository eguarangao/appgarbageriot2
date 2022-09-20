package com.example.app_garbager_iot.Retrofit;

import com.example.app_garbager_iot.Services.PersonServices;

public class FullApis {
    public static  final String url_heroku = "https://garbager-iot.herokuapp.com/";

    public static PersonServices getPersonServices(){
        return ApiClient.getRetrofitClient(url_heroku).create(PersonServices.class);
    }

}
