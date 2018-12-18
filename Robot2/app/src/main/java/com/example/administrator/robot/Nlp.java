package com.example.administrator.robot;

import android.os.Message;

import java.io.IOException;

public class Nlp {
    public static String message,askContent;

//    public  String NlpMessage(String askContent) throws IOException {
//        String msg;
//        msg =new GetHttpMessage().testGetRequest(askContent);
//        return msg;
//    }

    public static  class NlpMessage extends Thread{
        public void run(){
            Message msg =new Message();
            String askContent="";//,message="";
          try{
              msg.what=1;
              askContent="你叫什么名字？";
            //  message=new GetHttpMessage().testGetRequest(askContent);
              new MainActivity().compose(new GetHttpMessage().testGetRequest(askContent));
          } catch(Exception e){
              e.printStackTrace();
          }

        }
    }
}
