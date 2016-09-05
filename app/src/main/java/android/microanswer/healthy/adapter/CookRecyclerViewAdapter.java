package android.microanswer.healthy.adapter;

import android.content.Context;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.CookListItem;
import android.microanswer.healthy.fragment.CookFragment;
import android.microanswer.healthy.viewbean.CookItemHolder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * 健康才欧里面的RecyclerView的适配器
 * 由 Micro 创建于 2016/7/18.
 */

public class CookRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CookItemHolder.OnClickListener {

    private SparseArray<List<CookListItem>> data;
    private Context context;
    private int currentClassify = -1;

    public CookRecyclerViewAdapter(Context context) {
        this.context = context;
        this.data = new SparseArray<>();
    }

    public void setCurrentClassify(int currentClassify) {
        if (this.currentClassify == currentClassify) {
            return;
        }
        this.currentClassify = currentClassify;
        notifyDataSetChanged();
    }

    /**
     * 获取当前显示的分类
     *
     * @return
     */
    public int getCurrentClassify() {
        return currentClassify;
    }

    /**
     * 设置对应菜谱分类的数据
     *
     * @param classify
     * @param classifyData
     */
    public void setClassifyData(int classify, List<CookListItem> classifyData) {
        this.data.put(classify, classifyData);
        if (this.currentClassify == classify) {
            notifyDataSetChanged();
        }
    }

    /**
     * 追加对应分类数据
     *
     * @param classify
     * @param data
     */
    public void appendClassifyData(int classify, List<CookListItem> data) {
        List<CookListItem> cookListItems = this.data.get(classify);
        int oldSize = cookListItems.size();
        cookListItems.addAll(data);
        if (classify == currentClassify) {
            notifyItemRangeInserted(oldSize + 1, data.size());
        }
    }

    /**
     * 获取对应分类的数据
     *
     * @param classify
     * @return
     */
    public List<CookListItem> getClassifyData(int classify) {
        return this.data.get(classify);
    }

    /**
     * 获取当前显示了几页
     * @return
     */
    public int getCurrentClassifyPage() {
        return getClassifyData(currentClassify).size() / CookFragment.PAGE_COUNT;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = View.inflate(context, R.layout.view_cook_item, null);
        return new CookItemHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CookItemHolder) {
            CookItemHolder cookItemHolder = (CookItemHolder) holder;
            cookItemHolder.setOnClickListener(this);
            CookListItem cookListItem = data.get(currentClassify).get(position);
            cookItemHolder.setCookListItem(cookListItem);
        }

    }

    @Override
    public int getItemCount() {
        if (currentClassify == -1 || data.get(currentClassify) == null) {
            Toast.makeText(context, "No Data To Show With CookClassify " + currentClassify + " !", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return data.get(currentClassify).size();
    }


    @Override
    public void onclick(CookListItem cookListItem) {
        if (onClickListener != null) {
            onClickListener.onClick(cookListItem);
        }
    }

    private OnClickListener onClickListener;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(Object obj);
    }
}
