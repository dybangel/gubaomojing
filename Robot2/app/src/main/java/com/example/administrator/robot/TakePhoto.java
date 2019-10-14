package com.example.administrator.robot;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Element;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.example.administrator.robot.qiniuultra.Upload;
import com.example.administrator.robot.service.FileUploadObserver;
import com.example.administrator.robot.service.HttpRequest;
import com.example.administrator.robot.service.ProgressRequestBody;
import com.example.administrator.robot.storage.model.FileInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;


public class TakePhoto extends AppCompatActivity {
    public  String mScreenshotPath  = Environment.getExternalStorageDirectory() + File.separator ;
    private ArrayList<Element> mElements = new ArrayList<Element>();
    private int mElementNumber = 0;
    private Paint mPaint = new Paint();
    public  String path1 = Environment.getExternalStorageDirectory() + File.separator + "1.mp4";
    public MainActivity mainActivity;
    private Upload ud;
    private  SuperSurfaceView surfaceView;
    private JsonBean jsonBean = null;

    private SurfaceView sv;
    private SurfaceHolder holder1 = null;
    private Camera cam = null;
    private boolean previewRunning = true;
    private RelativeLayout r1;
    public Button b2,b3,b4,b5;
    public ImageView ivpic1,ivpic2,ivpic3,sjsiv;
    private File f;
    private ImageView iv1,zhifubao;
    private LinearLayout LLpassword;
    private Button bp0,bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8,bp9,bp10,bpenter;
    private EditText et1,et2,et3,et4;
    private int passwordcount=0;
    private String passwordstring="";
    CircleProgressBar mCircleProgress;
    Handler handler;
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takephoto);
        b5=(Button) findViewById(R.id.button5);
        b4=(Button) findViewById(R.id.button4);
        b3=(Button) findViewById(R.id.button3);
     //   b2=(Button)findViewById(R.id.button2);
       ivpic1=(ImageView)findViewById(R.id.pic1);
       ivpic2=(ImageView)findViewById(R.id.pic2);
       ivpic3=(ImageView)findViewById(R.id.pic3);
        sjsiv=(ImageView)findViewById(R.id.iv2);
       LLpassword=(LinearLayout) findViewById(R.id.password);
        bp0=(Button)findViewById(R.id.pbutton0);
       bp1=(Button)findViewById(R.id.pbutton1);
        bp2=(Button)findViewById(R.id.pbutton2);
        bp3=(Button)findViewById(R.id.pbutton3);
        bp4=(Button)findViewById(R.id.pbutton4);
        bp5=(Button)findViewById(R.id.pbutton5);
        bp6=(Button)findViewById(R.id.pbutton6);
        bp7=(Button)findViewById(R.id.pbutton7);
        bp8=(Button)findViewById(R.id.pbutton8);
        bp9=(Button)findViewById(R.id.pbutton9);
        bpenter=(Button)findViewById(R.id.pbuttonenter);
        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable((R.drawable.sjs1));
        //以最省资源的方式读取资源文件，加载到iv2上
        Bitmap bmsjs1=readBitMap(this,R.drawable.sjs1);
        //初始化设计师第一个姿势的大贴图
        sjsiv.setImageBitmap(bmsjs1);
        //给小图标增加设计师图片
        Bitmap bmsjs1m=readBitMap(this,R.mipmap.sjs1m);
        Bitmap bmsjs2m=readBitMap(this,R.mipmap.sjs1m);
        Bitmap bmsjs3m=readBitMap(this,R.mipmap.sjs1m);
        ivpic1.setImageBitmap(bmsjs1m);
        ivpic2.setImageBitmap(bmsjs2m);
        ivpic3.setImageBitmap(bmsjs3m);

      //  sjsiv.setImageBitmap(  bd.getBitmap());

        MainActivity.soundPool.play(MainActivity.tishiid, 1,1,1,0,1);//播放，得到StreamId

       // b2.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View v) {
              //  onCreate(null);
//                Intent intent =new Intent(null,TakePhoto.class);
//                startActivity(intent);
//                cam.startPreview();
//                sv = (SurfaceView) findViewById(R.id.sv1);
//                holder1 = sv.getHolder();
//
//                holder1.addCallback(new MySurfaceViewCallback());

     //       }
       // });
        //密码键盘按钮事件
        bp0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("0");
            }
        });
        bp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("1");
            }
        });
        bp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("2");
            }
        });
        bp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("3");
            }
        });
        bp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("4");
            }
        });
        bp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("5");
            }
        });
        bp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("6");
            }
        });
        bp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("7");
            }
        });
        bp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("8");
            }
        });
        bp9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword("9");
            }
        });
        bpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordstring.equals("8359")||passwordstring.equals("6743")||passwordstring.equals("2190")||passwordstring.equals("7721")||passwordstring.equals("6400")
                        ||passwordstring.equals("9912")||passwordstring.equals("4053")||passwordstring.equals("1873"))
                {
                    //显示图片下载二维码
                    ImageView iv1 =(ImageView)findViewById(R.id.iv1);
                    zhifubao=(ImageView)findViewById(R.id.ivzhifubao);
                    iv1.setVisibility(View.VISIBLE);
                    zhifubao.setVisibility(View.VISIBLE);
                   // findViewById(R.id.button4).setVisibility(View.GONE);
                    b4.setVisibility(View.GONE);
                    LLpassword.setVisibility(View.GONE);
                }else{
                    et1.setText("*");
                    et2.setText("*");
                    et3.setText("*");
                    et4.setText("*");
                    passwordcount=0;
                    passwordstring="";
                }
            }
        });

        ivpic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sjsiv.setImageDrawable(getResources().getDrawable((R.drawable.sjs1)));

            }
        });
        ivpic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sjsiv.setImageDrawable(getResources().getDrawable((R.drawable.sjs2)));
            }
        });
        ivpic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sjsiv.setImageDrawable(getResources().getDrawable((R.drawable.sjs3)));
            }
        });

        handler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                      //截图前隐藏所有按钮  findViewById(R.id.button3).setVisibility(View.GONE);
                        b3.setVisibility(View.GONE);
                        b4.setVisibility(View.GONE);
                        b5.setVisibility(View.GONE);
                        findViewById(R.id.play_circle_progress).setVisibility(View.GONE);
                        break;
                    case 2:
                        b3.setVisibility(View.GONE);
                        b4.setVisibility(View.GONE);
                        b5.setVisibility(View.GONE);

                        findViewById(R.id.play_circle_progress).setVisibility(View.GONE);
                          //      SoundPool soundPool=new  SoundPool(100, AudioManager.STREAM_MUSIC,0);//构建对象
        //int soundId=soundPool.load(null,R.raw.takephoto,1);//加载资源，得到soundId
//        //int streamId=
                MainActivity.soundPool.play(MainActivity.soundId, 1,1,1,0,1);//播放，得到StreamId
                     //   cam.stopPreview();
                        takePhoto(sv);
                        //播放拍照声音
//                        MediaPlayer mp = new MediaPlayer();
//                        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.takephoto);
//                        try {
//                            mp.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
//                                    file.getLength());
//                            mp.prepare();
//                            file.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        mp.setVolume(0.5f, 0.5f);
//                        mp.setLooping(false);
//                        mp.start();
                        // new Thread(new mythread1()).start();



                        break;
                    case 3:
                     //   MainActivity.soundPool.play(MainActivity.warningid, 1,1,1,0,1);//播放，得到StreamId

                        break;
                }
            }
        };
        mCircleProgress = (CircleProgressBar) findViewById(R.id.play_circle_progress);

        mCircleProgress.setBgColor(R.color.theme_button_deep_bg_trans_70)
                .setRingColor(R.color.trans_blank_bg_70).setProgressColor(android.R.color.white)
                .setTextColor(android.R.color.white).setTextSize(dip2px2(this, 18))
                .setTextSize2(dip2px2(this, 14))
                .setTextFormat(CircleProgressBar.TextFormat.CURRENT_WITH_TOTAL_PROGRESS);

        if (null != mCircleProgress) {
      //      mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_PROGRESS);
        //    mCircleProgress.setMaxProgress(500);
       //     mCircleProgress.update(100, true);
        }
      //  mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_PER_SECOND_PROGRESS);
      //  mCircleProgress.setMaxProgress(500);

        mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_WITH_TOTAL_PROGRESS);
        mCircleProgress.setMaxProgress(10);


        mCircleProgress.update(0);
      //  mData.getCacheInfo().setCircleProgressPausePosition(2);

        r1=(RelativeLayout) findViewById(R.id.r1);
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//                                                          @Override
//                                                          public void onClick(View v) {
//                                                          //    cutpic2();
//                                                              //Bitmap bitmap = surfaceView.getBitmap();
//                                                            //saveScreenshot();
//
//                                                            //  r1.addView(surfaceView);
//
//                                                          }
//                                                      }
//
//        );
       b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TakePhoto.this,MainActivity.class);
                startActivity(intent);
                //  MainActivity.getMainActivity().play(path1);
                finish();

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示密码框
                LLpassword.setVisibility(View.VISIBLE);

            }
        });
        //拍照按钮

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止连续点击拍照按钮
                b3.setEnabled(false);
                ivpic1.setVisibility(View.GONE);
                ivpic2.setVisibility(View.GONE);
                ivpic3.setVisibility(View.GONE);
                //显示进度条
                findViewById(R.id.play_circle_progress).setVisibility(View.VISIBLE);
                b3.setVisibility(View.GONE);
                b3.setWidth(0);

                //b4.setEnabled(false);
                b4.setVisibility(View.GONE);
                b4.setWidth(0);

                //b5.setEnabled(false);
                b5.setVisibility(View.GONE);
                b5.setWidth(0);
                new Thread(new mythread()).start();

            }
        });

        //默认读取摄像头显示到sv上
        sv = (SurfaceView) findViewById(R.id.sv1);
        holder1 = sv.getHolder();

        holder1.addCallback(new MySurfaceViewCallback());

        //        设置全屏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




    }
//    private byte[] InputStreamToByte(InputStream is) throws IOException {
//        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
//        int ch;
//        while ((ch = is.read()) != -1) {
//            bytestream.write(ch);
//        }
//        byte imgdata[] = bytestream.toByteArray();
//        bytestream.close();
//        return imgdata;
//    }


//    /**
//     *
//     * 指定路径文件是否存在
//     */
//    private boolean ensureSDCardAccess() {
//        File file = new File(mScreenshotPath);
//        if (file.exists()) {
//            return true;
//        } else if (file.mkdirs()) {
//            return true;
//        }
//        return false;
//    }
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

    public void enterpassword(String s){
    if(passwordcount==0){
        passwordcount=1;
        et1.setText(s);
        passwordstring=passwordstring+s;
    }else if(passwordcount==1){
        passwordcount=2;
        et2.setText(s);
        passwordstring=passwordstring+s;
    }else if(passwordcount==2){
        passwordcount=3;
        et3.setText(s);
        passwordstring=passwordstring+s;
    }else if(passwordcount==3){
        passwordcount=4;
        et4.setText(s);
        passwordstring=passwordstring+s;
    }else if(passwordcount==4){

    }
    }
//多线程播放声音
public class mythread1 implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        //int i=1;




    }
}

    //多线程倒计时

    class mythread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            Message msg = new Message();
            int i=1;
            try {
                while(i<10){
                    i++;
                    Thread.sleep(1000);
                    mCircleProgress.update(i);


                }

             //   msg.what = 1;
           //     msg.arg1 = time;
                //也可以发送对象
                //msg.obj = obj;
               // handler.sendMessage(msg);
//takephoto code

                msg.what=2;
                handler.sendMessage(msg);
                //隐藏自身按钮
//                findViewById(R.id.button3).setVisibility(View.GONE);


                //takephoto code end


            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            intent = new Intent(getApplicationContext(),
//                    NewPagerActivity.class);
//            startActivity(intent);//启动方法 不属于UI
        }
    }
    class MyThd extends Thread{

        @Override
        public void run() {
            super.run();
            long stime=System.currentTimeMillis();
            //此处是逻辑计算加载资源时间 是否6S


            long etime=System.currentTimeMillis();
            long runtime=etime-stime;
            try {
                if (6000>runtime) {

                    Thread.sleep(6000-runtime);//一般定义为常量
                }
                else Thread.sleep(runtime);//一般定义为常量
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

//多线程结束

    /**
     * 将dip/dp值转换为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px2(Activity activity, float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }
    public static Bitmap capture(Activity activity){
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp=activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    private void cutpic(){
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        //Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        Toast.makeText(this,"正在为你截取照片。。。。。",Toast.LENGTH_SHORT).show();;
        Bitmap bitmap=capture(this);
        if (bitmap != null) {
            try {
                final String picname="pic"+ (int)(Math.random() * 100)+".jpeg";
                Toast.makeText(this,"正在为您保存照片。。。。。",Toast.LENGTH_SHORT).show();;
                saveBitmap(bitmap,picname);
                Toast.makeText(this,"正在为您生成照片二维码。。。。。",Toast.LENGTH_SHORT).show();;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      //  try {
                           // new Upload().upload(getBaseContext(),"/sdcard/DCIM/Camera/"+picname);

                             f =new File("/sdcard/DCIM/Camera/"+picname);//
                          FtpUtil ftpUtil = new FtpUtil("47.240.57.240", 21, "anonymous", "anonymous");
                        if(ftpUtil.open()){
                            //ftpUtil.donwLoad("/中.txt", "E:/ftp2/中文.txt");
                            ftpUtil.upLoading("/pub/"+picname, "/sdcard/DCIM/Camera/"+picname);

                        }
                        //sm.ms的上传方式
                       //      upload(f);

                            // f.delete();
                    //    } catch (IOException e) {
                        //    e.printStackTrace();
                     //   }
                    }
                }).start();

           //     b4.setEnabled(true);


          //      b5.setEnabled(true);
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap("http://47.240.57.240/pub/pub/"+picname, 480, 480);
                iv1=(ImageView) findViewById(R.id.iv1);
                iv1.setImageBitmap(mBitmap);
                //显示下载按钮
                b4.setVisibility(View.VISIBLE);
                //显示返回按钮
                b5.setVisibility(View.VISIBLE);
                Log.d("a7888", "存储完成");
            } catch (Exception e){
            }
        }

    }

    private void deletedirfile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deletedirfile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

//图片压缩，传入原始图片对象，传入要生成的文件名称
    private void saveBitmap(Bitmap bitmap,String bitName) throws IOException
    {
        File ffdir= new File("/sdcard/DCIM/Camera");
        deletedirfile(ffdir);
        //  String path1 = Environment.getExternalStorageDirectory() + File.separator;
        File file = new File("/sdcard/DCIM/Camera/"+bitName);
//        if(file.exists()){
//            file.delete();
//        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out))
            {
                out.flush();
                out.close();
            }
            ImageView iv3 =(ImageView)findViewById(R.id.iv3);

            /**
             * >>>>>>>> 第一步：只获取图片的宽和高
             */
            BitmapFactory.Options options = new BitmapFactory.Options();

            // 设置只获取图片的信息，到时候记得还原恢复成false
            options.inJustDecodeBounds = true;

            /*
             * 第一次加载图片，只为获得宽高
             */
            BitmapFactory.decodeFile("/sdcard/DCIM/Camera/"+bitName, options);

            // 获取图片的宽高
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;

            /**
             * >>>>>>>> 第二步：获取屏幕的宽和高
             */

            // 获取手机屏幕的宽高，手机屏幕属于Window
            Display display = getWindow().getWindowManager().getDefaultDisplay();
            int windowWidth = display.getWidth();
            int windowHeight = display.getHeight();

            /**
             * >>>>>>>> 第三步：图片宽和高 对比 屏幕宽和高 = 计算宽高的缩放比例
             */

            /*
             * 计算宽高的缩放比例
             * valueX 代表横行 宽
             * valueY 代表纵向 高
             */
            int valueX = outWidth / windowWidth;
            int valueY = outHeight / windowHeight;

            /**
             * 判断宽和高的缩放比例哪个大，哪个大我就取哪个的值，只要把大的值处理即可
             */
            int value = valueX > valueY ? valueX : valueY;

            // 防止加载的图片，比屏幕小，如果比屏幕小，就没有必要缩放了
            if (0 >= value) {
                value = 1;
            }

            // 在这里要恢复options.inJustDecodeBounds，才能设置下面的代码，设置false需要加载图片内容啦
            options.inJustDecodeBounds = false;

            // 指定缩放比率
            options.inSampleSize = value;

            /**
             * 参数一：不推荐/sdcard/big.JPG 这种方式，因为每部手机的这个路径会变的，所以开发中要用Environment
             * 参数二：Options 选项 可以设置图片的比例缩放，从而解决图片内存溢出的问题
             */
            Bitmap bitmap2 = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/"+bitName, options);
            iv3.setImageBitmap(bitmap2);

            //iv3.setImageBitmap(bitmap);
         //////   Uri uri = Uri.fromFile(new File("/sdcard/DCIM/Camera/"+bitName));
         ////   iv3.setImageURI(uri);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

//旋转
    private  Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

        } catch (OutOfMemoryError ex) {
        }
        return null;

    }

    public void takePhoto(View view ){
        Toast.makeText(this,"正在拍照...",Toast.LENGTH_SHORT).show();;
        cam.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //技术：图片压缩技术（如果图片不压缩，图片大小会过大，会报一个oom内存溢出的错误）
               // BitmapFactory.Options options =new BitmapFactory.Options();
               // options.inJustDecodeBounds=true;
                //          options.inJustDecodeBounds=false;
               // options.inSampleSize=2;
               // BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);


                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                //顺时针旋转90度 手机
               //bitmap=adjustPhotoRotation(bitmap,90);
               //大屏 旋转-90度
                bitmap=adjustPhotoRotation(bitmap,-90);

                //  第二张根据第一张生成

            ////    Bitmap modBm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                //2、以生成的第二张图片做画板
            ////    Canvas canvas = new Canvas(modBm);

           ////     Paint paint = new Paint();
           ////     paint.setColor(Color.BLACK);
         ////       paint.setAntiAlias(true);
               //水平镜像
         ////       Matrix matrix =new Matrix();
           ////     matrix.setScale(-1, 1);
         ////       matrix.postTranslate(bitmap.getWidth(), 0);
         ////       canvas.drawBitmap(bitmap, matrix, paint);
                try {
                    //保存sv图片成jpeg，并且更新到iv3上
                ////    saveBitmap(modBm,"pic1.jpeg");
                    saveBitmap(bitmap,"pic1.jpeg");

                    //截屏并上传到图床
                    cutpic();
                } catch (IOException e) {
                    e.printStackTrace();
               }


            }
        });

    }
  //  Camera.Parameters parameters = Camera.getParameters();
    private class MySurfaceViewCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //cam = Camera.open(); // 取得第一个摄像头
            cam=Camera.open(0);

            cam.setDisplayOrientation(90); // 纠正摄像头自动旋转，纠正角度，如果引用，则摄像角度偏差90度

            try {
                cam.setPreviewDisplay(holder);
            } catch (IOException e) {
            }

            cam.startPreview(); // 进行预览
            previewRunning = true; // 已经开始预览
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (cam != null) {
                if (previewRunning) {
                    cam.stopPreview(); // 停止预览
                    previewRunning = false;

                }
                cam.release();
            }

        }

    }
    //上传至图床
    private void upload(File file) {
        FileUploadObserver<JsonBean> fileUploadObserver = new FileUploadObserver<JsonBean>() {
            @Override
            public void onSuccess(JsonBean bean) {

                jsonBean = bean;
                Log.d("jsonBean.code","jsonBean.code:+++++++++"+jsonBean.code.toString());
                try{
                    if (jsonBean.code.equals("success")) {
                        Log.d("a7889", "上传成功");

                        Log.d("url", jsonBean.data.url);
                        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(jsonBean.data.url, 480, 480);
                        iv1=(ImageView) findViewById(R.id.iv1);
                        iv1.setImageBitmap(mBitmap);
                        //显示下载按钮
                        b4.setVisibility(View.VISIBLE);
                    } else {
                        //上传失败
                        //maketext决定Toast显示内容
                        Toast toastCenter = Toast.makeText(getApplicationContext(),"当前网络繁忙\n，您可以再拍一张试一试",Toast.LENGTH_LONG);

                        //setGravity决定Toast显示位置
                        toastCenter.setGravity(Gravity.CENTER,0,0);

                        //调用show使得toast得以显示
                        toastCenter.show();
                        Log.d("a4444上传失败", "ffuucckk");
                        //    showToast("上传失败: " + jsonBean.msg);
                    }

                }catch(Exception e){
                    Log.d("upload error::::::","upload error 1:"+e.toString());
                }


            }

            @Override
            public void onFail(Throwable e) {
               // hideProgressDialog();
               // showToast("上传失败");
                Log.e("upload faile：", e.toString());
            }

            @Override
            public void onProgress(int progress) {
                Log.d("progress upload:", progress + "");
               // progressDialog.setProgress(progress);
            }

            @Override
            public void onSubscribe(Disposable d) {

                //showProgressDialog();
            }
        };

        HttpRequest.getApi()
                .upload(fileToMultipartBodyPart(file, fileUploadObserver))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);
    }

    //file转换为MultipartBodyPart
    private MultipartBody.Part fileToMultipartBodyPart(File file, FileUploadObserver<JsonBean> fileUploadObserver) {
        ProgressRequestBody requestBody = new ProgressRequestBody(file, fileUploadObserver);
        return MultipartBody.Part.createFormData("smfile", file.getName(), requestBody);
    }


    //新的图片压缩算法
    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if(bm == null){
            return  null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm,degree) ;
        ByteArrayOutputStream baos = null ;
        try{
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        }finally{
            try {
                if(baos != null)
                    baos.close() ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm ;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }


    private static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
        if(bitmap == null)
            return null ;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }



}
