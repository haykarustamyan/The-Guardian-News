package am.highapps.theguardiannews.di.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import javax.inject.Singleton;

import am.highapps.theguardiannews.data.api.ApiFactory;
import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public TheGuardianNewsAPI ProvidesService(Retrofit mvpRetrofit) {
        return mvpRetrofit.create(TheGuardianNewsAPI.class);
    }

    @Provides
    @Singleton
    public Gson ProvidesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ApiFactory.Url.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

}
