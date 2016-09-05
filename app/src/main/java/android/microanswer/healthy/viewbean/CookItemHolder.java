package android.microanswer.healthy.viewbean;

import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.CookListItem;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 由 Micro 创建于 2016/7/18.
 */

public class CookItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView img;
    private TextView tv;
    private CookListItem cookListItem;
    private OnClickListener onClickListener;


    public CookItemHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.view_cook_item_img);
        img.setOnClickListener(this);
        tv = (TextView) itemView.findViewById(R.id.view_cook_item_title);
    }


    public void setImg(String url) {
        ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + url, img);
    }

    public void setTitle(String title) {
        this.tv.setText(title);
    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null) {
            onClickListener.onclick(this.cookListItem);
        }
    }

    public CookListItem getCookListItem() {
        return cookListItem;
    }

    public void setCookListItem(CookListItem cookListItem) {
        this.cookListItem = cookListItem;
        setImg(this.cookListItem.getImg());
        setTitle(this.cookListItem.getName());
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onclick(CookListItem cookListItem);
    }
}
