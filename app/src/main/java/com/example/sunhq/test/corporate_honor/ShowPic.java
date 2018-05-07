package com.example.sunhq.test.corporate_honor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sunhq.test.R;
import com.example.sunhq.test.home_display.sliding_effect.ZoomOutPageTransformer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Sunhq on 2018/3/29.
 */
public class ShowPic extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView currentPage;
    ZoomImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.corporate_honor_vp);
        Button back = (Button) findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        //接受传过来的信息
        Bundle bundle = getIntent().getExtras();

        final ArrayList<String> imagePathListArray = bundle.getStringArrayList("PicListArray");
        final ImageView[] mImageViews = new ImageView[imagePathListArray.size()];
        int currentItem = bundle.getInt("SelectedItem",0);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
         /*
        * 显示页码的TextView
        * */
        currentPage = (TextView) findViewById(R.id.currentPage);
        /*
        * 设置页码
        * 当前页码  /  总共页码
        * */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                int currentPosition = position;
                currentPage.setText(currentPosition+1 +" / "+ mImageViews.length);
                currentPage.setTextColor(0xffc1966c);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /***********************************************************************************************/
        //设置图片切换时的动画效果(下面是两种效果,任选一种)
        //mViewPager.setPageTransformer(true,new DepthPageTransformer());
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        /****************************************************************/

       // mViewPager.setOffscreenPageLimit(3);      //效果不明显,不知是否有用
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                imageView = new ZoomImageView(getApplicationContext());
                /*
                * 控制ViewPager的数量最多为5个
                * */
                int i = position % 5;
                Picasso.with(ShowPic.this)
                        .load(new File(imagePathListArray.get(position)))
                        .fit()
                        .error(R.mipmap.error)
                        .centerInside()
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);

                /*imageView.setImageURI(Uri.parse(imagePathListArray.get(position)));*/ //Picasso与之二选一,很明显,Picasso优于你
                container.addView(imageView);
                mImageViews[i] = imageView;
                onDetachedFromWindow();
                return imageView;
            }
            protected void onDetachedFromWindow() {
                imageView.setImageDrawable(null);
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {

                /*Log.d("Corporate_honor", "destroyItem******************************************************:"+object);

                container.removeView((View) object);

                ImageView mImageView=mImageViews[position];
                if (mImageView != null) {
                    mImageView = null;
                }
               // 将多余的页面销毁.减少内在的占用.*/
                imageView = (ZoomImageView) object;
                if (imageView == null){
                    return;
                }
                Glide.clear(imageView);   //核心,解决OOM
                container.removeView(imageView);
            }
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mImageViews.length;
            }
        });


           mViewPager.setCurrentItem(currentItem); //初始化  刚开始选中的哪张图片
    }

    @Override
    protected void onDestroy() {

        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        System.gc();

        super.onDestroy();
    }


}
