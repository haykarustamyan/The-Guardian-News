package am.highapps.theguardiannews;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import am.highapps.theguardiannews.di.DaggerAppComponent;
import am.highapps.theguardiannews.util.NotificationWorkerFactory;
import am.highapps.theguardiannews.worker.NotificationWorker;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

import static am.highapps.theguardiannews.util.Constant.NOTIFICATION_CHANEL_ID;
import static am.highapps.theguardiannews.util.Constant.SCHEDULE_TIME;
import static am.highapps.theguardiannews.util.Constant.WORK_TAG;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class App extends DaggerApplication implements LifecycleObserver {



    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Inject
    NotificationWorkerFactory notificationWorkerFactory;

    private static App instance;
    private PeriodicWorkRequest periodicWorkRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initializeWorkManager();
        setupLifecycleListener();
        createWorker();
        createNotificationChannel();
    }

    private void initializeWorkManager() {
        WorkManager.initialize(
                this,
                new Configuration.Builder().setWorkerFactory(notificationWorkerFactory)
                        .build()
        );
    }

    private void setupLifecycleListener() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    private void createWorker() {
        //        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(true).build();
        periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, SCHEDULE_TIME, TimeUnit.MINUTES)
                .addTag(TAG)
//                .setConstraints(constraints)
                .build();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        WorkManager.getInstance(this).cancelAllWorkByTag(WORK_TAG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }


    public static App getInstance() {
        return instance;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
