package android.microanswer.healthy.viewbean;

import android.microanswer.healthy.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 由 Micro 创建于 2016/6/24.
 */

public class HealthyItemGroup extends RecyclerView.ViewHolder {
    private View itemView;

    private TextView tv_name;
    private TextView tv_smallName;

    public HealthyItemGroup(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.itemView.setTag(1);
        tv_name = (TextView) findViewById(R.id.viewpager_healthy_itemgroup_textview_name);
        tv_smallName = (TextView) findViewById(R.id.viewpager_healthy_itemgroup_textview_smallname);
    }

    public void setName(String name) {
        this.tv_name.setText(name);
    }

    public void setSmallName(String smallname) {
        this.tv_smallName.setText(smallname);
    }


    private View findViewById(int id) {
        return this.itemView.findViewById(id);
    }


}
