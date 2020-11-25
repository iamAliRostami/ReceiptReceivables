package com.leon.receipt_receivables.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkHelper {
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final long READ_TIMEOUT = 120;
    private static final long WRITE_TIMEOUT = 60;
    private static final long CONNECT_TIMEOUT = 10;
    private static final boolean RETRY_ENABLED = true;

    private NetworkHelper() {
    }

    public static OkHttpClient getHttpClient(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY_ENABLED)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor).build();
    }

    public static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY_ENABLED)
                .addInterceptor(interceptor).build();
    }

    public static Retrofit getInstance(String token, boolean b) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String baseUrl = DifferentCompanyManager.getBaseUrl(
                DifferentCompanyManager.getActiveCompanyName());
        if (b)
            baseUrl = DifferentCompanyManager.getLocalBaseUrl(
                    DifferentCompanyManager.getActiveCompanyName());
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(NetworkHelper.getHttpClient(token))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getInstance(boolean b) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String baseUrl = DifferentCompanyManager.getBaseUrl(
                DifferentCompanyManager.getActiveCompanyName());
        if (b)
            baseUrl = DifferentCompanyManager.getLocalBaseUrl(
                    DifferentCompanyManager.getActiveCompanyName());
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(NetworkHelper.getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

