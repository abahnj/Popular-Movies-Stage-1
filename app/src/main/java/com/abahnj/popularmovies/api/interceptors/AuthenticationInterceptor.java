package com.abahnj.popularmovies.api.interceptors;

import android.support.annotation.NonNull;
import com.abahnj.popularmovies.BuildConfig;
import com.abahnj.popularmovies.utils.Constants;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();
        HttpUrl newHttpUrl = originalHttpUrl.newBuilder()
                .setQueryParameter(Constants.API_KEY_PARAM, BuildConfig.ApiKey)
                .build();
        Request newRequest = originalRequest.newBuilder()
                .url(newHttpUrl)
                .build();
        return chain.proceed(newRequest);
    }
}
