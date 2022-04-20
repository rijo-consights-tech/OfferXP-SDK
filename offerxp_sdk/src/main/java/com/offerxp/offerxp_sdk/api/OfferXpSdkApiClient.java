package com.offerxp.offerxp_sdk.api;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

//import com.chuckerteam.chucker.api.ChuckerInterceptor;
//import com.example.offerxp.BuildConfig;
import com.example.offerxp.OfferXP;
import com.example.offerxp.TokenExpiryListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfferXpSdkApiClient {
    private static OfferXpSdkApiClient instance;
    private final OfferXpSdkApiService offerXpApis;

    private OfferXpSdkApiClient(Context context) {
        synchronized (Retrofit.class) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS);

//            builder.addInterceptor(new ChuckInterceptor(context));
//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(loggingInterceptor);
//            }
            //builder.authenticator(new JwtAuthenticator());
            //builder.addInterceptor(new RefreshTokenExpiryHandlingInterceptor());
            builder.addInterceptor(new AuthenticationInterceptor());
            builder.addInterceptor(new RequestModifier());
            builder.addInterceptor(new RefreshTokenExpiryHandlingInterceptor());
            //builder.addInterceptor(new ChuckerInterceptor(context));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(OfferXpSdkApiConstants.getBaseUrl())
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

            offerXpApis = retrofit.create(OfferXpSdkApiService.class);
        }
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new OfferXpSdkApiClient(context);
        }
    }

    public static OfferXpSdkApiClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ApiClient not initialized, use initialize()");
        }
        return instance;
    }

    public OfferXpSdkApiService getOfferXpSdkApis() {
        return instance.offerXpApis;
    }

    public static class AuthenticationInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();

            String authToken = OfferXP.INSTANCE.getJwt();

            List<String> customAnnotations = original.headers().values(OfferXpSdkApiConstants.CUSTOM_HEADER_KEY_AT);
            List<String> customTokens = original.headers().values(OfferXpSdkApiConstants.CUSTOM_TOKEN_KEY);

            Request.Builder builder = original.newBuilder().removeHeader("@");
            builder.removeHeader(OfferXpSdkApiConstants.CUSTOM_TOKEN_KEY);
            if (customTokens.size() > 0) {
                String cToken = customTokens.get(0);
                builder.header("Authorization", "Token " + cToken);
            } else if (!customAnnotations.contains(OfferXpSdkApiConstants.CUSTOM_HEADER_VALUE_NO_AUTH) && !TextUtils.isEmpty(authToken)) {
                builder.header("Authorization", "Token " + authToken);
                //Logger.i("AuthInterceptor", "Token: " + authToken);
            }
            builder.header("Content-Type", "application/json");

            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    public static class RefreshTokenExpiryHandlingInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (response.code() == OfferXpSdkApiConstants.STATUS_UNAUTHORIZED) {
                TokenExpiryListener tokenExpiryListener = OfferXP.INSTANCE.getTokenExpiryListener();
                if(tokenExpiryListener!=null){
                    tokenExpiryListener.onTokenExpiry();
                }
            }

            return response;
        }
    }

    public static class RequestModifier implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final Request request = chain.request();
            final Request.Builder newRequestBuilder = request.newBuilder();
            if (request.header("REQUEST-ID") == null) {
                newRequestBuilder.addHeader("REQUEST-ID", UUID.randomUUID().toString());
            }
            if (request.header("Partner-Store") == null) {
                newRequestBuilder.addHeader("Partner-Store", Objects.requireNonNull(OfferXP.INSTANCE.getPartnerStore()));
            }
            if (request.header("Partner-API-key") == null) {
                newRequestBuilder.addHeader("Partner-API-key", Objects.requireNonNull(OfferXP.INSTANCE.getApiKey()));

            }
            /*
               Add Additional modifications/customizations here...
             */
            return chain.proceed(newRequestBuilder.build());
        }
    }
}
