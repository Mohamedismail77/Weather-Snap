package com.application.weathersnap.utils;


import com.pountech.andoird.ela.retrofit.ServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {



    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = ServiceGenerator.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }
        return error;
    }

}
