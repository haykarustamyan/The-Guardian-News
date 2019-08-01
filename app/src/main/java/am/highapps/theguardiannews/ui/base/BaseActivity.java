package am.highapps.theguardiannews.ui.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.core.content.ContextCompat;

import javax.inject.Inject;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.ui.main.MainActivity;
import am.highapps.theguardiannews.util.InternetReceiver;
import am.highapps.theguardiannews.util.NetworkReceiverListener;
import am.highapps.theguardiannews.util.NetworkUtil;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity implements NetworkReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Inject
    NetworkUtil networkUtil;

    private Toolbar mToolbar;
    private TextView mTvToolbarTitle;
    private TextView toolbarErrorText;
    private RelativeLayout errorBar;

    private InternetReceiver networkReceiver;

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        findViews();
        initToolbar();
        initNetworkReceiver();
    }

    protected void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.tb);
        if (mToolbar != null) {
            mTvToolbarTitle = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        }
        errorBar = (RelativeLayout) findViewById(R.id.rl_error_bar);
        toolbarErrorText = (TextView) findViewById(R.id.tv_error);
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initNetworkReceiver() {
        networkReceiver = new InternetReceiver();
        networkReceiver.addListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (networkReceiver != null) {
            this.registerReceiver(networkReceiver, new IntentFilter(CONNECTIVITY_CHANGE_ACTION));
        }
    }

    @Override
    protected void onStop() {
        if (networkReceiver != null) {
            this.unregisterReceiver(networkReceiver);
        }
        super.onStop();
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void showActionBar() {
        getSupportActionBar().show();
    }

    public void setActionBarTitle(String title) {
        mTvToolbarTitle.setText(title);
    }

    public void hideActionBarIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void showActionBarIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void hideErrorsBar(boolean hide) {
        try {
            if (errorBar != null) {
                if (hide && networkUtil.isConnected()) {
                    new CountDownTimer(4000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            errorBar.setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.green));
                            toolbarErrorText.setText(getResources().getString(R.string.connected));
                        }

                        @Override
                        public void onFinish() {
                            toolbarErrorText.setText(getResources().getString(R.string.waiting_for_network));
                            errorBar.setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.network));
                            errorBar.setVisibility(View.GONE);
                        }
                    }.start();

                } else {
                    errorBar.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void networkAvailable() {
        hideErrorsBar(true);
    }

    @Override
    public void networkUnavailable() {
        hideErrorsBar(false);
    }

}
