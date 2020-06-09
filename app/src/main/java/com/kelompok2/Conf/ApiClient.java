package com.kelompok2.Conf;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://kolakmahal.000webhostapp.com/mobile-app/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){

        if ( retrofit == null ){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
