package com.example.administrator.robot;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.robot.SampleApplication.utils.Directory;
import com.example.administrator.robot.SampleApplication.utils.FileUtils;
import com.example.administrator.robot.VuforiaSamples.app.ImageTargets.ImageTargets;
import com.example.administrator.robot.qiniuultra.Upload;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
//import com.iflytek.speech.util.FucUtil;
//import com.iflytek.speech.util.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.media.AudioManager;
import com.example.administrator.robot.database.db;

import static com.example.administrator.robot.Socket.MyApplication.isServiceWork;
import com.example.administrator.robot.Socket.SocketService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.example.administrator.robot.Tcpclient.Tcpclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {
    //静态方法实现MainActivity的实体
    private static MainActivity mainActivity;
    private StringBuffer sbuff;
    private ListView answer;
    private MyAdapter myAdapter;
    private ArrayList<TalkBean> mlist = new ArrayList<TalkBean>();
    private String[] mAnswer = new String[]{"要约么？", "生活处处都有美女啦", "抱歉，我拒绝回答"};
    private int[] mPics = new int[]{R.drawable.a, R.drawable.b};
    private String message;
    private String answers;
    private String askContent;
    MediaPlayer mediaPlayer,nextmediaPlayer;
    SurfaceView surfaceView;
    BarrageView bv;
    BarrageViewEnglish bv_english;
    public static Resources res;
    private boolean Gdebug=false;
    public boolean Gplaystate=true;
    public static  int soundId,tishiid,intervalid;
    public static  SoundPool soundPool;
    public static String language="zh_cn";//en_us
    public static boolean isfirstrun=true;
    public PcmToWavUtil pcmToWavUtil ;
    public static int welcomesoundid,warningid,pleasewait;
    public static String tulingid;
    public ImageView iv2;
    public static String geshi="mp4";
    //     public  String sit = Environment.getExternalStorageDirectory() + File.separator + "sit.mp4";
    public  String sit ="/sdcard/"+ "sit."+geshi;

    // public String sit = "android.resource://" + getPackageName() + "/" + R.raw.sit;

    //    public  String standup_walk = Environment.getExternalStorageDirectory() + File.separator + "standup_walk.mp4";
//    public  String standup_walk = Environment.getExternalStorageDirectory() + File.separator + "standup_walk."+geshi;
    public  String standup_walk = "/sdcard/" + "standup_walk."+geshi;

    //public String standup_walk = "android.resource://" + getPackageName() + "/" + R.raw.standup_walk;

    //    public  String standby = Environment.getExternalStorageDirectory() + File.separator + "standby.mp4";
    public  String standby = "/sdcard/" + "standby."+geshi;

    // public String standby = "android.resource://" + getPackageName() + "/" + R.raw.standby;

    //    public  String talk = Environment.getExternalStorageDirectory() + File.separator + "talk.mp4";
    public  String talk = "/sdcard/" + "talk."+geshi;

    // public String talk = "android.resource://" + getPackageName() + "/" + R.raw.talk;

    //    public  String back_walk = Environment.getExternalStorageDirectory() + File.separator + "back_walk.mp4";
    public  String back_walk = "/sdcard/" + "back_walk."+geshi;
    //  public String back_walk = "android.resource://" + getPackageName() + "/" + R.raw.back_walk;

    public int interval=30000;
    Handler handler_interval,handler_yincang;
    public String sjs_state="sit";
    //视频分类  sit、standup_walk、standby、talk、back_walk
    //  int position;
    private static long SPLASH_MILLIS = 450;
    //子线程是否运行的状态1 表示需要运行
    private int thread_status=1;
    public Button btnch;

    private ImageView img_down;
    AnimationSet animationSet;
    private SpeechRecognizer mAsr;
    // 语音听写对象
    private SpeechRecognizer mIat;
    private boolean mTranslateEnable = false;
    private String resultType = "json";
    private StringBuffer buffer = new StringBuffer();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;


    Handler han = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                //   executeStream();
            }
        }
    };
    private boolean cyclic = false;//音频流识别是否循环调用

   // private StringBuffer buffer = new StringBuffer();
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    // private ImageView Hintbtn;
    //  private MainActivity mainActivity;



    //socket相关
//    Socket Socket = null;//Socket
//    boolean buttontitle = true;//定义一个逻辑变量，用于判断连接服务器按钮状态
//    boolean RD = false;//用于控制读数据线程是否执行

//    java.io.OutputStream OutputStream = null;//定义数据输出流，用于发送数据
//    java.io.InputStream InputStream = null;//定义数据输入流，用于接收数据

    //用线程创建Socket连接
//    class Connect_Thread extends Thread{
//        public void run(){
//            //定义一个变量用于储存ip
//            InetAddress ipAddress;
//            try {
//                //判断socket的状态，防止重复执行
//                if (Socket == null) {
//                    //如果socket为空则执行
//                    //获取输入的IP地址
//                    ipAddress = InetAddress.getByName("192.168.123.2");
//                    //获取输入的端口
//                    int port = Integer.valueOf("6800");
//
//                    //新建一个socket
//                    Socket = new Socket(ipAddress, port);
//                    //获取socket的输入流和输出流
//                    InputStream = Socket.getInputStream();
//                    OutputStream = Socket.getOutputStream();
//
//                    //新建一个线程读数据
//                    ThreadReadData t1 = new ThreadReadData();
//                    t1.start();
//                }
//            } catch (Exception e) {
//                //如果有错误则在这里返回
//                e.printStackTrace();
//            }
//        }
//    }


    //用线程执行读取服务器发来的数据
//    class ThreadReadData extends Thread{
//        public void run() {
//            //定义一个变量用于储存服务器发来的数据
//            String textdata;
//            //根据RD变量的值判断是否执行读数据
//            while (RD) {
//                try {
//                    //定义一个字节集，存放输入的数据，缓存区大小为2048字节
//                    final byte[] ReadBuffer = new byte[2048];
//                    //用于存放数据量
//                    final int ReadBufferLengh;
//
//                    //从输入流获取服务器发来的数据和数据宽度
//                    //ReadBuffer为参考变量，在这里会改变为数据
//                    //输入流的返回值是服务器发来的数据宽度
//                    ReadBufferLengh = InputStream.read(ReadBuffer);
//
//                    //验证数据宽度，如果为-1则已经断开了连接
//                    if (ReadBufferLengh == -1) {
//                        //重新归位到初始状态
//                        RD = false;
//                        Socket.close();
//                        Socket = null;
//                        buttontitle = true;
//                        //   Button.setText("连 接 服 务 器");
//                    } else {
//                        //如果有数据正常返回则进行处理显示
//
//                        /*
//                            这个地方有个很大的坑，让我搞了不少的时间
//                            我用其他语言写的Web服务器程序，默认编码是gb2312
//                            AS的默认编码是utf-8
//                            在获取服务器发来的数据的时候，程序已经对这段gb2312的数据进行编码...
//                            至于编码是什么就不知道了
//                            我研究了很长时间，怎么转码也不对，越转越乱
//                            最后测试出来是gb2312编码已经被转码了，我就先恢复gb2312编码
//                            然后转成程序不会乱码的utf-8
//                            如果目标服务器编码是utf8的话就不用转了
//                        */
//
//                        //先恢复成GB2312编码
//                        textdata = new String(ReadBuffer,0,ReadBufferLengh,"UTF-8");//原始编码数据
//                       // Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");
//                        Log.d("aaa",textdata);
//                        compose(textdata);
//                        //转为UTF-8编码后显示在编辑框中
//                        //    edittotext.setText(new String(textdata.getBytes(),"UTF-8"));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    //socket 结束





    protected void hideBottomUIMenu() {

        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);  } else if (Build.VERSION.SDK_INT >= 19) {
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }}
    //将getmainActivity public化供别的类调用，虽然这样做不太好！！
    public static MainActivity getMainActivity(){
        return mainActivity;
    }
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = this.getResources();
        // 根据分辨率调整布局
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        int screenWidth =dMetrics.widthPixels;
        int screenHeight =dMetrics.heightPixels;



        if(screenWidth<1000){
            setContentView(R.layout.activity_main_small);
        }else{
             setContentView(R.layout.activity_main_small);
            //setContentView(R.layout.activity_main);
        }
        //   setContentView(R.layout.activity_main);
        hideBottomUIMenu();
        this.getString(R.string.tulingid);
        if(isfirstrun==true){
            copayAssetsToSdCard();
            isfirstrun=false;
        }

        soundPool=new  SoundPool(100,AudioManager.STREAM_MUSIC,0);//构建对象
        soundId=soundPool.load(this,R.raw.takephoto,1);//加载资源，得到soundId
        welcomesoundid=soundPool.load(this,R.raw.welcome,1);
        tishiid=soundPool.load(this,R.raw.tishi,1);
        intervalid=soundPool.load(this,R.raw.interval,1);
        warningid=soundPool.load(this,R.raw.warning,1);
        pleasewait=soundPool.load(this,R.raw.pleasewait,1);
        iv2=(ImageView)this.findViewById(R.id.imageView2);
        //   soundId=welcomesoundid;
        Intent intent = new Intent(MainActivity.this, AutoStartService.class);
        startService(intent);

        sharedPreferences = getSharedPreferences("autostart", ContextWrapper.MODE_PRIVATE);

        bv=(BarrageView) findViewById(R.id.bv_text);
        bv_english=(BarrageViewEnglish)findViewById(R.id.bv_text_english);
        btnch=(Button) findViewById(R.id.chlanuage);
        img_down=(ImageView)findViewById(R.id.img_down);

        //普通手机
        //if(screenWidth<1000){
//            LinearLayout line = (LinearLayout) findViewById(R.id.main3btn);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(null,null);//(LinearLayout.LayoutParams) line.getLayoutParams();
//            params.setMargins(0, 190, 0, 0);
//            line.setLayoutParams(params);

        //设置LinearLayout的高宽
        //   LinearLayout layout = (LinearLayout)findViewById(R.id.main3btn);
        //     LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 150);
        //      //设置linearLayout 的起点
        // params.leftMargin = 100

//                params.topMargin=190;
//                layout.setLayoutParams( params );

        //}
        //   System.out.println("width===" + screenWidth);
        //  System.out.println("width===" + screenHeight);
//提示按钮特效
        //  img_down = (ImageView)findViewById(R.id.img_down);
        animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.down_anim);




//给拍照+录音按钮绑定按下特效

        Button btn_recode=(Button)findViewById(R.id.record);
        btn_recode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setTop(15);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setTop(0);
                }
                return  false;
            }
        });
        Button btn_takephoto=(Button)findViewById(R.id.button_auto);
        btn_takephoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setTop(15);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setTop(0);
                }
                return false;
            }
        });
        //给拍照按钮绑定事件
        btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sjs_state=="talk"){
                    soundPool.play(warningid, 1,1,1,0,1);//播放，得到StreamId

                    return;
                }
                mediaPlayer.stop();
                mediaPlayer.reset();
                //结束子线程
                thread_status=0;
                Intent intent =new Intent(MainActivity.this,TakePhoto.class);
                startActivity(intent);

            }
        });


        //MainActivity供别的类调用准备
        mainActivity=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        answer = (ListView) findViewById(R.id.answer);
//        设置全屏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bitmap bmzhezhao=readBitMap(this,R.drawable.sjs360j);
        iv2.setImageBitmap(bmzhezhao);

        handler_yincang = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 9:
                        iv2.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        break;
                }
            }
        };

        //
        handler_interval = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if(sjs_state=="sit"){
                            showTip("sit");
                        }else{
                            sjs_state="back_walk";
                            //播放转身往回走，并且关闭弹幕
                            play(back_walk);
                            bv.setVisibility(View.GONE);
                            bv_english.setVisibility(View.GONE);
                            btnch.setVisibility(View.GONE);
                            showTip("back_walk");
                        }
                        break;
                    case 1:
                        break;
                }
            }
        };

//

        mediaPlayer = new MediaPlayer();
        nextmediaPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        //设置SurfaceView自己不管理的缓冲区
        //    surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//++++++++++++++++++++++++++++++++++++++++++++
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        RelativeLayout layout = (RelativeLayout) inflater.inflate(
//                R.layout.splash_screen, null, false);
//
//        addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//
//
//        final Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {

//                 //Intent intent = new Intent(ActivitySplashScreen.this, AboutScreen.class);
//                // intent.putExtra("ABOUT_TEXT_TITLE", "Image Target");
//                //  intent.putExtra("ACTIVITY_TO_LAUNCH", "app.ImageTargets.ImageTargets");
//                // intent.putExtra("ABOUT_TEXT", "ImageTargets/IT_about.html");
//                //Intent intent = new Intent(ActivitySplashScreen.this, AboutScreen.class);
//               // Intent intent = new Intent();
//             //   intent.setClassName("com.example.administrator.robot.MainActivity","com.example.administrator.robot.VuforiaSamples.app.ImageTargets.ImageTargets");
//

//
//
//            }
//
//        }, SPLASH_MILLIS);

//++++++++++++++++++++++++++++++++++++++++++++++
        myAdapter = new MyAdapter();
        answer.setAdapter(myAdapter);
        // 将“12345678”替换成您申请的 APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "="+this.getString(R.string.kedaid));
        //   play(sit);
        interval=30000;
//        compose("请按下屏幕右侧的拍照按钮，然后您有10秒钟的时间调整最佳的拍照动作，当拍照结束后可以扫描屏幕上的二维码下载照片");
        compose("你好，欢迎光临总督府");
        //   compose("请稍等，我马上就来");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        thread_status=1;
        new Thread(new mythread1()).start();



        //
    }



    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    public    void copayAssetsToSdCard() {
        //FileUtils.makeDirs(path);
        // String path=Directory.DOWN_LODE_APF_PATH.toString();
        //  soundPool.play(warningid, 1,1,1,0,1);//播放，得到StreamId

        String path=Environment.getExternalStorageDirectory() + File.separator;
        // FileUtils.copyAssetToSDCard(this.getAssets(), "talk.mp4", path + "talk.mp4");
        // FileUtils.copyAssetToSDCard(this.getAssets(), "standby.mp4", path + "standby.mp4");
        // FileUtils.copyAssetToSDCard(this.getAssets(), "back_walk.mp4", path + "back_walk.mp4");
        // FileUtils.copyAssetToSDCard(this.getAssets(), "standup_walk.mp4", path + "standup_walk.mp4");
        //FileUtils.copyAssetToSDCard(this.getAssets(), "sit.mp4", path + "sit.mp4");

        FileUtils.copyAssetToSDCard(this.getAssets(), "talk."+geshi, path + "talk."+geshi);
        FileUtils.copyAssetToSDCard(this.getAssets(), "standby."+geshi, path + "standby."+geshi);
        FileUtils.copyAssetToSDCard(this.getAssets(), "back_walk."+geshi, path + "back_walk."+geshi);
        FileUtils.copyAssetToSDCard(this.getAssets(), "standup_walk."+geshi, path + "standup_walk."+geshi);
        FileUtils.copyAssetToSDCard(this.getAssets(), "sit."+geshi, path + "sit."+geshi);
    }

    public class mythread_yincang implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                    Thread.sleep(1000);
                    Message msg1 = new Message();
                    msg1.what=9;
                handler_yincang.sendMessage(msg1);
                 //   iv2.setVisibility(View.INVISIBLE);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    //多线程

    public class mythread1 implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                while(thread_status==1){
                    //   i++;
                    Thread.sleep(1000);
                    if(interval==0){

                        Message msg1 = new Message();
                        msg1.what=0;
                        handler_interval.sendMessage(msg1);
                        interval=30000;
                    }else {
                        interval = interval - 1000;
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    //视频播放函数
    public void play(String path){
        if(Gplaystate==true){

            try {

                if(sjs_state=="standby"){
                    iv2.setVisibility(View.VISIBLE);
                   new Thread(new mythread_yincang()).start();
                    //return;
                }else if (sjs_state=="talk"){
                    iv2.setVisibility(View.VISIBLE);
                   // mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaPlayer.setDataSource(talk);
//                    mediaPlayer.setDisplay(surfaceView.getHolder());
//                    mediaPlayer.prepare();
//                    mediaPlayer.seekTo(0);
//                    mediaPlayer.start();
                    new Thread(new mythread_yincang()).start();
                 //   return;
                }

                //mediaPlayer.stop();
               // mediaPlayer.pause();//新增
                // mediaPlayer.release();
                mediaPlayer.reset();

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //设置需要播放的视频
                mediaPlayer.setDataSource(path);
                //  mediaPlayer.setDataSource( MainActivity.mainActivity.getResources().getAssets().open("111.obj"));
                //把视频画面输出到SurfaceView
                mediaPlayer.setDisplay(surfaceView.getHolder());

                mediaPlayer.prepare();
                // mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        //如果设计师状态是起身走过来，那么执行standby循环
                        if(sjs_state=="standup_walk"){
                            try {
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(standby);
                                mediaPlayer.prepare();
                                showTip("standby");
                                //打开弹幕
                                bv.setVisibility(View.VISIBLE);
                                //   打开切换语言按钮
                                btnch.setVisibility(View.VISIBLE);
                                btnch.setText("ENGLISH");
                                //设置语言中文
                                language="zh_cn";
                                img_down.setVisibility(View.INVISIBLE);
                                img_down.setVisibility(View.GONE);
                                sjs_state="standby";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        if(sjs_state=="talk"){
//                            mediaPlayer.stop();
//                            mediaPlayer.reset();
//                            try {
//                                mediaPlayer.setDataSource(standby);
//                                mediaPlayer.prepare();
//                                sjs_state="standby";
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                        //如果设计师状态师转身回去，那么执行坐下循环
                        if(sjs_state=="back_walk"){
                            try {
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(sit);
                                mediaPlayer.prepare();
                                showTip("sit");
                                sjs_state="sit";
                                //显示提示按钮
                                img_down.startAnimation(animationSet);
                                img_down.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //  mediaPlayer.stop();
                        mediaPlayer.seekTo(0);
                        //mediaPlayer.wait(1.1);
                        mediaPlayer.start();
                    }
                });

                //播放
                mediaPlayer.seekTo(0);
                mediaPlayer.start();


//                if(sjs_state=="standby"){
//                 //   iv2.setVisibility(View.VISIBLE);
//                  //  return;
//                }else{
//                      iv2.setVisibility(View.INVISIBLE);
//                    //  return;
//                }

                 // Toast.makeText(this, "开始播放11", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
    //屏蔽下拉菜单
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        // TODO Auto-generated method stub
//        super.onWindowFocusChanged(hasFocus);
//        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
//        try {
//            Object service = getSystemService("statusbar");
//            Class<?> statusbarManager = Class
//                    .forName("android.app.StatusBarManager");
//            Method test = statusbarManager.getMethod("collapse");
//            test.invoke(service);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    private void disableStatusBars() {
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, 0x00010000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        System.out.println("hasfocus--->>>" + hasFocus);
//        super.onWindowFocusChanged(hasFocus);
//        try {
//            Object service = getSystemService("statusbar");
//            Class<?> statusbarManager = Class
//                    .forName("android.app.StatusBarManager");
//            Method test = statusbarManager.getMethod("collapse");
//            test.invoke(service);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //开始语音识别
//    public void startListen(View v){
//        Intent intent =new Intent(MainActivity.this,ImageTargets.class);
//       // intent.setClassName("com.example.administrator.robot.VuforiaSamples","com.example.administrator.robot.VuforiaSamples.app.ImageTargets.ImageTargets");
//        startActivity(intent);
//    }
    public void autostart(View v){
        //设置自启动
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("AutoStartService", true);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, AutoStartService.class);
        startService(intent);

    }
    public void startTakePic(View v){
        mediaPlayer.stop();
        mediaPlayer.reset();
        Intent intent = new Intent(MainActivity.this, ImageTargets.class);
        startActivity(intent);
    }
    //录音按钮事件
    public void startListen(View v) {
        //重置interval
        interval=30000;

        //如果当前视频path1 不是站立循环视频或者说话循环视频，说明设计师正在坐着，那就播放起身走过来的视频然后退出
        if(sjs_state=="sit"){
            sjs_state="standup_walk";
            //  Gplaystate=false;
//            compose("你好，欢迎光临迎宾馆");
            // Gplaystate=true;

            soundPool.play(welcomesoundid, 1,1,1,0,1);//播放，得到StreamId

            play(standup_walk);
            showTip("standup_walk");
            return;
        } else if(sjs_state=="standup_walk"||sjs_state=="back_walk"){
            //  compose("我这就来");
            soundPool.play(pleasewait, 1,1,1,0,1);//播放，得到StreamId

            return;
        }
        //maketext决定Toast显示内容
        Toast toastCenter = Toast.makeText(getApplicationContext(),"请大声说出您的问题",Toast.LENGTH_LONG);

        //setGravity决定Toast显示位置
        toastCenter.setGravity(Gravity.CENTER,0,0);

        //调用show使得toast得以显示
        toastCenter.show();
        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);

//设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
        mIat.setParameter( SpeechConstant.SUBJECT, null );
//设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//此处engineType为“cloud”
        mIat.setParameter( SpeechConstant.ENGINE_TYPE, mEngineType );
//设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
// 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
//取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
//设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
//自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"1");

//开始识别，并设置监听器
        mIat.startListening(mRecognizerListener);
// 初始化识别对象
//        mAsr = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);
//        //反之不需要做判断直接下一步
//        //1.创建RecognizerDialog对象
//        RecognizerDialog mDialog = new RecognizerDialog(this, null);
//        //2.设置accent、language等参数
//        mDialog.setParameter(SpeechConstant.LANGUAGE, language);
//        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
//        mDialog.setTitle("123");
//        //若要将UI控件用于语义理解， 必须添加以下参数设置， 设置之后onResult回调返回将是语义理解
//        //结果
//
//        sbuff = new StringBuffer();
//        //3.设置回调接口
//        mDialog.setListener(new RecognizerDialogListener() {
//            //最终的识别结果
//            @Override
//            public void onResult(RecognizerResult recognizerResult, boolean b) {
//                String result = recognizerResult.getResultString();
//                String spreak_word = pareData(result);
//                sbuff.append(spreak_word+"  ");
//                if (b) {
//                    askContent = sbuff.toString();//得到最终结果
//
//                    String cutstr=askContent.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
//                    if(cutstr.equals("管理员82861903")){
//                      compose("确认");
//                        Intent intent= new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                       Uri content_url = Uri.parse("http://www.cnblogs.com");
//                        intent.setData(content_url);
//                        startActivity(intent);
//                         finish();
//                    }
//                   else if(cutstr.equals("管理员82861903设置")){
//                        Intent intent= new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                        startActivity(intent);
//                        finish();
//                    }
//                    showTip(cutstr);
//                    Log.e("HLS","用户："+askContent);
//                    TalkBean askBean = new TalkBean(askContent, -1, true);//初始化提问对象
//                    mlist.add(askBean);
//                    //刷新 listview
//                    answers="这个问题我们机器要开个会，等商量出来再告诉你";
//                    //调用图灵
//                    new GetMessage().start();
//                }
//            }
//            @Override
//            public void onError(SpeechError speechError) {
//
//            }
//        });
//        //4.显示dialog，接收语音输入
//        mDialog.show();
    }

    private String pareData(String _json) {
        //Gson解析
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(_json, VoiceBean.class);
        StringBuffer sb = new StringBuffer();
        ArrayList<VoiceBean.WS> ws = voiceBean.ws;
        for (VoiceBean.WS w : ws) {
            String word = w.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public TalkBean getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {

                convertView = View.inflate(getApplicationContext(), R.layout.list_iten, null);

                holder = new ViewHolder();
                holder.tvAsk = (TextView) convertView.findViewById(R.id.tv_ask);
                holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
                holder.llAnswer = (LinearLayout) convertView.findViewById(R.id.ll_answer);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.tv_pic);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            TalkBean item = getItem(position);
            if (item.isAsk) {
                //提问
                holder.tvAsk.setVisibility(View.VISIBLE);
                holder.llAnswer.setVisibility(View.GONE);
                holder.tvAsk.setText(item.content);
            } else {

                holder.tvAsk.setVisibility(View.GONE);
                holder.tvAnswer.setVisibility(View.VISIBLE);
                holder.tvAnswer.setText(item.content);
                //图片
                if (item.imageId > 0) {
                    holder.ivPic.setVisibility(View.VISIBLE);
                    holder.ivPic.setImageResource(item.imageId);
                } else {
                    holder.ivPic.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }

    abstract class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            //   path1 = Environment.getExternalStorageDirectory() + File.separator + "talk.mp4";

            sjs_state="talk";
            //开始说话前 停止子线程，这样做的目的是防止过长的语音过程中，设计师还没说完话就往回走了
            thread_status=0;
            play(talk);
            showTip(" 开始播放 ");
            //  findViewById(R.id.button_auto).setEnabled(false);


        }

        @Override
        public void onSpeakPaused() {
            showTip(" 暂停播放 ");
        }

        @Override
        public void onSpeakResumed() {
            showTip(" 继续播放 ");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            // showTip("progress......");
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //play(path1);
                //path1 = Environment.getExternalStorageDirectory() + File.separator + "talk.mp4";
                sjs_state="standby";
                //设计师说完话了，再次启动子线程倒计时
                thread_status=1;
                interval=30000;
                new Thread(new mythread1()).start();

                play(standby);
                showTip("播放完成66666 ");
                final String path = Environment.getExternalStorageDirectory() + File.separator + "iflytek.pcm";

                //按原路径把音频文件后缀改一下;
                final String outpath = path.replace(".pcm", ".wav");
                pcmToWavUtil = new PcmToWavUtil();
                pcmToWavUtil.pcmToWav(path, outpath);
                //  findViewById(R.id.button_auto).setEnabled(true);
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }
    }

    static class ViewHolder {
        public TextView tvAsk;
        public TextView tvAnswer;
        public ImageView ivPic;
        public LinearLayout llAnswer;

    }

    //修改语言
    public void change_language(View v){
        if(language=="zh_cn"){

            btnch.setText("切换中文");
            language="en_us";
            bv.setVisibility(View.GONE);
            bv_english.setVisibility(View.VISIBLE);
        }else{
            btnch.setText("ENGLISH");
            language="zh_cn";
            bv.setVisibility(View.VISIBLE);
            bv_english.setVisibility(View.GONE);
        }

        //  BarrageView.initData();
        //  bv.refreshDrawableState();
//        bv.setVisibility(View.VISIBLE);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    new Upload().upload(getBaseContext(),"/sdcard/DCIM/Camera/pic2.jpeg");
//                    f =new File("/sdcard/DCIM/Camera/pic2.jpeg");
//                    upload(f);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
    //showtipp
    private void showTip(String data){
        if(Gdebug==true){
            Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
        }
    }

    //语音合成
    public  void  compose(String speak) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置） ，保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");


        //3.开始合成
        //Toast.makeText(this, "开始说话", Toast.LENGTH_LONG).show();
        mTts.startSpeaking(speak, new MySynthesizerListener() {
            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });

        //合成监听器
        SynthesizerListener mSynListener = new SynthesizerListener() {
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error) {
                if(error ==null){
                    //showTip("播放完成");
                }

            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在
            // 文本中结束位置，info为附加信息。
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            }
            //开始播放
            public void onSpeakBegin() {
            }
            //暂停播放
            public void onSpeakPaused() {
            }
            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文
            //本中结束位置.
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
            }
            //恢复播放回调接口
            public void onSpeakResumed() {
            }
            //会话事件回调接口
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            }
        };
        // mSynListener.onCompleted(null);
    }

    public class GetMessage extends Thread{
        public void run(){
            /*super.start();*/
            Message msg=new Message();
            try{
                msg.what=1;
                message=new GetHttpMessage().testGetRequest(askContent);
                  Log.e("黄柳淞",message);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            handler.sendMessage(msg);
        }
    }
    final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                int imageId=-1;
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT);
                answers=message;
                TalkBean answerBean=new TalkBean(answers,imageId,false);
                mlist.add(answerBean);
                myAdapter.notifyDataSetChanged();
                compose(answers);

                //play(path1);
                //Log.e("HLS", "助手："+answer);
            }

        }

    };


    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            //Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("内容：===", results.getResultString());
            String str = "[{'ws':'hahah''}]";
            try {
                JSONObject json = new JSONObject( results.getResultString());
             //   Log.d("fuck",json.toString(1));
               // Log.d("fuck2",json.getString("ws"));
                JSONArray jsonarray1 = new JSONArray( json.getString("ws"));
                jsonarray1=json.getJSONArray("ws");
                Log.d("fuck2-1",String.valueOf(jsonarray1.length()));
                askContent="";
                for (int i = 0; i < jsonarray1.length(); i++) {
                    JSONObject jobj=jsonarray1.getJSONObject(i);
                    JSONArray w=jobj.getJSONArray("cw");
                    JSONObject tmpstr= new JSONObject(w.get(0).toString());
                    String ww=tmpstr.getString("w");
                 //   Log.d("fuck3:"+String.valueOf(i),ww);
                    askContent+=ww;
//                    JSONObject value = jsonarray1.getJSONObject(0);
//                    JSONArray valuearray=value.getJSONArray("cw");
//                    JSONObject valueobj=valuearray.getJSONObject(i);
                    //      Log.d("fuck3:"+String.valueOf(i),valueobj.getString("w"));
                }
                askContent=askContent.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
                    if(askContent.length() <= 0){

                    }else{
                        Log.d("fuck3-1",askContent);
                        TalkBean askBean = new TalkBean(askContent.toString(), -1, true);//初始化提问对象
                    //     mlist.add(askBean);
                        //刷新 listview
                        answers="这个问题我们机器要开个会，等商量出来再告诉你";
                        new GetMessage().start();
                    }
                //

//                Log.d("fuck3",String.valueOf(json.getString("ws").length()));
//
//                JSONArray json2 = new JSONArray( json.getString("ws"));
//                Log.d("fuck4-1", String.valueOf(json2.length()));
//                //for  循环可以
//                Log.d("fuck4-2", String.valueOf(json2.get(0).toString()));
//                JSONObject json3 = new JSONObject(  String.valueOf(json2.get(0).toString()));
//                Log.d("fuck4-3", json3.getString("cw").toString());
//                JSONArray json4 = new JSONArray( json3.getString("cw").toString());
//                Log.d("fuck4-4",    json4.get(0).toString());
//                JSONObject json5 = new JSONObject( json4.get(0).toString());
//                Log.d("fuck4-5",    json5.getString("w"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultType.equals("json")) {
                if( mTranslateEnable ){
                    //        printTransResult( reults );

                }else{
                    //      printResult(results);
                }
            }else if(resultType.equals("plain")) {
                buffer.append(results.getResultString());
                //  mResultText.setText(buffer.toString());
                //    mResultText.setSelection(mResultText.length());
            }

            if (isLast & cyclic) {
                // TODO 最后的结果
                Message message = Message.obtain();
                message.what = 0x001;
                han.sendMessageDelayed(message,100);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d("9999", "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

}
