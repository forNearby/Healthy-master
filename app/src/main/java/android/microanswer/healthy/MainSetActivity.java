package android.microanswer.healthy;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 该Activity用于设置主界面显示什么数据
 * 由 Micro 创建于 2016/7/25.
 */

public class MainSetActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_set);
        suitToolBar(R.id.activity_main_set_toolbar);
        setToolBarBackEnable();
    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }
}
