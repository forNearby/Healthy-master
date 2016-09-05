package android.microanswer.healthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.microanswer.healthy.adapter.FriendsListAdapter;
import android.microanswer.healthy.bean.Friend;
import android.microanswer.healthy.bean.FriendGroup;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.ChineseTool;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.microanswer.healthy.view.LetterView;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 关注好友列表
 * 由 Micro 创建于 2016/7/20.
 */

public class FriendsActivity extends BaseActivity implements AdapterView.OnItemClickListener, LetterView.OnLetterSelectListener, View.OnClickListener, AbsListView.OnScrollListener {

    private ListView listView;
    private LetterView letterView;
    private static FriendsListAdapter adapter;

    private View load_fail_view;
    private View loading_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        suitToolBar(R.id.activity_friends_toolbar);
        setToolBarBackEnable();
        letterView = (LetterView) findViewById(R.id.activity_friends_letterview);
        letterView.setOnLetterSelectListener(this);
        listView = (ListView) findViewById(R.id.activity_friends_listview);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        load_fail_view = findViewById(R.id.activity_friends_loading_fail_view);
        load_fail_view.setOnClickListener(this);
        loading_view = findViewById(R.id.activity_friends_loading_view);
        listView.setEmptyView(loading_view);
        if (adapter == null) {
            initData();
        } else {
            listView.setAdapter(adapter);
        }
    }

    private void initData() {
        runOnOtherThread(new BaseOtherThread() {

            @Override
           public void onOtherThreadRunEnd(Message msg) {
                List<Friend> friends = (List<Friend>) msg.obj;

                loading_view.setVisibility(View.GONE);

                if (friends == null) {
                    load_fail_view.setVisibility(View.VISIBLE);
                    return;
                }

                load_fail_view.setVisibility(View.GONE);
                adapter = new FriendsListAdapter(FriendsActivity.this);
                adapter.setData(friends);
                listView.setAdapter(adapter);
            }

            @Override
            public Map getTaskParams() {
                loading_view.setVisibility(View.VISIBLE);
                return null;
            }

            @Override
            public Message run(Map params) {
                List<Friend> friends = JavaBeanTools.UserInterface.myHeart(30, 1, User.getUser().getAccount(), User.getUser().getAccess_token());
//                Collections.sort(friends);

                if (friends == null || friends.size() < 1) {
                    return new Message();
                }

                for (int i = 0; i < friends.size(); i++) {
                    Friend f = friends.get(i);
                    char letter = ChineseTool.getInstance().convert((f.getAccount().charAt(0) + "")).toLowerCase().charAt(0);
                    if (letter < 'a') {
                        letter = '*';
                    }

                    if (letter > 'z') {
                        letter = '#';
                    }

                    f.setLetter(String.valueOf(letter));
                }
                Collections.sort(friends);

                List<Friend> data = new ArrayList<Friend>();
                for (Friend f : friends) {
                    if (data.size() < 1) {//第一个数据
                        FriendGroup friendgroup = new FriendGroup();
                        friendgroup.setLetter(f.getLetter());
                        data.add(friendgroup);
                        data.add(f);
                        continue;
                    }
                    Friend friendlast = data.get(data.size() - 1);
                    if (f.getLetter().equals(friendlast.getLetter())) {
                        data.add(f);
                    } else {
                        FriendGroup friendgroup = new FriendGroup();
                        friendgroup.setLetter(f.getLetter());
                        data.add(friendgroup);
                        data.add(f);
                    }
                }

                Message msg = new Message();
                msg.obj = data;
                return msg;
            }
        }, 1);
    }



    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Friend friend = (Friend) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, FriendActivity.class);
        intent.putExtra("data", friend);
        startActivity(intent, false);
    }

    @Override
    public void onLetterSelected(int index, char letter) {
        if (listView.getAdapter() == null || adapter == null) {
            return;
        }

        int letterPosition = adapter.getLetterPosition(letter);
        Log.i("TEST", "letterPosition=" + letterPosition);
        if (letterPosition != -1) {
            listView.setSelection(letterPosition);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_friends_loading_fail_view) {
            initData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Object itemAtPosition = absListView.getItemAtPosition(i);
        if (itemAtPosition instanceof FriendGroup) {
            FriendGroup fg = (FriendGroup) itemAtPosition;
            letterView.setCurrentLetter(fg.getLetter().charAt(0));
        }
    }
}
