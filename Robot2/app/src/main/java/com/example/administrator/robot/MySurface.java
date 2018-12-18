package com.example.administrator.robot;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;


public class MySurface extends SurfaceView {
//    public MySurface(Context context) {
//        super(context);
//    }
//
//    public MySurface(Context context, AttributeSet attrs, int defStyleAttr) {
//       // super(context, attrs, defStyleAttr);
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MySurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
