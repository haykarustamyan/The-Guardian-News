package am.highapps.theguardiannews.di;

import javax.inject.Singleton;

import am.highapps.theguardiannews.App;
import am.highapps.theguardiannews.di.network.NetworkModule;
import am.highapps.theguardiannews.di.network.ServiceModule;
import am.highapps.theguardiannews.di.viewmodel.ViewModelModule;
import am.highapps.theguardiannews.di.worker.NotificationWorkerModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ViewModelModule.class,
        NotificationWorkerModule.class,
        NetworkModule.class,
        ServiceModule.class,
        DataModule.class,
        BuildersModule.class})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

}
