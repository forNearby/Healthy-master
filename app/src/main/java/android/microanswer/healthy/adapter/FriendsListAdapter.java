package android.microanswer.healthy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.Friend;
import android.microanswer.healthy.bean.FriendGroup;
import android.microanswer.healthy.tools.BaseTools;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/7/22.
 */

public class FriendsListAdapter extends BaseAdapter {

    private static final int TYPE_FRIEND = 2;
    private static final int TYPE_GROUP = 1;

    private Context context;
    private List<Friend> data;

    public FriendsListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<Friend> data) {
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addData(Friend data) {
        this.data.add(data);
        this.notifyDataSetChanged();
    }

    public List<Friend> getData() {
        return data;
    }

    public Friend getData(int index) {
        return data.get(index);
    }

    /**
     * 返回指定字母对应的下标,没有则返回-1
     *
     * @param l
     * @return
     */
    public int getLetterPosition(char l) {
        for (int i = 0; i < data.size(); i++) {
            Friend data = getData(i);
            if (data instanceof FriendGroup) {
                if (data.getLetter().equals((l + "").toLowerCase()))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return getData(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        Friend friend = getData(i);
        if (type == TYPE_FRIEND) {
            FriendHolder fh;
            if (view == null) {
                view = View.inflate(context, R.layout.view_friends_item, null);
                fh = new FriendHolder();
                fh.img = (ImageView) view.findViewById(R.id.view_friends_item_img);
                fh.name = (TextView) view.findViewById(R.id.view_friends_item_name);
                fh.desc = (TextView) view.findViewById(R.id.view_friends_item_desc);
                view.setTag(fh);
            } else {
                fh = (FriendHolder) view.getTag();
            }
            ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + friend.getAvatar(), fh.img);
            fh.desc.setText(friend.getSignature());
            fh.name.setText(friend.getAccount());
        } else if (type == TYPE_GROUP) {
            GroupHolder gh;
            if (view == null) {
                TextView textView = new TextView(context);
//                textView.setTextSize(BaseTools.sp2px(context, 5f));
                textView.setBackgroundColor(Color.parseColor("#22000000"));
                textView.setPadding(BaseTools.Dp2Px(context, 6f), BaseTools.Dp2Px(context, 3f), BaseTools.Dp2Px(context, 6f), BaseTools.Dp2Px(context, 3f));
                view = textView;
                gh = new GroupHolder();
                gh.t = textView;
                view.setTag(gh);
            } else {
                gh = (GroupHolder) view.getTag();
            }
            gh.t.setText(friend.getLetter());
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        Friend data = getData(position);
        if (data instanceof FriendGroup) {
            return TYPE_GROUP;
        }
        return TYPE_FRIEND;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == TYPE_FRIEND;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }


    private class GroupHolder {
        TextView t;
    }

    private class FriendHolder {
        ImageView img;
        TextView name, desc;
    }
}
