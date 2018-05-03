package com.example.sunhq.test;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sunhq.test.corporate_honor.Corporate_honor;
import com.example.sunhq.test.engineering_case.Engineering_case;
import com.example.sunhq.test.home_display.Home_display;
import com.example.sunhq.test.home_display.menu.*;
import com.example.sunhq.test.updatePictures.FTP;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    //String getPath_logo = Environment.getExternalStorageDirectory() + "/images/logomax_nomargin.png";
    ImageView imageView;

    Button updatePictures;
    private static final String TAG = "MainActivity";

    public static final String FTP_CONNECT_SUCCESSS = "ftp 连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp 连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp 断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp 上文件不存在";


    public static final String FTP_DOWN_LOADING = "ftp 文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp 文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp 文件下载失败";


    // 定义文件的存取路径(分类存放在不同的文件夹下面)(这里所有的目录到时候根据要求修改,相应的GeyImagePath那个函数里面也要修改)
    public static final String HOME_SHOW = "/FTP/images/";  //家装展示图片路径
    public static final String CORPORATE_HONOR = "/FTP/image/";  //企业荣誉
    public static final String ENGINEERING_CASE = "/FTP/project/";  //工程案列

    String[] serverFolder = new String[]{HOME_SHOW,CORPORATE_HONOR,ENGINEERING_CASE+"parameter1/"
            ,ENGINEERING_CASE+"parameter2/",ENGINEERING_CASE+"parameter3/",ENGINEERING_CASE+"parameter4/"};

    //本地存放的文件夹
    String localFolder = "/mnt/sdcard/Picture/";
    // **********************************************

    // 创建线程池对内存进行优化处理
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //这里要配合修改Manifest里的内容theme属性，因为这个Activity继承的包不一样
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View view = View.inflate(this, R.layout.activity_main,null);
        setContentView(view);


        // 更新后台图片
        initView();
        //最大十条线程同时执行
        executorService = Executors.newFixedThreadPool(10);


        LeakCanary.install(this.getApplication());

        imageView = (ImageView) findViewById(R.id.logo_home);
        int resourceId = R.mipmap.logomax_nomargin;
        Picasso.with(this)
                .load(resourceId)
                .error(R.mipmap.error)
                //.resize(com.example.sunhq.test.DisplayUtils.dip2px(this,500),com.example.sunhq.test.DisplayUtils.dip2px(this,500))
                .fit()
                .tag("image")
                .into(imageView);

        Button close_btn = (Button)findViewById(R.id.close_btn);
        if (close_btn != null) {
            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0); //完全退出本程序，finish()只是返回上一页
                }
            });
        }

        Button home_decoration_display = (Button) findViewById(R.id.home_decoration_display);
        if (home_decoration_display != null) {
            home_decoration_display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Home_display.class);
                    startActivity(intent);
                }
            });
        }

        Button corporate_honor = (Button) findViewById(R.id.corporate_honor);
        if (corporate_honor != null) {
            corporate_honor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Corporate_honor.class);
                    startActivity(intent);
                }
            });
        }

        Button engineering_case = (Button) findViewById(R.id.engineering_case);
        if (engineering_case != null) {
            engineering_case.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Engineering_case.class);
                    startActivity(intent);
                }
            });
        }

    }
    // 更新后台图片
    private void initView() {
        updatePictures = (Button) findViewById(R.id.updatePictures);
        updatePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 下载
                        try {
                            for (int j = 0; j < serverFolder.length; j++) {
                                switch (j){
                                    case 0:
                                        localFolder = "/mnt/sdcard/Picture/"+"images/";
                                        break;
                                    case 1:
                                        localFolder = "/mnt/sdcard/Picture/"+"image/";
                                        break;
                                    case 2:
                                        localFolder = "/mnt/sdcard/Picture/"+"project/parameter1/";
                                        break;
                                    case 3:
                                        localFolder = "/mnt/sdcard/Picture/"+"project/parameter2/";
                                        break;
                                    case 4:
                                        localFolder = "/mnt/sdcard/Picture/"+"project/parameter3/";
                                        break;
                                    case 5:
                                        localFolder = "/mnt/sdcard/Picture/"+"project/parameter4/";
                                        break;
                                }
                                String[] serverPath = new FTP().JudgeFile(serverFolder[j]);  // 服务端的文件路径
                                for (int i = 0; i < serverPath.length; i++) {
                                    //单文件下载 (服务器上文件的路径, 存放的本地路径, 下载到本地后要保存的文件名称(可以跟源文件名不一样), 下载监听器)
                                    new FTP().downloadSingleFile(serverFolder[j] + serverPath[i], localFolder, serverPath[i], new FTP.DownLoadProgressListener() {
                                        @Override
                                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                                            Log.d(TAG, currentStep);
                                            if (currentStep.equals(MainActivity.FTP_DOWN_SUCCESS)) {
                                                Log.d(TAG, "-----下载--成功");
                                            } else if (currentStep.equals(MainActivity.FTP_DOWN_LOADING)) {
                                                Log.d(TAG, "-----下载---" + downProcess + "%");

                                            }
                                            downProcess = 0;   //对不同的文件,进度重新置0
                                        }
                                    });
                                    if (i == serverPath.length) {
                                        Log.d(TAG, "该文件夹下图片全部下载完成!!!");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
