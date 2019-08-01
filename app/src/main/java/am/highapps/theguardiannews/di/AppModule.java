package am.highapps.theguardiannews.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import javax.inject.Singleton;

import am.highapps.theguardiannews.App;
import am.highapps.theguardiannews.data.db.DbFactory;
import am.highapps.theguardiannews.data.db.NewsDatabase;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

}
