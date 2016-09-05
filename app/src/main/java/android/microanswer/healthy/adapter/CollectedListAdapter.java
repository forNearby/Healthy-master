package android.microanswer.healthy.adapter;

import android.content.Context;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.Collected;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 由 Micro 创建于 2016/8/4.
 */

public class CollectedListAdapter extends BaseAdapter {

    private List<Collected> data;
    private Context context;

    public CollectedListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Collected> data) {
        if (this.data == null) {
            this.data = data;
            notifyDataSetChanged();
            return;
        }
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addData(Collected data) {
        if (this.data == null) {
            return;
        }
        this.data.add(data);
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (this.data == null) {
            return;
        }

        this.data.clear();
        this.notifyDataSetChanged();
    }

    public Collected deleteItem(int postion) {
        Collected delete = delete(postion);
        notifyDataSetChanged();
        return delete;
    }

    private Collected delete(int position) {
        if (this.data != null) {
            if (this.data.size() - 1 <= position) {
                return this.data.remove(position);
            }
        }
        return null;
    }


    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }

        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(context, R.layout.view_collected_item, null);
            vh.title = (TextView) view.findViewById(R.id.view_collected_item_title);
            vh.tag = (TextView) view.findViewById(R.id.view_collected_item_tag);
            vh.time = (TextView) view.findViewById(R.id.view_collected_item_time);
            vh.type = (TextView) view.findViewById(R.id.view_collected_item_type);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        Collected collected = (Collected) getItem(i);

        vh.title.setText(collected.getTitle());
        vh.time.setText("收藏时间:" + new Date(collected.getTime()).toLocaleString());
        vh.tag.setText(collected.getTag());

        vh.type.setText(getType(collected.getOtype()));


        return view;
    }

    private String getType(String type) {
        if (type.equalsIgnoreCase("lore")) {
            return "健康知识";
        } else if (type.equalsIgnoreCase("info")) {
            return "健康咨询";
        } else if (type.equalsIgnoreCase("book")) {
            return "健康图书";
        } else if (type.equalsIgnoreCase("cook")) {
            return "健康菜谱";
        } else if (type.equalsIgnoreCase("food")) {
            return "健康食物";
        } else if (type.equalsIgnoreCase("ask")) {
            return "健康问答";
        } else if (type.equalsIgnoreCase("blog")) {
            return "博客";
        } else if (type.equalsIgnoreCase("memo")) {
            return "说说";
        } else {
            return "其他";
        }

    }


    private class ViewHolder {
        TextView title, type, tag, time;
    }
}
