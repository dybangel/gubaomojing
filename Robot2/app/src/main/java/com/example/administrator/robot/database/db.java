package com.example.administrator.robot.database;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.example.administrator.robot.MainActivity;
import com.example.administrator.robot.MainActivity;
import com.example.administrator.robot.R;

import java.io.File;

public final class db {
   public  void  initdb(){
        //    SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.administrator.robot/databases/stu.db",null);
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator+"qa.db",null);
//https://www.cnblogs.com/foxy/p/7725010.html
        //建立数据表tqa
        String stu_table="create table if not exists tqa(fid integer primary key autoincrement,fquestion text,fanswer text,fisenable integer,fq1 integer,fwav integer)";
        //执行SQL语句
        db.execSQL(stu_table);

        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加问题
        cValue.put("fquestion","您多大了？");
        //添加答案
        cValue.put("fanswer","现在的我37岁，但我永远18岁！");
        //调用insert()方法插入数据
        //  db.insert("tqa",null,cValue);
//        String stu_sql="insert into stu_table(sname,snumber) values('xiaoming','01005')";db.execSQL(sql);
//判断数据表里有没有记录，如果没有才真正执行insert 命令

        //查询数据
        //查询获得游标
        Cursor cursor = db.query ("tqa",null,"fquestion='您多大了？'",null,null,null,null);

//判断游标是否为空
        if(cursor.moveToFirst()){
//            //遍历游标
            for(int i=0;i<cursor.getCount();i++){
                cursor.move(i);
//                //获得ID
                int id = cursor.getInt(0);
//                //获得用户名
                String fquestion=cursor.getString(1);
//                //获得密码
                String fanswer=cursor.getString(2);
//            //输出用户信息
                System.out.println(id+":"+fquestion+":"+fanswer);
            }
        }
        //return "";
    }
    //输入问题返回答案的函数
    private String GetAnswer(String question){
        String answer="";

       return answer;
    }
    //返回所有问题的函数，用于加载弹幕
    public  String[] GetAllquestion(){

         String  array[]=MainActivity.res.getStringArray(R.array.question);
            return array;
    }
    public String[] GetAllquestion_english(){
        String    array[]=MainActivity.res.getStringArray(R.array.question_english);
        return array;
    }
    //返回所有答案的函数，用于回答问题
    public  String[] GetAllanswer(){
            String array[]=MainActivity.res.getStringArray(R.array.answer);
            return array;
    }

    public String[] GetAllanswer_english(){
        String array[]=MainActivity.res.getStringArray(R.array.answer_english);
        return array;
    }

    private String[][] getTwoDimensionalArray(String[] array) {
        String[][] twoDimensionalArray = null;
        for (int i = 0; i < array.length; i++) {
            String[] tempArray = array[i].split("|");
            if (twoDimensionalArray == null) {
                twoDimensionalArray = new String[array.length][tempArray.length];
            }
            for (int j = 0; j < tempArray.length; j++) {
                twoDimensionalArray[i][j] = tempArray[j];
            }
        }
        return twoDimensionalArray;
    }
}
