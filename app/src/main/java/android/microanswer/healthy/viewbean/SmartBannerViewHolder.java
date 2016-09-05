package android.microanswer.healthy.viewbean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.tools.BaseTools;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;
import java.util.List;


/**
 * 由 Micro 创建于 2016/7/14.
 */

public class SmartBannerViewHolder extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener, OnItemClickListener {
    private ConvenientBanner convenientBanner;
    private TextView bannertag;
    private BannerItemCreator bannerItemCreator;
    private OnSmartBannerItemClickListener onSmartBannerItemClickListener;

    public SmartBannerViewHolder(View itemView) {
        super(itemView);
        convenientBanner = (ConvenientBanner) itemView.findViewById(R.id.healthy_banner);
        convenientBanner.setOnItemClickListener(this);
        convenientBanner.setOnPageChangeListener(this);
        bannertag = (TextView) itemView.findViewById(R.id.healthy_banner_tag);
        bannerItemCreator = new BannerItemCreator();
    }

    public void stopTurning() {
        convenientBanner.stopTurning();
    }

    public void setData(List<InfoListItem> datas) {
        if (datas == null) {
            return;
        }

        if (this.data == datas) {//相同数据不用重新设置banner
            return;
        }
        Log.i("从数据库获取到的健康咨询","Banner设置数据:"+datas.toString());

        this.data = datas;
        convenientBanner.setPages(bannerItemCreator, datas).setPageIndicator(new int[]{R.drawable.dot2, R.drawable.dot1})
                //以下使用了一个匿名内部类作为Viewpager的切换动画
                .setPageTransformer(new ViewPager.PageTransformer() {
                    private static final float MIN_SCALE = 0.95f;
                    private static final float MIN_ALPHA = 0.9f;

                    @Override
                    public void transformPage(View view, float position) {
                        int pageWidth = view.getWidth();
                        int pageHeight = view.getHeight();


                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            view.setAlpha(0);

                        } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
                        { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                            if (position < 0) {
                                view.setTranslationX(horzMargin - vertMargin / 2);
                            } else {
                                view.setTranslationX(-horzMargin + vertMargin / 2);
                            }

                            // Scale the page down (between MIN_SCALE and 1)
                            view.setScaleX(scaleFactor);
                            view.setScaleY(scaleFactor);

                            // Fade the page relative to its size.
                            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                        } else { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            view.setAlpha(0);
                        }
                    }
                }).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).setCanLoop(true);
        if (!convenientBanner.isTurning()) {
            convenientBanner.startTurning(4000);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private String[] tags = {"健康快讯", "企业要闻", "医疗新闻", "生活贴士", "药品新闻", "食品新闻", "社会热点", "疾病快讯"};

    private List<InfoListItem> data;

    @Override
    public void onPageSelected(int position) {
        if (data != null) {
            bannertag.setText(tags[data.get(position).getInfoclass()]);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int position) {
        if (onSmartBannerItemClickListener != null) {
            onSmartBannerItemClickListener.onItemClick(data.get(position));
        }
    }


    class BannerItemCreator implements CBViewHolderCreator<BannerItem> {

        @Override
        public BannerItem createHolder() {
            return new BannerItem();
        }
    }

    class BannerItem implements Holder<InfoListItem>, ImageLoadingListener {
        private View v;
        private TextView title;
        private TextView smalltitle;
        private TextView desc;
        private ImageView iv;

        @Override
        public View createView(Context context) {
            v = View.inflate(context, R.layout.fragment_banner_item, null);
            title = (TextView) v.findViewById(R.id.viewpager_banner_title);
            smalltitle = (TextView) v.findViewById(R.id.viewpager_banner_desc);
            desc = (TextView) v.findViewById(R.id.viewpager_banner_info);
            iv = (ImageView) v.findViewById(R.id.viewpager_banner_img);
            return v;
        }

        @Override
        public void UpdateUI(Context context, int position, InfoListItem data) {
            title.setText(data.getTitle());
            smalltitle.setText(data.getDescription());
            desc.setText("发布时间:" + new Date(data.getTime()).toLocaleString());
            ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + data.getImg(), iv, this);
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        private Bitmap blurbitmap;

        @Override
        public void onLoadingComplete(String imageUri, View view, final Bitmap bitmap) {
            if (blurbitmap != null) {
                v.setBackgroundDrawable(new BitmapDrawable(blurbitmap));
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Bitmap bitmap1 = null;
                    try {
                        bitmap1 = BaseTools.doBlur2(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (bitmap1 == null) {
                        return;
                    }

                    Message msg = Message.obtain();
                    msg.obj = bitmap1;
                    handler.sendMessage(msg);
                }
            }).start();
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                blurbitmap = (Bitmap) msg.obj;
                v.setBackgroundDrawable(new BitmapDrawable(blurbitmap));
            }
        };
    }

    public OnSmartBannerItemClickListener getOnSmartBannerItemClickListener() {
        return onSmartBannerItemClickListener;
    }

    public void setOnSmartBannerItemClickListener(OnSmartBannerItemClickListener onSmartBannerItemClickListener) {
        this.onSmartBannerItemClickListener = onSmartBannerItemClickListener;
    }

    public interface OnSmartBannerItemClickListener {
        void onItemClick(InfoListItem item);
    }
}
