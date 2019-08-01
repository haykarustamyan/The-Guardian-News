package am.highapps.theguardiannews.di.worker;

import androidx.work.ListenableWorker;
import androidx.work.Worker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.MapKey;

@Retention(RetentionPolicy.RUNTIME)
@MapKey
@interface WorkerKey {
    Class<? extends ListenableWorker> value();
}
