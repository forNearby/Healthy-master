package android.microanswer.healthy.application;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.microanswer.healthy.R;
import android.microanswer.healthy.service.HealthyService;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.tauth.Tencent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

/**
 * 应用程序类
 * Created by Micro on 2016/6/21.
 */

public class Healthy extends Application {

    public static final String IMAGE_URL = "http://tnfs.tngou.net/image";

    public static final String APP_ID_FOR_TENCENT = "1105506561";
    public static final String APP_KEY_FOR_TENCENT = "Qy0ai0WXvvfd5AcE";
    public static ImageLoaderConfiguration ilc = null;
    public static Tencent tencent;//腾讯sdk服务

//    public static Handler WORKHANDLER=null;//这个handler用于整个程序
    @Override
    public void onCreate() {
        super.onCreate();
        if (ilc == null) {
            ilc = new ImageLoaderConfiguration.Builder(this)
                    .threadPoolSize(5)
                    .threadPriority(Thread.NORM_PRIORITY)
                    .memoryCacheExtraOptions(480, 800)
                    .diskCacheSize(500 * 1024 * 1024)
                    .diskCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(this)))
                    .diskCacheFileNameGenerator(new FileNameGenerator() {
                        @Override
                        public String generate(String imageUri) {
                            return imageUri + ".jpg";
                        }
                    })
                    .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .showImageOnFail(R.mipmap.ic_img_faill)
                            .displayer(new FadeInBitmapDisplayer(200))
                            .showImageOnLoading(R.mipmap.loading)
                            .build())
                    .diskCacheFileCount(10000)
                    .build();
        }
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ilc);
        }

        if (tencent == null) {
            tencent = Tencent.createInstance(APP_ID_FOR_TENCENT, this);
        }
        Intent intent = new Intent(this, HealthyService.class);
        startService(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public static void noUseMethod(Object... obj){

    }


}
