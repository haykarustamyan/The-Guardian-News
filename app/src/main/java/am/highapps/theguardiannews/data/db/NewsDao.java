package am.highapps.theguardiannews.data.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import am.highapps.theguardiannews.data.entity.NewsResponseEntity;

import static am.highapps.theguardiannews.data.db.DbFactory.NEWS_TABLE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsDao {

    @Insert(onConflict = REPLACE)
    void insertOrUpdateNews(List<NewsResponseEntity> newsEntries);

    @Query("SELECT * from " + NEWS_TABLE + " WHERE isSaved = 1 ORDER BY webPublicationDate DESC")
    DataSource.Factory<Integer, NewsResponseEntity> getOfflineNewsDF();

    @Query("SELECT * from " + NEWS_TABLE + " WHERE isSaved = 1 ORDER BY webPublicationDate DESC")
    LiveData<List<NewsResponseEntity>> getOfflineNews();

    @Query("SELECT * from " + NEWS_TABLE + " WHERE isPined = 1 ORDER BY webPublicationDate DESC")
    LiveData<List<NewsResponseEntity>> getAllPined();

    @Query("SELECT * FROM " + NEWS_TABLE)
    LiveData<List<NewsResponseEntity>> getNews();

    @Insert(onConflict = REPLACE)
    void insertNewsItem(NewsResponseEntity newsEntry);

    @Delete
    void deleteNewsItem(NewsResponseEntity newsEntry);

    @Query("SELECT * FROM " + NEWS_TABLE + "  WHERE id = :id")
    LiveData<NewsResponseEntity> getNewsItemLiveData(String id);

    @Query("SELECT * FROM " + NEWS_TABLE + "  WHERE id = :id")
    NewsResponseEntity getNewsItem(String id);

}
