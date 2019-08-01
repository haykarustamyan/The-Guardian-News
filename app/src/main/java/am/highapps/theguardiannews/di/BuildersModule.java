package am.highapps.theguardiannews.di;


import am.highapps.theguardiannews.di.main.MainModule;
import am.highapps.theguardiannews.ui.main.MainActivity;
import am.highapps.theguardiannews.ui.newsitem.NewsItemActivity;
import am.highapps.theguardiannews.ui.newslist.NewsListFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @FragmentScope
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract NewsListFragment bindNewsListFragment();

    @ActivityScope
    @ContributesAndroidInjector
    abstract NewsItemActivity bindNewsItemActivity();

}
