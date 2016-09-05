package android.microanswer.healthy.viewbean;

import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/6/24.
 */

public class HealthyItemItemKnowledge extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tv_names[];
    private ImageView iv_imgs[];
    private View clickviews[];

    private List<LoreListItem> data;

    private OnHealthyItemKnowledgeClickListener onHealthyItemKnowledgeClickListener;

    public HealthyItemItemKnowledge(View itemView) {
        super(itemView);
        tv_names = new TextView[2];
        iv_imgs = new ImageView[2];
        clickviews = new View[2];
        tv_names[0] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_desc1);
        tv_names[1] = (TextView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_desc2);
        iv_imgs[0] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_img1);
        iv_imgs[1] = (ImageView) itemView.findViewById(R.id.viewpager_healthy_itemitem_health_knowledge_img2);
        clickviews[0] = itemView.findViewById(R.id.knowledge_item1);
        clickviews[1] = itemView.findViewById(R.id.knowledge_item2);
        clickviews[0].setOnClickListener(this);
        clickviews[1].setOnClickListener(this);
    }

    public void setData(List<LoreListItem> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LoreListItem item = data.get(i);
            tv_names[i].setText(item.getDescription());
            if (item.getImg() != null)
                ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + item.getImg(), iv_imgs[i]);
        }
    }

    public OnHealthyItemKnowledgeClickListener getOnHealthyItemKnowledgeClickListener() {
        return onHealthyItemKnowledgeClickListener;
    }

    public void setOnHealthyItemKnowledgeClickListener(OnHealthyItemKnowledgeClickListener onHealthyItemKnowledgeClickListener) {
        this.onHealthyItemKnowledgeClickListener = onHealthyItemKnowledgeClickListener;
    }

    @Override
    public void onClick(View view) {
        if (this.onHealthyItemKnowledgeClickListener != null && data != null) {
            if (view == clickviews[0]) {
                onHealthyItemKnowledgeClickListener.onclick(data.get(0));
            } else if (view == clickviews[1]) {
                onHealthyItemKnowledgeClickListener.onclick(data.get(1));
            }
        }
    }


    public interface OnHealthyItemKnowledgeClickListener {
        void onclick(LoreListItem item);
    }
}
