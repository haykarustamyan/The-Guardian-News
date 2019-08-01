package am.highapps.theguardiannews.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import am.highapps.theguardiannews.ui.newsitem.NewsItemViewModel;
import am.highapps.theguardiannews.ui.newslist.NewsListViewModel;
import am.highapps.theguardiannews.util.MainViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel.class)
    abstract ViewModel bindNewsListViewModel(NewsListViewModel newsListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsItemViewModel.class)
    abstract ViewModel bindNewsItemViewModel(NewsItemViewModel newsListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(MainViewModelFactory factory);

}
