package com.example.sunhq.test.corporate_honor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sunhq.test.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunhq on 2018/3/30.
 */
public class Corporate_honor extends AppCompatActivity implements View.OnClickListener {

    Button corporate_honor_back;  //页面左下角返回按钮
    Button corporate_honor; // 右边两个按钮
    Button technical_parameter;

    GridView gridView;

    List<String> PicList = new ArrayList<String>();
    GetImagePath imagePath;

    ImageView image_tech = null;

    //异步消息处理
    public static final int CORPORATE_HONOR = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CORPORATE_HONOR:
                    technical_parameter.setBackgroundResource(R.mipmap.technical_parameter_unchosed);
                    corporate_honor.setBackgroundResource(R.mipmap.corporate_honor_chosed);
                    imagePath = new GetImagePath("image"); // 这里依据传入的字符串不同,方法获得不同的文件夹,展示不同的图片
                    PicList =  imagePath.getImagePathFromSD();
                    gridView.setAdapter(new ImageListAdapter(Corporate_honor.this,PicList));
                    gridView.setVisibility(View.VISIBLE);
                    image_tech.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //这里要配合修改Manifest里的内容theme属性，因为这个Activity继承的包不一样
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.corporate_honor);


        /*imageViewLogo = (ImageView) findViewById(R.id.logo_home);
       // String getPath_logo = Environment.getExternalStorageDirectory() + "/images/logomax_nomargin.png";
        //加载屏幕左边的logo
        Glide.with(this)
                .load(R.mipmap.logomax_nomargin)
                //.placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.error)
                .into(imageViewLogo);*/

        corporate_honor_back = (Button) findViewById(R.id.corporate_honor_back);
        corporate_honor_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // System.exit(0);
            }
        }); //返回到上一界面

        gridView = (GridView) findViewById(R.id.gridView);

        /*
         * 技术参数的按钮
         * */
        image_tech = (ImageView) findViewById(R.id.image_tech);
        technical_parameter = (Button) findViewById(R.id.technical_parameter);

        technical_parameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technical_parameter.setBackgroundResource(R.mipmap.technical_parameter_chosed);
                corporate_honor.setBackgroundResource(R.mipmap.corporate_honor_unchosed);
                gridView.setVisibility(View.INVISIBLE);
                image_tech.setVisibility(View.VISIBLE);
            }
        });

        /*
         * 企业荣誉的按钮
         * */
        corporate_honor = (Button) findViewById(R.id.corporate_honor);

        assert corporate_honor != null;
        corporate_honor.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"position =  "+position,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("PicListArray", (ArrayList<String>) PicList);
                bundle.putStringArray("PicList", new String[]{PicList.get(position)});
                bundle.putInt("SelectedItem", position);
                Intent intent = new Intent(Corporate_honor.this,ShowPic.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.corporate_honor:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = CORPORATE_HONOR;
                        handler.sendMessage(message); // 将Message对象发送出去
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
            super(context,R.layout.corporate_honor_item_picasso,PicList);

            this.context = context;
            this.PicList = PicList;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = View.inflate(context,R.layout.corporate_honor_item_picasso,null);
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
                        .resize(300,280)   //这几个Picasso都要待修改
                        .noFade()
                        .into((ImageView) convertView);
            }

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) gridView.getBackground();
        gridView.setBackgroundResource(0);
        bitmapDrawable.setCallback(null);
        try{
            bitmapDrawable.getBitmap().recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
