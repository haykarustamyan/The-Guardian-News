package am.highapps.theguardiannews.ui.newslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.data.paging.NewsDataSourceFactory;
import am.highapps.theguardiannews.data.repository.NewsRepository;

import static am.highapps.theguardiannews.util.Constant.INITIAL_LOAD_SIZE_HINT;
import static am.highapps.theguardiannews.util.Constant.NUMBER_OF_FIXED_THREADS_FIVE;
import static am.highapps.theguardiannews.util.Constant.PAGE_SIZE;
import static am.highapps.theguardiannews.util.Constant.PREFETCH_DISTANCE;

public class NewsListViewModel extends ViewModel {

    private final NewsRepository newsRepository;
    private final TheGuardianNewsAPI guardianNewsAPI;

    private LiveData<PagedList<NewsResponseEntity>> pagedListLiveData;
    private LiveData<PagedList<NewsResponseEntity>> offlinePagedListLiveData;
    private NewsDataSourceFactory newsDataSourceFactory;
    private PagedList.Config config;


    @Inject
    public NewsListViewModel(NewsRepository newsRepository, TheGuardianNewsAPI guardianNewsAPI) {
        this.newsRepository = newsRepository;
        this.guardianNewsAPI = guardianNewsAPI;

        newsDataSourceFactory = new NewsDataSourceFactory(guardianNewsAPI);
        config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();
    }

    private void initPagedListLiveData() {
        pagedListLiveData = new LivePagedListBuilder<>(newsDataSourceFactory, config)
                .setFetchExecutor(Executors.newFixedThreadPool(NUMBER_OF_FIXED_THREADS_FIVE))
                .build();
    }

    private void initOfflinePagedListLiveData() {
        offlinePagedListLiveData = new LivePagedListBuilder<>(newsRepository.getOfflineNewsDS(), config)
                .setFetchExecutor(Executors.newFixedThreadPool(NUMBER_OF_FIXED_THREADS_FIVE))
                .build();
    }

    public LiveData<PagedList<NewsResponseEntity>> getNewsPagedList() {
        if (pagedListLiveData == null) {
            initPagedListLiveData();
        }
        return pagedListLiveData;
    }


    public LiveData<PagedList<NewsResponseEntity>> getOfflineNewsPagedList() {
        if (offlinePagedListLiveData == null) {
            initOfflinePagedListLiveData();
        }
        return offlinePagedListLiveData;
    }


    public void invalidateDataSource() {
        if (newsDataSourceFactory.getNewsDataSource() != null) {
            newsDataSourceFactory.getNewsDataSource().invalidate();
        }
    }

    public LiveData<Boolean> checkNewItem(String date) {
        return newsRepository.checkNewItem(date);
    }


    public LiveData<List<NewsResponseEntity>> getPinedNews() {
        return newsRepository.getPinedNews();
    }

}
