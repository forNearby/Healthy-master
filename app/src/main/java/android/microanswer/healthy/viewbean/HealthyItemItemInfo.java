package android.microanswer.healthy.viewbean;

import android.content.Context;
import android.graphics.Bitmap;
import android.microanswer.healthy.R;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/6/24.
 */

public class HealthyItemItemInfo extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tv_names[];
    private ImageView iv_imgs[];
    private View clickviews[];
    private List<InfoListItem> data;
    private OnHealthyItemItemInfoClickListener onHealthyItemItemInfoClickListener;
    private Context context;

    public HealthyItemItemInfo(View itemView,Context context) {
        super(itemView);
        this.context = context;
        tv_names = new TextView[2];
        iv_imgs = new ImageView[2];
        clickviews = new View[2];
        tv_names[0] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_desc1);
        tv_names[1] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_desc2);
        iv_imgs[0] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_img1);
        iv_imgs[1] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_img2);
        clickviews[0] = itemView.findViewById(R.id.knowledge_item1);
        clickviews[0].setOnClickListener(this);
        clickviews[1] = itemView.findViewById(R.id.knowledge_item2);
        clickviews[1].setOnClickListener(this);
    }

    public void setData(List<InfoListItem> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            InfoListItem item = data.get(i);
            tv_names[i].setText(item.getTitle());
            if (item.getImg() != null) {
                if (iv_imgs[i].getTag() != null && iv_imgs[i].getTag().equals(Healthy.IMAGE_URL + item.getImg())) {
                    return;
                }
                ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + item.getImg(), iv_imgs[i], new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        view.setTag(imageUri);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        }
    }

    public OnHealthyItemItemInfoClickListener getOnHealthyItemItemInfoClickListener() {
        return onHealthyItemItemInfoClickListener;
    }

    public void setOnHealthyItemItemInfoClickListener(OnHealthyItemItemInfoClickListener onHealthyItemItemInfoClickListener) {
        this.onHealthyItemItemInfoClickListener = onHealthyItemItemInfoClickListener;
    }

    @Override
    public void onClick(View view) {
        if (onHealthyItemItemInfoClickListener != null && data != null) {
            if (view == clickviews[0]) {
                onHealthyItemItemInfoClickListener.onclick(data.get(0));
            } else if (view == clickviews[1]) {
                onHealthyItemItemInfoClickListener.onclick(data.get(1));
            }
        }
    }

    public interface OnHealthyItemItemInfoClickListener {
        void onclick(InfoListItem item);
    }


}
