package android.microanswer.healthy;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 关于软件界面
 * 由 Micro 创建于 2016/7/19.
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        suitToolBar(R.id.activity_about_toolbar);
        setToolBarBackEnable();
    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }
}
