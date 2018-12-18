package com.example.administrator.robot.qiniuultra;

/**
 * Created by H10002056 on 2016/12/7.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.robot.common.QiniuException;
import com.example.administrator.robot.common.Zone;
import com.example.administrator.robot.http.Response;
import com.example.administrator.robot.storage.Configuration;
import com.example.administrator.robot.storage.UploadManager;
import com.example.administrator.robot.util.Auth;

import java.io.IOException;
import java.util.UUID;


public class Upload {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "YSrky-exudq2WfG55vCEnwCs37GZGE6QFs0FOxYR";
    String SECRET_KEY = "sRuEwrksFxWfoayR6EcaRsrETVBdqDqjKNbaH7eF";
    //要上传的空间
    String bucketname = "mojing";
    //上传到七牛后保存的文件名
    //String key = UUID.randomUUID()+ "" ;
   String key="pic2.jpeg";
    //上传文件的路径
//    String FilePath = "/storage/emulated/0/DCIM/Camera/xx.mp4";
//    String FilePath = "file:///storage/emulated/0/DCIM/Camera/xx.mp4";  // 5.4
//    String FilePath = "content://media/external/video/media/1247";  //166

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void upload(Context context, String FilePath) throws IOException {
        try {
            Uri uri = Uri.parse(FilePath);
//            String path = getRealFilePath(context,uri);
            String path = FilePath;
            Log.i("c00james", path);
            //调用put方法上传
          //  String token = "SsH5vHOCnsBo46tgKXrpj6sNE2F-e0dA2aU5SNtc:MJjscWY_DksaJOnyyRVR2qqZXdo=:eyJzY29wZSI6ImJhamFuanUtcCIsImRlYWRsaW5lIjoxNDgyMzk4MzE5fQ==";
            String token = getUpToken();
            Response res = uploadManager.put(getRealFilePath(context,uri), key, token);
//            Response res = uploadManager.asyncPut(getRealFilePath(context,uri), key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
}
