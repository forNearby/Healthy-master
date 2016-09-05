package android.microanswer.healthy.adapter;

import android.content.Context;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.FoodListItem;
import android.microanswer.healthy.fragment.FoodFragment;
import android.microanswer.healthy.viewbean.CookItemHolder;
import android.microanswer.healthy.viewbean.FoodItemHolder;
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

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FoodItemHolder.OnClickListener {

    private SparseArray<List<FoodListItem>> data;
    private Context context;
    private int currentClassify = -1;

    public FoodRecyclerViewAdapter(Context context) {
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
    public void setClassifyData(int classify, List<FoodListItem> classifyData) {
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
    public void appendClassifyData(int classify, List<FoodListItem> data) {
        List<FoodListItem> cookListItems = this.data.get(classify);
        int oldSize = cookListItems.size();
        int addsize = 0;
        for (int i = 0; i < data.size(); i++) {
            if (!cookListItems.contains(data.get(i))) {
                cookListItems.add(data.get(i));
                addsize++;
            }
        }
        if(addsize==0){
            return;
        }
//        cookListItems.addAll(data);
        if (classify == currentClassify) {
            notifyItemRangeInserted(oldSize + 1, addsize);
        }
    }

    /**
     * 获取对应分类的数据
     *
     * @param classify
     * @return
     */
    public List<FoodListItem> getClassifyData(int classify) {
        return this.data.get(classify);
    }

    /**
     * 获取当前显示了几页
     * @return
     */
    public int getCurrentClassifyPage() {
        return getClassifyData(currentClassify).size() / FoodFragment.PAGE_COUNT;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = View.inflate(context, R.layout.view_cook_item, null);
        return new FoodItemHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FoodItemHolder) {
            FoodItemHolder cookItemHolder = (FoodItemHolder) holder;
            FoodListItem cookListItem = data.get(currentClassify).get(position);
            cookItemHolder.setOnClickListener(this);
            cookItemHolder.setfoodListItem(cookListItem);
        }

    }

    @Override
    public int getItemCount() {
        if (currentClassify == -1 || data.get(currentClassify) == null) {
//         Toast.makeText(context, "No Data To Show With CookClassify " + currentClassify + " !", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return data.get(currentClassify).size();
    }


    @Override
    public void onclick(FoodListItem foodListItem) {
        if (onClickListener != null) {
            onClickListener.onClick(foodListItem);
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
        void onClick(FoodListItem foodListItem);
    }
}
