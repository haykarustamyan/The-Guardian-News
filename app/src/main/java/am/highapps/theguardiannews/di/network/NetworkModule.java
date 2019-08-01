package am.highapps.theguardiannews.di.network;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import am.highapps.theguardiannews.data.api.ApiFactory;
import am.highapps.theguardiannews.util.NetworkUtil;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    NetworkUtil provideNetworkUtil(Context context) {
        return new NetworkUtil(context);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@Named("logging") HttpLoggingInterceptor loggingInterceptor,
                                     SharedPreferences preferences) {

        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Content-Type", "application/json;charset=utf-8");
                    builder.addHeader("api-key", ApiFactory.API_KEY);

                    return chain.proceed(builder.build());
                })
                .build();
        return okhttpClient;
    }

    @Provides
    @Named("logging")
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

}