package com.application.weathersnap.retrofit;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class ServiceGenerator {

    private final Retrofit retrofit;
    private static Retrofit retrofitO;

    @Inject
    public ServiceGenerator(Retrofit retrofit) {
        this.retrofit = retrofit;
        retrofitO = retrofit;
    }


    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static Retrofit getRetrofit(){
        return retrofitO;
    }

}
