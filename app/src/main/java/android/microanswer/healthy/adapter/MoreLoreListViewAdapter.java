package android.microanswer.healthy.adapter;

import android.microanswer.healthy.R;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.LoreListItem;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Calendar;
import java.util.List;

/**
 * 由 Micro 创建于 2016/8/16.
 */

public class MoreLoreListViewAdapter extends BaseAdapter {

    private List<LoreListItem> data = null;

    public void setData(List<LoreListItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void addDatas(List<LoreListItem> datas) {
        if (this.data == null) {
            this.data = datas;
        } else {
            this.data.addAll(datas);
        }
        notifyDataSetChanged();
    }


    public void addData(LoreListItem data) {
        if (this.data == null) {
            return;
        }
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        if (data == null) {
            return;
        }
        this.data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (this.data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int i) {

        if (this.data == null) {
            return null;
        }

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (this.data == null) {
            return i;
        }
        return this.data.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VieHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.view_morelore_item, null);
            viewHolder = new VieHolder();
            viewHolder.count = (TextView) view.findViewById(R.id.view_morelore_item_count);
            viewHolder.desc = (TextView) view.findViewById(R.id.view_morelore_item_desc);
            viewHolder.img = (ImageView) view.findViewById(R.id.view_morelore_item_img);
            viewHolder.time = (TextView) view.findViewById(R.id.view_morelore_item_time);
            viewHolder.title = (TextView) view.findViewById(R.id.view_morelore_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (VieHolder) view.getTag();
        }

        LoreListItem item = (LoreListItem) getItem(i);

        viewHolder.title.setText(item.getTitle());
        viewHolder.desc.setText("　　" + item.getDescription());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.getTime());
        viewHolder.time.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
        viewHolder.count.setText(item.getCount() + "次");
        ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + item.getImg(), viewHolder.img);
        return view;
    }


    private class VieHolder {
        private ImageView img;
        private TextView title, desc, time, count;
    }
}
