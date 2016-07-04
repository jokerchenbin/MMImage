/**
 * @Title: BackGroundImage.java
 * @Package com.loongjoy.androidjj.widget.image
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author deng_long_fei(longfei_deng@loongjoy.com) 
* @date 2015年12月16日 下午12:04:27
 * @version V1.0
 */

package com.jokerchen.mmimage.widget.imagebower;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ClassName: BackGroundImage
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年12月16日 下午12:04:27
 *
 */
public class BackGroundImage extends View {

    private int mPosition;

    private float mDegree;

    private List<Drawable> mDrawableLists;

    private int mPrePosition = 0;

    private Drawable mNext;


    public void setmDrawableLists(List<Drawable> mDrawableLists) {

        this.mDrawableLists = mDrawableLists;

        mNext = mDrawableLists.get(1);//设置下一个背景图片的drawable

    }

 

    public void setmPosition(int mPosition) {

        this.mPosition = mPosition;

    }


    public void setmDegree(float mDegree) {

        this.mDegree = mDegree;

    }


    public BackGroundImage(Context context) {

        super(context);

        //setWillNotDraw(false);

    }


    public BackGroundImage(Context context, AttributeSet attrs) {

        super(context, attrs);

        //setWillNotDraw(false);

    }


    public BackGroundImage(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        //setWillNotDraw(false);

    }


    @Override

    protected void onDraw(Canvas canvas) {

        Log.i("111", "onDraw");

        if (null == mDrawableLists) {

            return;

        }

        int alpha1 = (int) (255 - (mDegree * 255));

        Drawable fore = mDrawableLists.get(mPosition);

        fore.setBounds(0, 0, getWidth(), getHeight());

        mNext.setBounds(0, 0, getWidth(), getHeight());

        if (mPrePosition != mPosition) {//边界判断

            if (mPosition != mDrawableLists.size() - 1) {

                mNext = mDrawableLists.get(mPosition + 1);

            } else {

                mNext = mDrawableLists.get(mPosition);

            }

        }

        fore.setAlpha(alpha1);//淡出

        mNext.setAlpha(255);

        mNext.draw(canvas);

        fore.draw(canvas);

        mPrePosition = mPosition;

        super.onDraw(canvas);

    }
}
