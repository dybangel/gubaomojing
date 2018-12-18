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
import android.hardware.Camera;
import android.media.AudioManager;
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
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private ImageView iv1;

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
                ImageView iv1 =(ImageView)findViewById(R.id.iv1);
                iv1.setVisibility(View.VISIBLE);
                findViewById(R.id.button4).setVisibility(View.GONE);
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
        Bitmap bitmap=capture(this);
        if (bitmap != null) {
            try {
                saveBitmap(bitmap,"pic2.jpeg");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Upload().upload(getBaseContext(),"/sdcard/DCIM/Camera/pic2.jpeg");
                             f =new File("/sdcard/DCIM/Camera/pic2.jpeg");
                             upload(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

           //     b4.setEnabled(true);
                b4.setVisibility(View.VISIBLE);
          //      b5.setEnabled(true);
                b5.setVisibility(View.VISIBLE);
                Log.d("a7888", "存储完成");
            } catch (Exception e){
            }
        }

    }

    private void saveBitmap(Bitmap bitmap,String bitName) throws IOException
    {
        //  String path1 = Environment.getExternalStorageDirectory() + File.separator;
        File file = new File("/sdcard/DCIM/Camera/"+bitName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out))
            {
                out.flush();
                out.close();
            }
            ImageView iv3 =(ImageView)findViewById(R.id.iv3);
            //iv3.setImageBitmap(bitmap);
            Uri uri = Uri.fromFile(new File("/sdcard/DCIM/Camera/"+bitName));
            iv3.setImageURI(uri);
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

        cam.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //技术：图片压缩技术（如果图片不压缩，图片大小会过大，会报一个oom内存溢出的错误）
                BitmapFactory.Options options =new BitmapFactory.Options();
  //              options.inJustDecodeBounds=true;

//                BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
    //            options.inSampleSize=2;
      //          options.inJustDecodeBounds=false;
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                //顺时针旋转90度 手机
               //bitmap=adjustPhotoRotation(bitmap,90);
               //大屏 旋转-90度
                bitmap=adjustPhotoRotation(bitmap,-90);

                //  第二张根据第一张生成
                Bitmap modBm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                //2、以生成的第二张图片做画板
                Canvas canvas = new Canvas(modBm);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
               //水平镜像
                Matrix matrix =new Matrix();
                matrix.setScale(-1, 1);
                matrix.postTranslate(bitmap.getWidth(), 0);
                canvas.drawBitmap(bitmap, matrix, paint);
                try {
                    //保存sv图片成jpeg，并且更新到iv3上
                    saveBitmap(modBm,"pic1.jpeg");
                    //截屏并保存图片到设备
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
           //     hideProgressDialog();
                jsonBean = bean;
                if (jsonBean.code.equals("success")) {
                 //   showToast("上传成功");
                //    text.setVisibility(View.VISIBLE);
                    Log.d("url", jsonBean.data.url);
                    Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(jsonBean.data.url, 480, 480);
                    iv1=(ImageView) findViewById(R.id.iv1);
                    iv1.setImageBitmap(mBitmap);
                //    text.setText("图片链接：" + jsonBean.data.url);
                //    delete.setVisibility(View.VISIBLE);
                    //根据返回的外链加载该图
              //      Glide.with(getBaseContext()).load(jsonBean.data.url).into(imageUpload);
                } else {
                    //上传失败
                //    showToast("上传失败: " + jsonBean.msg);
                }
            }

            @Override
            public void onFail(Throwable e) {
               // hideProgressDialog();
               // showToast("上传失败");
                Log.e("MainActivity", e.toString());
            }

            @Override
            public void onProgress(int progress) {
                Log.d("progress:", progress + "");
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
}
