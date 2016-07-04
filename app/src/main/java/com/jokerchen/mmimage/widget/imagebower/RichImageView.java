package com.jokerchen.mmimage.widget.imagebower;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.loongjoy.androidframework.R;
import com.loongjoy.androidframework.http.imgloader.Md5FileNameGenerator;

public class RichImageView extends ImageView implements OnClickListener {

	private RichImageLoadListener listener;

	/**
	 * 播放GIF动画的关键类
	 */
	private Movie mMovie;

	/**
	 * 开始播放按钮图片
	 */
	private Bitmap mStartButton;

	/**
	 * 记录动画开始的时间
	 */
	private long mMovieStart;

	/**
	 * GIF图片的宽度
	 */
	private int mImageWidth;

	/**
	 * GIF图片的高度
	 */
	private int mImageHeight;

	/**
	 * 图片是否正在播放
	 */
	private boolean isPlaying;

	/**
	 * 是否允许自动播放
	 */
	private boolean isAutoPlay;

	public void setListener(RichImageLoadListener listener) {
		this.listener = listener;
	}

	/**
	 * PowerImageView构造函数。
	 * 
	 * @param context
	 */
	public RichImageView(Context context) {
		super(context);
	}

	/**
	 * PowerImageView构造函数。
	 * 
	 * @param context
	 */
	public RichImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * PowerImageView构造函数，在这里完成所有必要的初始化操作。
	 * 
	 * @param context
	 */
	public RichImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
		int resourceId = getResourceId(a, context, attrs);
		if (resourceId != 0) {
			// 当资源id不等于0时，就去获取该资源的流
			InputStream is = getResources().openRawResource(resourceId);
			// 使用Movie类对流进行解码
			mMovie = Movie.decodeStream(is);
			if (mMovie != null) {
				// 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
				isAutoPlay = true;
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				mImageWidth = bitmap.getWidth();
				mImageHeight = bitmap.getHeight();
				bitmap.recycle();
			}
		}
	}

	public void setImageSrc(final String uploadUrl, RichImageLoadListener listener) {
		this.listener = listener;
		if (!uploadUrl.isEmpty()) {
			final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/"
					+ getContext().getResources().getString(R.string.app_name) + "/cache/",
					new Md5FileNameGenerator().generate(uploadUrl));
			if (file.exists()) {
				try {
					InputStream is = new FileInputStream(file);
					mMovie = Movie.decodeStream(is);
					if (mMovie != null) {
						// 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
						isAutoPlay = true;
						if (is == null) {
							Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
							setImageBitmap(bitmap);
						} else {
							Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
							mImageWidth = bitmap.getWidth();
							mImageHeight = bitmap.getHeight();
							bitmap.recycle();
							invalidate();
						}

					} else {
						Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
						setImageBitmap(bitmap);
					}
					listener.onComplete(mMovie);
				} catch (FileNotFoundException e) {
					loadImg(uploadUrl);
				}
			} else {
				File mDir = file.getParentFile();
				mDir.mkdirs();
				loadImg(uploadUrl);

			}
		}
	}

	private void loadImg(final String uploadUrl) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				URL url;
				try {
					File file = new File(Environment.getExternalStorageDirectory().getPath() + "/"
							+ getContext().getResources().getString(R.string.app_name) + "/cache/",
							new Md5FileNameGenerator().generate(uploadUrl));
					url = new URL(uploadUrl);
					HttpURLConnection httpURLConnection;
					httpURLConnection = (HttpURLConnection) url.openConnection();
					InputStream is;
					is = httpURLConnection.getInputStream();
					FileOutputStream out;
					out = new FileOutputStream(file);
					byte[] b = new byte[1024];
					int c;
					while ((c = is.read(b)) != -1) {
						out.write(b, 0, c);
					}
					out.close();
					Message msg = new Message();
					msg.what = 0;
					msg.obj = uploadUrl;
					Handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();
	}
	
	
	Handler Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				File file = new File(Environment.getExternalStorageDirectory().getPath() + "/"
						+ getContext().getResources().getString(R.string.app_name) + "/cache/",
						new Md5FileNameGenerator().generate(msg.obj.toString()));
				InputStream is;
				try {
					is = new FileInputStream(file);
					mMovie = Movie.decodeStream(is);
					if (mMovie != null) {
						// 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
						isAutoPlay = true;
						Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
						mImageWidth = bitmap.getWidth();
						mImageHeight = bitmap.getHeight();
						bitmap.recycle();
						invalidate();
					} else {
						//一张普通图片,使用ImageView自带的setImageBitmap方法显示
						setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
						invalidate();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				listener.onComplete(mMovie);
				break;

			default:
				break;
			}

		};
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == getId()) {
			// 当用户点击图片时，开始播放GIF动画
			isPlaying = true;
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie == null) {
			// mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
			super.onDraw(canvas);
		} else {
			// mMovie不等于null，说明是张GIF图片
			if (isAutoPlay) {
				// 如果允许自动播放，就调用playMovie()方法播放GIF动画
				playMovie(canvas);
				invalidate();
			} else {
				// 不允许自动播放时，判断当前图片是否正在播放
				if (isPlaying) {
					// 正在播放就继续调用playMovie()方法，一直到动画播放结束为止
					if (playMovie(canvas)) {
						isPlaying = false;
					}
					invalidate();
				} else {
					// 还没开始播放就只绘制GIF图片的第一帧，并绘制一个开始按钮
					mMovie.setTime(0);
					mMovie.draw(canvas, 0, 0);
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mMovie != null) {
			// 如果是GIF图片则重写设定PowerImageView的大小
			setMeasuredDimension(mImageWidth, mImageHeight);
		}
	}

	/**
	 * 开始播放GIF动画，播放完成返回true，未完成返回false。
	 * 
	 * @param canvas
	 * @return 播放完成返回true，未完成返回false。
	 */
	private boolean playMovie(Canvas canvas) {
		long now = SystemClock.uptimeMillis();
		if (mMovieStart == 0) {
			mMovieStart = now;
		}
		int duration = mMovie.duration();
		if (duration == 0) {
			duration = 1000;
		}
		int relTime = (int) ((now - mMovieStart) % duration);
		mMovie.setTime(relTime);
		mMovie.draw(canvas, 0, 0);
		if ((now - mMovieStart) >= duration) {
			mMovieStart = 0;
			return true;
		}
		return false;
	}

	/**
	 * 通过Java反射，获取到src指定图片资源所对应的id。
	 * 
	 * @param a
	 * @param context
	 * @param attrs
	 * @return 返回布局文件中指定图片资源所对应的id，没有指定任何图片资源就返回0。
	 */
	private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {
		try {
			Field field = TypedArray.class.getDeclaredField("mValue");
			field.setAccessible(true);
			TypedValue typedValueObject = (TypedValue) field.get(a);
			return typedValueObject.resourceId;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (a != null) {
				a.recycle();
			}
		}
		return 0;
	}

	/**
	 * drawable2Bitmap
	 * 
	 * @author long_xia@loongjoy.com
	 *
	 * @param drawable
	 * @return
	 */
	public Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	public Drawable bitmap2Drawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	public interface RichImageLoadListener {
		void onComplete(Movie movie);
	}

}
