package am.highapps.theguardiannews.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.preference.PreferenceManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import am.highapps.theguardiannews.App;
import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.db.NewsDatabase;
import am.highapps.theguardiannews.data.entity.NewsItemResponse;
import am.highapps.theguardiannews.data.entity.NewsResponse;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.util.AppExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static am.highapps.theguardiannews.util.Constant.PREF_NEWEST_ARTICLE_PUBLICATION_DATE;
import static am.highapps.theguardiannews.util.DateTimeHelper.parsToStringDate;

@Singleton
public class NewsRepository {

    private static final String TAG = NewsRepository.class.getSimpleName();

    private final TheGuardianNewsAPI guardianNewsAPI;
    private final NewsDatabase newsDatabase;
    private final AppExecutors appExecutors;

    @Inject
    public NewsRepository(TheGuardianNewsAPI guardianNewsAPI,
                          NewsDatabase newsDatabase,
                          AppExecutors appExecutors) {
        this.guardianNewsAPI = guardianNewsAPI;
        this.newsDatabase = newsDatabase;
        this.appExecutors = appExecutors;
    }


    public LiveData<NewsResponseEntity> getNewsItem(String id) {
        final MutableLiveData<NewsResponseEntity> mutableLiveData = new MutableLiveData<>();

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                NewsResponseEntity value = newsDatabase.newsDao().getNewsItem(id);
                if (value != null) {
                    mutableLiveData.postValue(value);
                } else {
                    guardianNewsAPI.getNewsItem(id).enqueue(new Callback<NewsItemResponse>() {
                        @Override
                        public void onResponse(Call<NewsItemResponse> call, Response<NewsItemResponse> response) {
                            if (response.isSuccessful()) {
                                NewsItemResponse itemResponse = response.body();
                                mutableLiveData.postValue(itemResponse.getResponse().getContent());
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsItemResponse> call, Throwable t) {
                            mutableLiveData.postValue(null);
                            Log.e(TAG, "Failed getting NewsItemResponse: " + t.getMessage());
                        }
                    });
                }
            }
        });

        return mutableLiveData;
    }

    public DataSource.Factory<Integer, NewsResponseEntity> getOfflineNewsDS() {
        return newsDatabase.newsDao().getOfflineNewsDF();
    }

    public LiveData<List<NewsResponseEntity>> getPinedNews() {
        return newsDatabase.newsDao().getAllPined();
    }

    public LiveData getOfflineNews() {
        return newsDatabase.newsDao().getOfflineNews();
    }

    public LiveData<Boolean> checkNewItem(String data) {
        final MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();

        if (data == null) {
            mutableLiveData.setValue(false);
            return mutableLiveData;
        }
        guardianNewsAPI.getNewsItemsFromDate(parsToStringDate(data), 1, 1).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse itemResponse = response.body();
                    if (itemResponse != null) {
                        mutableLiveData.setValue(!itemResponse.getResponse().getNewsResponseEntities().get(0).getWebPublicationDate()
                                .equals(PreferenceManager.getDefaultSharedPreferences(App.getInstance()).getString(PREF_NEWEST_ARTICLE_PUBLICATION_DATE, null)));
                    } else {
                        mutableLiveData.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                mutableLiveData.setValue(false);
                Log.e(TAG, "Failed getting NewsItemResponse: " + t.getMessage());

            }
        });
        return mutableLiveData;
    }

    public void setSaveState(NewsResponseEntity entity) {
        appExecutors.diskIO().execute(() -> newsDatabase.newsDao().insertNewsItem(entity));
    }

    public void setPinState(NewsResponseEntity entity) {
        appExecutors.diskIO().execute(() -> newsDatabase.newsDao().insertNewsItem(entity));
    }

}
