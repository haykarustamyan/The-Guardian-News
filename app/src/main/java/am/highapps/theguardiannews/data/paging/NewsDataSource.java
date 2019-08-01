package am.highapps.theguardiannews.data.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import androidx.preference.PreferenceManager;

import java.util.Collections;
import java.util.List;

import am.highapps.theguardiannews.App;
import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.entity.NewsResponse;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


 import static am.highapps.theguardiannews.util.Constant.NEXT_PAGE_KEY_TWO;
import static am.highapps.theguardiannews.util.Constant.PAGE_ONE;
import static am.highapps.theguardiannews.util.Constant.PREF_NEWEST_ARTICLE_PUBLICATION_DATE;
import static am.highapps.theguardiannews.util.Constant.PREVIOUS_PAGE_KEY_ONE;
import static am.highapps.theguardiannews.util.Constant.RESPONSE_CODE_API_STATUS;
import static am.highapps.theguardiannews.util.DateTimeHelper.parseToDate;

public class NewsDataSource extends PageKeyedDataSource<Integer, NewsResponseEntity> {

    private static final String TAG = NewsDataSource.class.getSimpleName();

    private TheGuardianNewsAPI guardianNewsAPI;


    public NewsDataSource(TheGuardianNewsAPI guardianNewsAPI) {
        this.guardianNewsAPI = guardianNewsAPI;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, NewsResponseEntity> callback) {
        guardianNewsAPI.getNewsList(PAGE_ONE, params.requestedLoadSize)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            List<NewsResponseEntity> newsResponseEntities = response.body().getResponse().getNewsResponseEntities();

                            saveNewsItemLastDate(newsResponseEntities);
                            callback.onResult(response.body().getResponse().getNewsResponseEntities(), PREVIOUS_PAGE_KEY_ONE, NEXT_PAGE_KEY_TWO);

                        } else if (response.code() == RESPONSE_CODE_API_STATUS) {
                            Log.e(TAG, "Invalid Api key. Response code: " + response.code());
                        } else {
                            Log.e(TAG, "Response Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        Log.e(TAG, "Failed initializing a PageList: " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, NewsResponseEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, NewsResponseEntity> callback) {

        final int currentPage = params.key;

        guardianNewsAPI.getNewsList(currentPage, params.requestedLoadSize)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            int nextKey = currentPage + 1;
                            callback.onResult(response.body().getResponse().getNewsResponseEntities(), nextKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        Log.e(TAG, "Failed appending page: " + t.getMessage());
                    }
                });
    }

    private void saveNewsItemLastDate(List<NewsResponseEntity> newsResponseEntities) {
        Collections.sort(newsResponseEntities, (o1, o2) -> parseToDate(o2.getWebPublicationDate()).compareTo(parseToDate(o1.getWebPublicationDate())));

        PreferenceManager.getDefaultSharedPreferences(App.getInstance())
                .edit()
                .putString(PREF_NEWEST_ARTICLE_PUBLICATION_DATE,
                        newsResponseEntities.get(0).getWebPublicationDate())
                .apply();
    }
}
