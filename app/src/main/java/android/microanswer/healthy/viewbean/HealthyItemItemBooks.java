package android.microanswer.healthy.viewbean;

import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.BookListItem;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/6/24.
 */

public class HealthyItemItemBooks extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView[] tv_names;
    private ImageView[] iv_imgs;
    private View clickviews[];
    private List<BookListItem> data;
    private OnHealthyItemItemBooksClickListener onHealthyItemItemBooksClickListener;

    public HealthyItemItemBooks(View itemView) {
        super(itemView);
        tv_names = new TextView[3];
        iv_imgs = new ImageView[3];
        clickviews = new View[3];
        tv_names[0] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_title1);
        tv_names[1] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_title2);
        tv_names[2] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_title3);
        iv_imgs[0] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_img1);
        iv_imgs[1] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_img2);
        iv_imgs[2] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_books_img3);
        clickviews[0] = itemView.findViewById(R.id.books_item1);
        clickviews[0].setOnClickListener(this);
        clickviews[1] = itemView.findViewById(R.id.books_item2);
        clickviews[1].setOnClickListener(this);
        clickviews[2] = itemView.findViewById(R.id.books_item3);
        clickviews[2].setOnClickListener(this);
    }

    public void setData(List<BookListItem> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            BookListItem item = data.get(i);
            if (item == null) {
                continue;
            }
            tv_names[i].setText((item.getName() + "").replace("null", "未知书名"));
            if (item.getImg() != null) {
                ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + item.getImg(), iv_imgs[i]);
            }
        }

    }

    public OnHealthyItemItemBooksClickListener getOnHealthyItemItemBooksClickListener() {
        return onHealthyItemItemBooksClickListener;
    }

    public void setOnHealthyItemItemBooksClickListener(OnHealthyItemItemBooksClickListener onHealthyItemItemBooksClickListener) {
        this.onHealthyItemItemBooksClickListener = onHealthyItemItemBooksClickListener;
    }

    @Override
    public void onClick(View view) {
        if (onHealthyItemItemBooksClickListener != null && data != null) {
            if (view == clickviews[0]) {
                onHealthyItemItemBooksClickListener.onClick(data.get(0));
            } else if (view == clickviews[1]) {
                onHealthyItemItemBooksClickListener.onClick(data.get(1));
            } else if (view == clickviews[2]) {
                onHealthyItemItemBooksClickListener.onClick(data.get(2));
            }
        }
    }

    /**
     *
     */
    public interface OnHealthyItemItemBooksClickListener {
        void onClick(BookListItem item);
    }


}
