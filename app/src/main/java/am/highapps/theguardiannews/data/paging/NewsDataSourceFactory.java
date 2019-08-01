package am.highapps.theguardiannews.data.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;

public class NewsDataSourceFactory extends DataSource.Factory<Integer, NewsResponseEntity> {

    private MutableLiveData<NewsDataSource> newsDataSourceMutableLiveData;
    private NewsDataSource mNewsDataSource;
    private TheGuardianNewsAPI guardianNewsAPI;

    public NewsDataSourceFactory(TheGuardianNewsAPI guardianNewsAPI) {
        this.guardianNewsAPI = guardianNewsAPI;
        newsDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, NewsResponseEntity> create() {
        mNewsDataSource = new NewsDataSource(guardianNewsAPI);

        newsDataSourceMutableLiveData = new MutableLiveData<>();
        newsDataSourceMutableLiveData.postValue(mNewsDataSource);

        return mNewsDataSource;
    }

    public NewsDataSource getNewsDataSource() {
        return mNewsDataSource;
    }
}
