package android.microanswer.healthy;

import android.microanswer.healthy.fragment.MPreferenceFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by MicroAnswer on 2016/6/28.
 */
public class SetActivity extends BaseActivity {
    private MPreferenceFragment preferenceFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        suitToolBar(R.id.activity_set_toolbat);
        setToolBarBackEnable();
        fragmentManager = getSupportFragmentManager();
        preferenceFragment = new MPreferenceFragment();
    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }
}
