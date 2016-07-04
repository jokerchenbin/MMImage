package com.jokerchen.mmimage.widget.imagebower;

import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.loongjoy.androidframework.R;
import com.loongjoy.androidframework.http.imgloader.ImageLoader;
import com.loongjoy.androidframework.ui.imagebower.RichImageView.RichImageLoadListener;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private RichImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private ImageLoader imageLoader;

	/**
	 * 初始化
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		// 图片下载器
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (RichImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setZoomable(false);
		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(200);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mImageView.setImageSrc(mImageUrl, new RichImageLoadListener() {

					@Override
					public void onComplete(Movie movie) {
						// TODO Auto-generated method stub
						progressBar.setVisibility(View.GONE);
						mAttacher.setZoomable(true,movie);
						if (null!=movie) {
							mImageView.getLayoutParams().width = movie.width();
							mImageView.getLayoutParams().height = movie.height();
						}
					}
				});

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
