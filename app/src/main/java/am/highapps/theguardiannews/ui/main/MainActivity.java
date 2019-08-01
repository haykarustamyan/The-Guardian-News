package am.highapps.theguardiannews.ui.main;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.ui.base.BaseActivity;
import am.highapps.theguardiannews.ui.newslist.NewsListFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarTitle(getString(R.string.app_name));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_main_container, NewsListFragment.newInstance());
            transaction.commit();
        }
    }
}
