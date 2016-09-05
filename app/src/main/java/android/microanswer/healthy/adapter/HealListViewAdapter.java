package android.microanswer.healthy.adapter;

import android.content.Context;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.viewbean.HealthyItemItemInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 由 Micro 创建于 2016/8/8.
 */

public class HealListViewAdapter extends BaseAdapter implements HealthyItemItemInfo.OnHealthyItemItemInfoClickListener {
    private String TAG = "HealListViewAdapter";

    private List<List<InfoListItem>> data;
    private Context context;

    public HealListViewAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<List<InfoListItem>> datas) {
        if (datas == null) {
            Log.i(TAG, "健康咨询列表，设置数据为空");
            return;
        }

        if (this.data == null) {
            this.data = datas;
        } else {
            this.data.clear();
            this.data.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void addData(List<InfoListItem> data) {
        if (this.data == null) {
            Log.i(TAG, "适配器未设置数据源");
        } else {
            this.data.add(data);
            notifyDataSetChanged();
        }
    }

    public List<InfoListItem> delete(int index) {
        if (this.data == null) {
            Log.i(TAG, "适配器未设置数据源");
        } else {
            if (index < this.data.size()) {
                return data.remove(index);
            }
        }
        return null;
    }

    public void appendData(List<List<InfoListItem>> datas) {
        if (datas == null) {
            Log.i(TAG, "健康咨询列表，设置数据为空");
            return;
        }

        if (this.data == null) {
            this.data = datas;
        } else {
            this.data.addAll(datas);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        if (this.data == null) {
            return 0;
        }

        return this.data.size();
    }

    @Override
    public Object getItem(int i) {

        if (data == null) {
            return null;
        }

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HealthyItemItemInfo vh = null;
        if (view == null) {
            view = View.inflate(context, R.layout.healthy_itemitem_health_knowledge, null);
            vh = new HealthyItemItemInfo(view, context);
            view.setTag(vh);
        } else {
            vh = (HealthyItemItemInfo) view.getTag();
        }
        vh.setData(data.get(i));
        vh.setOnHealthyItemItemInfoClickListener(this);

        return view;

    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onclick(InfoListItem item) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(item);
        }
    }

    public static interface OnItemClickListener {
        void onClick(Object object);
    }
}
