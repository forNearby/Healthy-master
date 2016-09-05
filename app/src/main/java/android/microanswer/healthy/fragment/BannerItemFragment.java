package android.microanswer.healthy.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.tools.BaseTools;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;
//已经未使用
/**
 * 由 Micro 创建于 2016/6/30.
 */
@Deprecated
public class BannerItemFragment extends Fragment implements ImageLoadingListener {
    private View root;

    private ImageView img;
    private TextView tv_title;
    private TextView tv_smallrirle;
    private TextView tv_info;

    private InfoListItem infoListItem;

    private Bitmap blurbitmap;

    public InfoListItem getInfoListItem() {
        return infoListItem;
    }

    public void setInfoListItem(InfoListItem infoListItem) {
        this.infoListItem = infoListItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_banner_item, null);
        img = (ImageView) root.findViewById(R.id.viewpager_banner_img);
        tv_title = (TextView) root.findViewById(R.id.viewpager_banner_title);
        tv_smallrirle = (TextView) root.findViewById(R.id.viewpager_banner_desc);
        tv_info = (TextView) root.findViewById(R.id.viewpager_banner_info);

        setTitle(infoListItem.getTitle());
        setSmallTitle(infoListItem.getDescription());
        setInfo(new Date(infoListItem.getTime()).toLocaleString());
        setImageViewData("http://tnfs.tngou.net/image" + infoListItem.getImg());
        return root;
    }

    public void setImageViewData(String url) {
        ImageLoader.getInstance().displayImage(url, img, this);
    }

    public void setTitle(String title) {
//        Log.i("BannerItemFragment", "this.Ttv_title=" + this.tv_title + ",title=" + title);

        this.tv_title.setText(title);
    }

    public void setSmallTitle(String smalltitle) {
        this.tv_smallrirle.setText(smalltitle);
    }

    public void setInfo(String info) {
        this.tv_info.setText(info);
    }

    @Override
    public String toString() {
        return "BannerItemFragment{" +
                "root=" + root +
                ", img=" + img +
                ", tv_title=" + tv_title +
                ", tv_smallrirle=" + tv_smallrirle +
                ", tv_info=" + tv_info +
                '}';
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
        if (blurbitmap != null) {
            root.setBackgroundDrawable(new BitmapDrawable(blurbitmap));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BaseTools.doBlur(bitmap, 10);
                Message msg = Message.obtain();
                msg.obj = bitmap1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blurbitmap = (Bitmap) msg.obj;
            root.setBackgroundDrawable(new BitmapDrawable(blurbitmap));
        }
    };
}
