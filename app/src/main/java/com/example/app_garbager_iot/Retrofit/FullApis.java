package com.example.app_garbager_iot.Retrofit;

import com.example.app_garbager_iot.Services.ContainerServices;
import com.example.app_garbager_iot.Services.PedictServices;
import com.example.app_garbager_iot.Services.PersonServices;

public class FullApis {
    public static  final String url_heroku = "https://garbager-iot.herokuapp.com/";
    public static final  String url_postgres = "https://recycle-trash.herokuapp.com/";
    public static PersonServices getPersonServices(){
        return ApiClient.getRetrofitClient(url_heroku).create(PersonServices.class);
    }
    public static ContainerServices getContainerServices(){
        return ApiClient.getRetrofitClient(url_heroku).create(ContainerServices.class);
    }
    public  static PedictServices gePedictServices(){
        return ApiClient.getRetrofitClient(url_postgres).create(PedictServices.class);
    }

}
