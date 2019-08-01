package am.highapps.theguardiannews.di.main;

import am.highapps.theguardiannews.di.AppComponent;
import am.highapps.theguardiannews.di.FragmentScope;
import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {MainModule.class})
public interface MainComponent {

    @Component.Builder
    interface Builder {

        Builder appComponent(AppComponent component);

        MainComponent build();
    }
}
