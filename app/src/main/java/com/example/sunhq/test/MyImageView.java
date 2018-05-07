package com.example.sunhq.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/7.
 */
public class MyImageView extends ImageView{

    public MyImageView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            System.out.println("MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }

}
