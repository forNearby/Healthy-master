package android.microanswer.healthy;

import android.app.Activity;
import android.database.ContentObserver;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 没有的用于代码测试的Activity
 * 由 Micro 创建于 2016/7/13.
 */

public class NoUseFroTestActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runOnOtherThread(new BaseOtherThread(){

            @Override
            public Map getTaskParams() {
                return null;
            }

            @Override
            public Message run(Map params) {
                return null;
            }

            @Override
            void onOtherThreadRunEnd(Message msg) {

            }
        },4354);

    }
}
