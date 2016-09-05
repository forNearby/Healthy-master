package android.microanswer.healthy.viewbean;

import android.content.Context;
import android.microanswer.healthy.R;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 由 Micro 创建于 2016/8/8.
 */

public class ListHeadView extends LinearLayout implements View.OnClickListener, SmartBannerViewHolder.OnSmartBannerItemClickListener {
    private final String TAG = "ListHeadView";

    private SmartBannerViewHolder smartBannerViewHolder;

    private ImageView lore_imgs[];
    private TextView lore_tvs[];

    private ImageView book_imgs[];
    private TextView book_tvs[];


    public ListHeadView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        inflate(context, R.layout.healthy_list_headview, this);
        smartBannerViewHolder = new SmartBannerViewHolder(findViewById(R.id.healthy_list_headview_banner));
        smartBannerViewHolder.setOnSmartBannerItemClickListener(this);
        lore_imgs = new ImageView[6];
        lore_imgs[0] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_1);
        lore_imgs[1] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_2);
        lore_imgs[2] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_3);
        lore_imgs[3] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_4);
        lore_imgs[4] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_5);
        lore_imgs[5] = (ImageView) findViewById(R.id.healthy_list_headview_lore_img_6);
        lore_tvs = new TextView[6];
        lore_tvs[0] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_1);
        lore_tvs[1] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_2);
        lore_tvs[2] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_3);
        lore_tvs[3] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_4);
        lore_tvs[4] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_5);
        lore_tvs[5] = (TextView) findViewById(R.id.healthy_list_headview_lore_tv_6);

        book_imgs = new ImageView[9];
        book_tvs = new TextView[9];

        book_imgs[0] = (ImageView) findViewById(R.id.healthy_list_headview_book_img1);
        book_imgs[1] = (ImageView) findViewById(R.id.healthy_list_headview_book_img2);
        book_imgs[2] = (ImageView) findViewById(R.id.healthy_list_headview_book_img3);
        book_imgs[3] = (ImageView) findViewById(R.id.healthy_list_headview_book_img4);
        book_imgs[4] = (ImageView) findViewById(R.id.healthy_list_headview_book_img5);
        book_imgs[5] = (ImageView) findViewById(R.id.healthy_list_headview_book_img6);
        book_imgs[6] = (ImageView) findViewById(R.id.healthy_list_headview_book_img7);
        book_imgs[7] = (ImageView) findViewById(R.id.healthy_list_headview_book_img8);
        book_imgs[8] = (ImageView) findViewById(R.id.healthy_list_headview_book_img9);

        book_tvs[0] = (TextView) findViewById(R.id.healthy_list_headview_book_tv1);
        book_tvs[1] = (TextView) findViewById(R.id.healthy_list_headview_book_tv2);
        book_tvs[2] = (TextView) findViewById(R.id.healthy_list_headview_book_tv3);
        book_tvs[3] = (TextView) findViewById(R.id.healthy_list_headview_book_tv4);
        book_tvs[4] = (TextView) findViewById(R.id.healthy_list_headview_book_tv5);
        book_tvs[5] = (TextView) findViewById(R.id.healthy_list_headview_book_tv6);
        book_tvs[6] = (TextView) findViewById(R.id.healthy_list_headview_book_tv7);
        book_tvs[7] = (TextView) findViewById(R.id.healthy_list_headview_book_tv8);
        book_tvs[8] = (TextView) findViewById(R.id.healthy_list_headview_book_tv9);

        for (ImageView lore_img : lore_imgs) {
            lore_img.setOnClickListener(this);
        }
        for (ImageView book_img : book_imgs) {
            book_img.setOnClickListener(this);
        }

        findViewById(R.id.healthy_list_headview_moerlore).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMoreLoreClick();
                }
            }
        });

    }

    public void setBannerData(List<InfoListItem> datas) {
        smartBannerViewHolder.setData(datas);
    }


    public void setLoreListData(List<LoreListItem> loredatas) {
        if (loredatas.size() == 6) {
            for (int i = 0; i < loredatas.size(); i++) {
                LoreListItem item = loredatas.get(i);
                lore_imgs[i].setTag(item);
                ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + item.getImg(), lore_imgs[i]);
                lore_tvs[i].setText(item.getTitle());
            }
        } else {
            Log.i(TAG, "健康知识数据数量不正确");
        }

    }

    public void setBookListData(List<BookListItem> datas) {
        if (datas != null)
            if (datas.size() == 9) {
                for (int i = 0; i < datas.size(); i++) {
                    BookListItem item = datas.get(i);
                    book_tvs[i].setText(item.getName());
                    book_imgs[i].setTag(item);
                    ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + item.getImg(), book_imgs[i]);
                }
            } else {
                Log.i(TAG, "健康图书的数据条数不正确");
            }

    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view != null && view.getTag() != null) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(view.getTag());
            }
        }
    }

    @Override
    public void onItemClick(InfoListItem item) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(item);
        }
    }

    public static interface OnItemClickListener {
        void onClick(Object obj);

        void onMoreLoreClick();
    }

}
