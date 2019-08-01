package am.highapps.theguardiannews.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import am.highapps.theguardiannews.data.entity.NewsResponseEntity;

import static am.highapps.theguardiannews.data.db.DbFactory.DB_VERSION;

@Database(entities = {NewsResponseEntity.class}, version = DB_VERSION,exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

}
