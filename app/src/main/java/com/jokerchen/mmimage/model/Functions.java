/**
 * 
 */
package com.jokerchen.mmimage.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.jokerchen.mmimage.AppConfig;
import com.jokerchen.mmimage.R;
import com.jokerchen.mmimage.widget.imagebower.ImagePagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


/**
 * @author long_xia@loongjoy.com
 *
 */
public class Functions {

	/**
	 * get screen width
	 */
	public static void getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		AppConfig.screenWidth = dm.widthPixels;
		AppConfig.screenHeight = dm.heightPixels;
	}

	/**
	 * get devieceId
	 */
	public static String getUdId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return null;
		}
		return tm.getDeviceId();
	}

	/**
	 * get version Code
	 */
	public static int getAppVersion(Context context) {
		int versionCode = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
			// if (versionName == null || versionName.length() <= 0) {
			// return "";
			// }
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		// AppConfig.versionCode = versionCode;
		return versionCode;
	}

	/**
	 * get version Name
	 */
	public static String getapiVersion(Context context) {
		String versionName = "0.0.0";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * get mobile model
	 */
	public static String getDevice() {
		return Build.MODEL;
	}

	/**
	 * get mobile system version
	 */
	public static String getOs() {
		return "android" + Build.VERSION.RELEASE;
	}

	/**
	 * check the string is null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (null == str || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check mobile network station
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 图片预览
	 * 
	 * @author long_xia@loongjoy.com
	 *
	 * @param activity
	 * @param position
	 * @param urls
	 */
	public static void imageBrower(Activity activity, int position, ArrayList<String> urls) {
		Intent intent = new Intent(activity, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		activity.startActivity(intent);
	}

	/**
	 * 图片配置圆角矩形或者圆形或原图（可加边框）
	 * 
	 * @author loong_xia@loongjoy.com
	 * @param round
	 *            圆角度数
	 *            边框颜色
	 * @return DisplayImageOptions
	 */
	public static DisplayImageOptions getImageOptions(int round, Integer stockColor) {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.image_loading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.mipmap.image_error) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.mipmap.image_empty) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
						// 设置下载的图片是否缓存在SD卡中
				/*.displayer(
						mode == MODE.Circle ? new CircleBitmapDisplayer(stockColor) : new RoundedBitmapDisplayer(round,
								stockColor)) // 设置成圆角图片*/
				.build(); // 构建完成
		return options;
	}


	public static DisplayImageOptions getSimpleImageOptions() {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.image_loading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.mipmap.image_error) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.mipmap.image_empty) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
				.build(); // 构建完成
		return options;
	}

	/**
	 * 图片合成
	 * 
	 * @Title createBitmap
	 * @author loong_xia@loongjoy.com
	 * @param src
	 *            源图片（底层）
	 * @param watermark
	 *            覆盖图片
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h + wh + 5, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		int left = w / 2 - ww / 2;
		cv.drawBitmap(watermark, left, h + 5, null);//

		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 小米手机设置状态栏字体颜色
	 * 
	 * @Title setMiuiStatusBarDarkMode
	 * @author loong_xia@loongjoy.com
	 * @param activity
	 * @param darkmode
	 * @return
	 * @return boolean
	 */
	public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
		Class<? extends Window> clazz = activity.getWindow().getClass();
		try {
			int darkModeFlag = 0;
			Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
			extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 魅族手机设置状态栏字体颜色
	 * 
	 * @Title setMeizuStatusBarDarkIcon
	 * @author loong_xia@loongjoy.com
	 * @param activity
	 * @param dark
	 *            true 黑色 false base
	 * @return
	 * @return boolean
	 */
	public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
		boolean result = false;
		if (activity != null) {
			try {
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				activity.getWindow().setAttributes(lp);
				result = true;
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * 支付相关 alipay 作用 TODO(描述)
	 * 
	 * @Title: alipay
	 * @author bin_chen@loongjoy.com
	 *//*
	public static void Alipay(final String orderInfo, final Activity activity, final AlipayListener listener) {
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(orderInfo, true);
				listener.callBack(result);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	public static void WXPay(Activity activity, JSONObject data) {
		*//*
		 * createWXAPI参数：context，appID
		 *//*
		// Logger.getInstance().e("weixinInfo", info);
		AppConfig.WXAppId = data.optString("appid");
		IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WXAppId);
		PayReq request = new PayReq();
		request.appId = AppConfig.WXAppId;
		request.partnerId = data.optString("partnerid");
		request.prepayId = data.optString("prepayid");
		request.packageValue = data.optString("package");
		request.nonceStr = data.optString("noncestr");
		request.timeStamp = data.optString("timestamp");
		request.sign = data.optString("sign");
		api.registerApp(AppConfig.WXAppId);
		boolean flag = api.sendReq(request);
		Logger.getInstance().e("WXPay", flag);

	}*/

	public static <T> T parseObject(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public static <T> List<T> parseArray(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
		}
		return list;
	}

	public static String formatString(Object str) {

		return String.valueOf(str);

	}
	
	/**
	 *  检测时候是否安装微信
	  * isWeixinAvilible 作用
	  * TODO(描述)
	  * @Title: isWeixinAvilible
	  * @Description: TODO
	  * @author bin_chen@loongjoy.com
	 */
	public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
}
