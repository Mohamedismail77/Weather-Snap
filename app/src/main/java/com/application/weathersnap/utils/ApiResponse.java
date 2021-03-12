package com.application.weathersnap.utils;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.operators.flowable.FlowableJust;

public class ApiResponse<T,E> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;

    @NonNull
    public final E error;

    @Nullable public final String message;

    private ApiResponse(@NonNull Status status, @Nullable T data,
                        @Nullable E error,@Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.error = error;
    }


    public static <T,E> ApiResponse<T,E> success(@NonNull T data) {
        return new ApiResponse<>(Status.SUCCESS, data, null,null);
    }

    public static <T,E> ApiResponse<T,E> error(String msg, @Nullable E error) {
        return new ApiResponse<>(Status.ERROR, null,error, msg);
    }

    public static <T,E> ApiResponse<T,E> loading(@Nullable T data) {
        return new ApiResponse<>(Status.LOADING, data, null,null);
    }


    public static <T,E> ApiResponse<T,E> network() {
        return new ApiResponse<>(Status.NETWORK, null, null,null);
    }

    public LiveData<ApiResponse<T, E>> asLiveData() {
        return LiveDataReactiveStreams.fromPublisher(new FlowableJust<ApiResponse<T, E>>(this));
    }


    public enum Status { SUCCESS, ERROR, LOADING,NETWORK}
    
}
