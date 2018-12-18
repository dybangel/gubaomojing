package com.example.administrator.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class SuperSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    private Camera cam = null;
    public SuperSurfaceView(Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        new Thread(new MyThread()).start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            Canvas canvas = surfaceHolder.lockCanvas(null);//获取画布
            doDraw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像

            cam = Camera.open(); // 取得第一个摄像头

            cam.setDisplayOrientation(90); // 纠正摄像头自动旋转，纠正角度，如果引用，则摄像角度偏差90度

            try {
                cam.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
            }

            cam.startPreview(); // 进行预览

        }
    }

    //将绘制图案的方法抽象出来，让子类实现，调用getBitmap方法时就会调用此方法
    protected abstract void doDraw(Canvas canvas);

    //调用该方法将doDraw绘制的图案绘制在自己的canvas上
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        doDraw(canvas);
        return bitmap;
    }



    public void takePhoto(View view ){
        cam.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //技术：图片压缩技术（如果图片不压缩，图片大小会过大，会报一个oom内存溢出的错误）
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

               // return bitmap;
//                try {
//                    FileOutputStream fos = new FileOutputStream("/mnt/sdcard/qq"+System.currentTimeMillis()+".png");
//                    //图片保存路径
//                    bitmap.compress(Bitmap.CompressFormat.PNG,85,fos);
//                    //压缩格式，质量，压缩路径
//                    camera.stopPreview();
//                    camera.startPreview();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }







}
