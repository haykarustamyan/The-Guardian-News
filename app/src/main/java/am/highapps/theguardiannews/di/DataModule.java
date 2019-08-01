package am.highapps.theguardiannews.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

import javax.inject.Singleton;

import am.highapps.theguardiannews.data.db.DbFactory;
import am.highapps.theguardiannews.data.db.NewsDatabase;
import dagger.Module;
import dagger.Provides;

import static am.highapps.theguardiannews.data.db.DbFactory.DB_NAME;

@Module
public class DataModule {

    @Provides
    @Singleton
    NewsDatabase provideNewsDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                NewsDatabase.class,
                DB_NAME).build();
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
