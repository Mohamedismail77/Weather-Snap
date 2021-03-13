package com.application.weathersnap.retrofit;


import com.application.weathersnap.utils.ApiRoutes;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(ApplicationComponent.class)
@Module
public class RetrofitModule {


    @Provides
    @Singleton
    public ServiceGenerator providesServiceGenerator(Retrofit retrofit) {
        return new ServiceGenerator(retrofit);
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofitClient(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiRoutes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient).build();
    }


    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(){
        return new OkHttpClient.Builder().build();
    }

}

