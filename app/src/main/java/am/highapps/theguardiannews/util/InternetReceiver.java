package am.highapps.theguardiannews.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

public class InternetReceiver extends BroadcastReceiver {

    protected List<NetworkReceiverListener> listeners;

    protected Boolean isConnected;

    public InternetReceiver() {
        listeners = new ArrayList<NetworkReceiverListener>();
        isConnected = null;
    }

    private void notifyToAll() {
        for (NetworkReceiverListener listener : listeners)
            notifyListener(listener);
    }

    private void notifyListener(final NetworkReceiverListener listener) {
        if (isConnected == null || listener == null) {
            return;
        }

        if (isConnected) {
            listener.networkAvailable();
        } else {
            listener.networkUnavailable();
        }
    }

    public void addListener(final NetworkReceiverListener listener) {
        listeners.add(listener);
        notifyListener(listener);
    }

    public void removeListener(final NetworkReceiverListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        if (new NetworkUtil(context).isConnected()) {
            isConnected = true;
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            isConnected = false;
        }

        notifyToAll();
    }
}

