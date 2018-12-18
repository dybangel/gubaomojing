package com.example.administrator.robot;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.robot.database.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tpnet
 * 16/4/14
 */
public class BarrageViewEnglish extends RelativeLayout {

    private OnClickActionListener mClick = null;
    // 为接口设置监听器
    public void setOnClickActionListener(OnClickActionListener down) {
        mClick = down;
    }
    //定义接口
    public interface OnClickActionListener {
        void onClick(String str);
    }

    private Context mContext;
    private BarrageHandler mHandler = new BarrageHandler();
    private Random random = new Random(System.currentTimeMillis());   //获取自1970年1月1日0时起到现在的毫秒数

    private static final long BARRAGE_GAP_MIN_DURATION = 18000;//两个弹幕的最小间隔时间 3000
    private static final long BARRAGE_GAP_MAX_DURATION = 18000;//两个弹幕的最大间隔时间 5000
    private int maxSpeed = 10000;   // 最小速度，ms，越大越慢
    private int minSpeed = 10000;    //  最快速度，ms，越大越慢6000;
    private int maxSize = 8;       //最大字体文字，dp
    private int minSize = 8;       //最小文字大小，dp15;

    private int totalHeight = 1;    //总高度
    private int lineHeight = 1;     //每一行弹幕的高度
    private int totalLine = 1;      //弹幕的行数
    private static List<String> itemText = new ArrayList<>();  //内容list
    private int textCount;   //条目的数量
    public  static String questions[],questions_english[],answers[],answers_english[];
    public static db mydb;
    public BarrageViewEnglish(Context context) {
        this(context, null);
    }

    public BarrageViewEnglish(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageViewEnglish(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mydb=new db();
    //    questions= mydb.GetAllquestion();
        questions_english=mydb.GetAllquestion_english();
     //   answers=mydb.GetAllanswer();
        answers_english=mydb.GetAllanswer_english();
        initData();
        init();
    }

    private void init() {
        //初始化条目
        textCount = itemText.size();
    }
    private static void cleanlist(){
        int size=itemText.size();
     //   Log.d("",Integer.toString(size));
        if(size>0){
            //System.out.println(size);
            itemText.removeAll(itemText);
            //adapter.notifyDataSetChanged();
            //listView.setAdapter(adapter);
        }
    }
    private void generateItem() {
        BarrageItem item = new BarrageItem();
        //String tx = itemText[(int) (Math.random() * textCount)];
        //随机获取条目
        String tx;
        tx = itemText.get((int) (Math.random() * textCount));

        //范围随机获取大小
        int sz = (int) (minSize + (maxSize - minSize) * Math.random());
        //垂直弹幕使用 字体大小
        sz=25;
        //弹幕创建textView 控件
        item.textView = new TextView(mContext);

        //设置文本属性
        item.textView.setText(tx);
        item.textView.setTextSize(sz);
        //item.textView.setBackgroundColor(R.color.black);

        //item.textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        item.textView.setTextColor(Color.rgb(135,206,235));
        //获取滚动TextView的宽度
        item.textMeasuredWidth = (int) getTextWidth(item, tx, sz);

        //设置随机移动速度
       // item.moveSpeed = (int) (minSpeed + (maxSpeed - minSpeed) * Math.random());
      //固定速度，垂直状态下用 ，越大速度越慢
        item.moveSpeed =60000;
        //为0则,初始化
        if (totalLine == 0) {
            //获取当前View的实际高度
            totalHeight = getMeasuredHeight();
            //获取行高
            lineHeight = getLineHeight();
            //获取总行数
            totalLine = totalHeight / lineHeight;
            //垂直弹幕用这个
            totalLine=1;
        }

        //垂直方向显示位置,行数的随机一行，nextInt(n) 返回一个大于等于0小于n的随机数
        System.out.println(totalLine+" "+lineHeight);
        item.verticalPos = random.nextInt(totalLine) * lineHeight;
        //垂直弹幕使用
        item.verticalPos=1;
        //显示滚屏
        showBarrageItem(item);

    }


    /**
     * 显示TextView 的动画效果
     * @param item
     */
    private void showBarrageItem(final BarrageItem item) {
        //屏幕宽度 像素
        int leftMargin = this.getRight() - this.getLeft() - this.getPaddingLeft();
        //System.out.println(item.textView.getText()+(leftMargin+""));
        //显示的TextView 的位置，
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = item.verticalPos;
        this.addView(item.textView, params);

        //设置回调回调点击
        final String temp = item.textView.getText().toString();

        item.textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Nlp np =new Nlp();
            // new   Nlp.NlpMessage().start();

          //      String aa="对不起，我没有听清！";
//                try {
//                   //  aa=np.NlpMessage(temp);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //点击后 朗读点击的内容
                String ans="";
                for(int i = 0;i < questions_english.length; i ++){

                        if(temp==questions_english[i].toString()){
                            ans=answers_english[i].toString();
                            MainActivity.getMainActivity().compose(ans);
                            break;
                        }
                  //  }

                }


//                if(temp=="你叫什么名字"){
//                    ans="我是总督府设计师，拉查洛维茨";
//
//                 //   Intent intent = new Intent(MainActivity.getMainActivity(), ImageTargets.class);
//                    // startActivity(intent);
//                  //  MainActivity.getMainActivity().startActivity(intent);
//                    MainActivity.getMainActivity().compose(ans);
//                }else{
//                    MainActivity.getMainActivity().compose(temp);
//                }

             //   new MainActivity().compose(ans);


        //      np.new GetMessage().run();

             //   askContent = sbuff.toString();
              //  new MainActivity.GetMessage().start();
                //  mClick.onClick(temp);
            }
        });

        //使用属性动画，确保控件位置不固定
        transAnimRun(item, leftMargin);
    }

    private void transAnimRun(final BarrageItem item, int leftMargin)
    {
        ObjectAnimator objAnim =ObjectAnimator
                //滑动位置是x方向滑动，从屏幕宽度+View的长度到左边0-View的长度
          //      .ofFloat(item.textView,"translationX" , leftMargin, -item.textMeasuredWidth)
                .ofFloat(item.textView,"translationY" , -1800)
                .setDuration(item.moveSpeed);
        //设置移动的过程速度，开始快之后慢，横向使用
      //  objAnim.setInterpolator(new DecelerateInterpolator());
        //开始动画
        objAnim.start();

        objAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画执行完毕，清除动画，删除view，
                item.textView.clearAnimation();
                BarrageViewEnglish.this.removeView(item.textView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 计算TextView中字符串的长度
     *
     * @param text 要计算的字符串
     * @param Size 字体大小
     * @return TextView中字符串的长度
     */
    public float getTextWidth(BarrageItem item, String text, float Size) {
        //Rect表示一个矩形，由四条边的坐标组成
        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //System.out.println(item.textView.getText()+(bounds.width()+"")+"宽度");
        return bounds.width();
    }

    /**
     * 获得每一行弹幕的最大高度
     *
     * @return
     */
    private int getLineHeight() {
        BarrageItem item = new BarrageItem();
        String tx;
        tx = itemText.get(0);
        item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(maxSize);

        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(tx, 0, tx.length(), bounds);
        return bounds.height();
    }

    class BarrageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //每个弹幕产生的间隔时间随机
            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
            //垂直弹幕使用， 越大，两个弹幕之间间隔时间越大
            duration=5000;
            generateItem();
            this.sendEmptyMessageDelayed(0, duration);
        }
    }

    /**
     * 当view显示在窗口的时候，回调的visibility等于View.VISIBLE。。当view不显示在窗口时，回调的visibility等于View.GONE
     *
     * 窗口隐藏了，把内容全部清空，防止onPause时候内容停滞
     *
     * **/
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == View.GONE){
            mHandler.removeMessages(0);
           // initData();
        }else if(visibility == View.VISIBLE){
            mHandler.sendEmptyMessage(0);
        //    initData();
        }
    }

    /**
     *
     * 初始化数据
     *
     * **/
   private void initData(){

           for(int i = 0;i < questions_english.length; i ++){
               itemText.add(questions_english[i].toString());


    }
    }
}
