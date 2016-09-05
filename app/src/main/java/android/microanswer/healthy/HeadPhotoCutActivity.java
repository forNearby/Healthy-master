package android.microanswer.healthy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * 头像图片裁剪界面
 * 由 Micro 创建于 2016/8/18.
 */

public class HeadPhotoCutActivity extends BaseActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphotocut);
        suitToolBar(R.id.activity_headphotocut_toolbar);
        setToolBarBackEnable();
        imageView = (ImageView) findViewById(R.id.activity_headphotocut_img);
        imageView.setImageURI(getIntent().getData());
    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }
}
