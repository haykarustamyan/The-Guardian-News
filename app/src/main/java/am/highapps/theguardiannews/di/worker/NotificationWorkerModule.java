package am.highapps.theguardiannews.di.worker;

import am.highapps.theguardiannews.worker.NotificationWorker;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class NotificationWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(NotificationWorker.class)
    abstract ChildWorkerFactory bindNotificationWorker(NotificationWorker.Factory factory);

}
