package am.highapps.theguardiannews.worker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;

import am.highapps.theguardiannews.App;
import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.api.TheGuardianNewsAPI;
import am.highapps.theguardiannews.data.entity.NewsResponse;
import am.highapps.theguardiannews.di.worker.ChildWorkerFactory;
import am.highapps.theguardiannews.ui.main.MainActivity;
import am.highapps.theguardiannews.ui.newslist.NewsListFragment;
import am.highapps.theguardiannews.util.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 import static am.highapps.theguardiannews.util.Constant.NOTIFICATION_CHANEL_ID;
import static am.highapps.theguardiannews.util.Constant.PREF_NEWEST_ARTICLE_PUBLICATION_DATE;
import static am.highapps.theguardiannews.util.DateTimeHelper.parsToStringDate;

public class NotificationWorker extends Worker {

    private static final String TAG = NotificationWorker.class.getSimpleName();
    private Context mContext;
    private TheGuardianNewsAPI guardianNewsAPI;
    private NetworkUtil networkUtil;

    public NotificationWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams, TheGuardianNewsAPI guardianNewsAPI, NetworkUtil networkUtil) {
        super(appContext, workerParams);
        this.mContext = appContext;
        this.guardianNewsAPI = guardianNewsAPI;
        this.networkUtil = networkUtil;
    }

    public static class Factory implements ChildWorkerFactory {
        private TheGuardianNewsAPI guardianNewsAPI;
        private NetworkUtil networkUtil;

        @Inject
        public Factory(TheGuardianNewsAPI guardianNewsAPI, NetworkUtil networkUtil) {

            this.guardianNewsAPI = guardianNewsAPI;
            this.networkUtil = networkUtil;
        }

        @Override
        public ListenableWorker create(Context appContext, WorkerParameters workerParams) {
            return new NotificationWorker(appContext, workerParams, guardianNewsAPI, networkUtil);
        }
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e(TAG, "Start Work");

        if (networkUtil.isConnected()) {
            checkNewItem(PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getString(PREF_NEWEST_ARTICLE_PUBLICATION_DATE, null));
        }

        return Result.success();
    }


    public void checkNewItem(String data) {
        if (data == null) {
            return;
        }

        guardianNewsAPI.getNewsItemsFromDate(parsToStringDate(data), 1, 1).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse itemResponse = response.body();
                    if (itemResponse != null) {
                        if (!itemResponse.getResponse().getNewsResponseEntities().get(0).getWebPublicationDate()
                                .equals(PreferenceManager.getDefaultSharedPreferences(App.getInstance())
                                        .getString(PREF_NEWEST_ARTICLE_PUBLICATION_DATE, null))) {
                            createNotification();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, "Failed getting NewsItemResponse: " + t.getMessage());
            }
        });
    }

    private void createNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle(mContext.getString(R.string.latest_news))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        final PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notificationManager.notify(1, builder.build());
    }

}


