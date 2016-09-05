package android.microanswer.healthy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.ColorUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/8/10.
 */

public class MoreGridviewAdapter extends BaseAdapter {

//    private List<Item> asymmetricItems = new ArrayList<>();

    private final Item[] items = {
            new Item("药品信息"), new Item("疾病信息"), new Item("病状信息"), new Item("手术项目"),
            new Item("检查项目"), new Item("医院门诊"), new Item("药企药厂"), new Item("药店药房"),
            new Item("农业新闻"), new Item("农业技术"), new Item("博客"), new Item("论坛"),
            new Item("业务"), new Item("社会热点"), new Item("天狗阅图"), new Item("农政揽要"),
            new Item("地方快报"), new Item("农贸经济"), new Item("农业科技"), new Item("三农人文"),
            new Item("农机农品"), new Item("创业推广"), new Item("农牧渔林"), new Item("手术项目"),
            new Item("粮食种植"), new Item("家禽养殖"), new Item("畜牧养殖"), new Item("更多")
    };

    private final int[] colors = {
            0x112ffa, 0x11a1f4, 0x13f23e, 0x11a533,
            0xaa2233, 0x1cc12f, 0x1cfa43, 0xdd3a33,
            0x19fac3, 0x1ace33, 0x1122ff, 0x113aed,
            0xa12233, 0x1fa2c3, 0xaced25, 0x6cf3d1,
            0xaac51d, 0x2deaa2, 0x66fdd2, 0x1ffdda,
            0xaa34d1, 0x2dfc33, 0x1a16d3, 0xfaff8a,
            0x1add33, 0x4aaaa3, 0xeee233, 0x2d3fe3,
    };

    private Context context;

    public MoreGridviewAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = null;
        if (view == null) {
            tv = new TextView(context);
            tv.setLayoutParams(new ViewGroup.LayoutParams(viewGroup.getWidth() / 4, viewGroup.getWidth() / 4));
            tv.setGravity(Gravity.CENTER);
            view = tv;
        } else {
            tv = (TextView) view;
        }

        tv.setBackgroundColor(Color.rgb((colors[i] & 0xff0000) >> 16, (colors[i] & 0x00ff00) >> 8, colors[i] & 0x0000ff));
        tv.setText(items[i].values);

        return view;
    }


    public static class Item implements AsymmetricItem {

        private String values;
        private int cs = 1, rs = 1;

        public Item(String values) {
            this.values = values;
        }

        public String getValues() {
            return values;
        }

        public int getCs() {
            return cs;
        }

        public Item setCs(int cs) {
            this.cs = cs;
            return this;
        }

        public int getRs() {
            return rs;
        }

        public Item setRs(int rs) {
            this.rs = rs;
            return this;
        }

        public Item setValues(String values) {
            this.values = values;
            return this;
        }

        @Override
        public int getColumnSpan() {
            return 1;
        }

        @Override
        public int getRowSpan() {
            return 1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(values);
        }

        public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel parcel) {
                Item item = new Item(parcel.readString());
//                item.setCs(2);
//                item.setRs(1);
                return item;
            }

            @Override
            public Item[] newArray(int i) {
                return new Item[i];
            }
        };
    }

}
