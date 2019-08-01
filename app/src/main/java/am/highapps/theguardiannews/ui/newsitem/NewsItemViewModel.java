package am.highapps.theguardiannews.ui.newsitem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.data.repository.NewsRepository;

public class NewsItemViewModel extends ViewModel {

    private final NewsRepository newsRepository;
    private final TheGuardianNewsAPI guardianNewsAPI;

    @Inject
    public NewsItemViewModel(NewsRepository newsRepository, TheGuardianNewsAPI guardianNewsAPI) {
        this.newsRepository = newsRepository;
        this.guardianNewsAPI = guardianNewsAPI;
    }

    public LiveData<NewsResponseEntity> getNewsItem(String id) {
        return newsRepository.getNewsItem(id);
    }

    public void setNewsItemSaveState(NewsResponseEntity entity) {
        newsRepository.setSaveState(entity);
    }

    public void setPinState(NewsResponseEntity newsResponseEntity) {
        newsRepository.setPinState(newsResponseEntity);
    }
}