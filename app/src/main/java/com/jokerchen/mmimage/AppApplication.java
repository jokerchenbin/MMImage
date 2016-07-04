/**
 * 
 */
package com.jokerchen.mmimage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.jokerchen.mmimage.model.Functions;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


/**
 * @author long_xia@loongjoy.com
 *
 */
public class AppApplication extends Application {

	public List<Activity> allActivityList = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		AppConfig.context = this;
		initCommonData();
		initImageLoader(getApplicationContext());

	}

	public void addActitivity(Activity activity) {
		allActivityList.add(activity);
	}

	public void finishAllActivity() {
		for (Activity activity : allActivityList) {
			if (null != activity) {
				activity.finish();
			}
		}
	}

	/**
	 * 获取设备信息
	 */
	public void initCommonData() {
		AppConfig.udId = Functions.getUdId(this);
		AppConfig.versionCode = Functions.getAppVersion(this);
		AppConfig.versionName = Functions.getapiVersion(this);
		AppConfig.device = Functions.getDevice();
		AppConfig.os = Functions.getOs();
		AppConfig.prfsName = "Appbuilder.xml";

		AppConfig.prefs = getSharedPreferences(AppConfig.prfsName, MODE_PRIVATE);
		AppConfig.appPrefs = getSharedPreferences("AppConfig.xml", MODE_PRIVATE);
	}


	public static void initImageLoader(Context context) {

		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.image_loading)
				.showImageOnFail(R.mipmap.image_error).cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 自定义缓存路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getString(R.string.app_name) + "/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height内存缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)// default 设置当前线程的优先级
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将url转换成MD5保存
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024) // 缓存的最大值
				.diskCacheSize(50 * 1024 * 1024) // sd卡(本地)缓存的最大值
				.tasksProcessingOrder(QueueProcessingType.LIFO).diskCache(new UnlimitedDiskCache(cacheDir))// default
																											// 可以自定义缓存路径
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).writeDebugLogs() // 开启log日志
				.defaultDisplayImageOptions(Functions.getSimpleImageOptions()).build();
		ImageLoader.getInstance().init(config);
	}

}
