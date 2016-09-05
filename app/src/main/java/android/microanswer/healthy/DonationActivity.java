package android.microanswer.healthy;

import android.os.Bundle;

/**
 * 捐赠Activity
 * 由 Micro 创建于 2016/7/25.
 */

public class DonationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        suitToolBar(R.id.avtivity_donation_toolbar);
        setToolBarBackEnable();
    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }
}
