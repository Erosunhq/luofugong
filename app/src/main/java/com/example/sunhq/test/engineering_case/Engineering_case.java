package com.example.sunhq.test.engineering_case;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.sunhq.test.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunhq on 2018/3/30.
 */
public class Engineering_case extends AppCompatActivity implements View.OnClickListener {

    Button technical_parameter1;
    Button technical_parameter2;
    Button technical_parameter3;
    Button technical_parameter4;

    public static final int PARAMETER1 = 1;
    public static final int PARAMETER2 = 2;
    public static final int PARAMETER3 = 3;
    public static final int PARAMETER4 = 4;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case PARAMETER1:
                    technical_parameter1.setTextColor(getResources().getColorStateList(R.drawable.color2));
                    technical_parameter2.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter3.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter4.setTextColor(getResources().getColorStateList(R.drawable.color1));  // 这里几个包括下面的几个是为了设置选中后字体颜色的变化
                    imagePath = new GetImagePath("parameter1"); // 这里依据传入的字符串不同,方法获得不同的文件夹,展示不同的图片
                    PicList = imagePath.getImagePathFromSD();
                    gridView.setAdapter(new ImageListAdapter(Engineering_case.this, PicList));
                    break;
                case PARAMETER2:
                    technical_parameter2.setTextColor(getResources().getColorStateList(R.drawable.color2));
                    technical_parameter1.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter3.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter4.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    imagePath = new GetImagePath("parameter2"); // 这里依据传入的字符串不同,方法获得不同的文件夹,展示不同的图片
                    PicList = imagePath.getImagePathFromSD();
                    gridView.setAdapter(new ImageListAdapter(Engineering_case.this, PicList));
                    break;
                case PARAMETER3:
                    technical_parameter3.setTextColor(getResources().getColorStateList(R.drawable.color2));
                    technical_parameter2.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter1.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter4.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    imagePath = new GetImagePath("parameter3"); // 这里依据传入的字符串不同,方法获得不同的文件夹,展示不同的图片
                    PicList = imagePath.getImagePathFromSD();
                    gridView.setAdapter(new ImageListAdapter(Engineering_case.this, PicList));
                    break;
                case PARAMETER4:
                    technical_parameter4.setTextColor(getResources().getColorStateList(R.drawable.color2));
                    technical_parameter2.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter3.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    technical_parameter1.setTextColor(getResources().getColorStateList(R.drawable.color1));
                    imagePath = new GetImagePath("parameter4"); // 这里依据传入的字符串不同,方法获得不同的文件夹,展示不同的图片
                    PicList = imagePath.getImagePathFromSD();
                    gridView.setAdapter(new ImageListAdapter(Engineering_case.this, PicList));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    List<String> PicList = new ArrayList<String>();
    GetImagePath imagePath;

    private GridView gridView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //这里要配合修改Manifest里的内容theme属性，因为这个Activity继承的包不一样
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.engineering_case);

        Button engineer_case_back = (Button) findViewById(R.id.engineering_case_back);
        if (engineer_case_back != null) {
            engineer_case_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        ImageView imageView = (ImageView) findViewById(R.id.logo_home);
        //加载屏幕左边的logo
        int resourceId = R.mipmap.logomax_nomargin;
        Picasso.with(this)
                .load(resourceId)
                //.placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .tag("image")
                .into(imageView);

        gridView = (GridView) findViewById(R.id.gridView);

        // 默认展示选项
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = PARAMETER1;
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }).start();
        /*
        *  中间几个按钮的点击事件
        * */

        technical_parameter1 = (Button) findViewById(R.id.technical_parameter1);
        technical_parameter2 = (Button) findViewById(R.id.technical_parameter2);
        technical_parameter3 = (Button) findViewById(R.id.technical_parameter3);
        technical_parameter4 = (Button) findViewById(R.id.technical_parameter4);

        technical_parameter1.setOnClickListener(this);
        technical_parameter2.setOnClickListener(this);
        technical_parameter3.setOnClickListener(this);
        technical_parameter4.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("PicListArray", (ArrayList<String>) PicList);
                bundle.putStringArray("PicList", new String[]{PicList.get(position)});
                bundle.putInt("SelectedItem", position);
                Intent intent = new Intent(Engineering_case.this,ShowPic.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.technical_parameter1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message1 = new Message();
                        message1.what = PARAMETER1;
                        handler.sendMessage(message1); // 将Message对象发送出去
                    }
                }).start();
                break;

            case R.id.technical_parameter2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message2 = new Message();
                        message2.what = PARAMETER2;
                        handler.sendMessage(message2); // 将Message对象发送出去
                    }
                }).start();
                break;

            case R.id.technical_parameter3:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message3 = new Message();
                        message3.what = PARAMETER3;
                        handler.sendMessage(message3); // 将Message对象发送出去
                    }
                }).start();
                break;

            case R.id.technical_parameter4:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message4 = new Message();
                        message4.what = PARAMETER4;
                        handler.sendMessage(message4); // 将Message对象发送出去
                    }
                }).start();
                break;

            default:
                break;
        }
    }

    /**
         * 适配器
         */
        public class ImageListAdapter extends ArrayAdapter {
            private Context context;

            private List<String> PicList;

            public ImageListAdapter(Context context, List<String> PicList){
                super(context,R.layout.engineering_case_gridview_item,PicList);

                this.context = context;
                this.PicList = PicList;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView = View.inflate(context,R.layout.engineering_case_gridview_item,null);
                }
                ImageView imageView = (ImageView)convertView;
                if (TextUtils.isEmpty(PicList.get(position))){
                    Picasso
                            .with(context)
                            .cancelRequest(imageView);
                    imageView.setImageDrawable(null);
                }else {
                    //加载图片
                    Picasso
                            .with(context)
                            .load(new File(PicList.get(position)))
                            .placeholder(R.mipmap.loading_co)
                            .error(R.mipmap.error)
                            .config(Bitmap.Config.RGB_565)
                            .resize(305,305)
                            .noFade()
                            .into((ImageView) convertView);
                }
                return convertView;
            }

    }
    @Override
    protected void onDestroy() {

        //BitmapDrawable bitmapDrawable = (BitmapDrawable) gridView.getBackground();
        gridView.setBackgroundResource(0);
        //bitmapDrawable.setCallback(null);
        //bitmapDrawable.getBitmap().recycle();
        super.onDestroy();
    }


}
