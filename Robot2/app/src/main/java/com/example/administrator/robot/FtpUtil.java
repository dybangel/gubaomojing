package com.example.administrator.robot;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

class FtpUtil {
    private FTPClient ftpClient;
    private String serverIp;
    private int port;
    private String userName;
    private String passWord;
    public FtpUtil(String serverIp, int port, String userName, String passWord) {
        super();
        this.serverIp = serverIp;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
    }

    /**
     * 连接ftp服务器
     * @return
     */
    public boolean open(){
        if(ftpClient != null && ftpClient.isConnected())
            return true;
        //连接服务器
        try{
            ftpClient = new FTPClient();
            ftpClient.connect(serverIp, port);    //连接服务器
            ftpClient.login(userName, passWord);    //登录

            ftpClient.setBufferSize(1024);
            //设置文件类型，二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){ //判断ftp服务器是否连通
                this.closeFtp();
                System.out.println("FtpServer连接失败！");
                return false;

            }else{
                System.out.println("FtpServer连接成功！！！！！！");
            }
            return true;
        }catch(Exception e){
            this.closeFtp();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关闭ftp服务器，主要是对disconnect函数的调用
     */
    public void closeFtp() {
        try {
            if (ftpClient != null && ftpClient.isConnected())
                ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Close Server Success :"+this.serverIp+";port:"+this.port);
    }

    /**
     * 从ftp服务器下载文件
     * @param ftpDirectoryAndFileName 包含ftp部分的文件路径和名字，这里是从ftp设置的根目录开始
     * @param localDirectoryAndFieName 本文的文件路径和文件名字，相当于是绝对路径
     * @return
     */
    public boolean donwLoad(String ftpDirectoryAndFileName,String localDirectoryAndFieName){
        if(!ftpClient.isConnected()){
            return false;
        }
        FileOutputStream fos =null;
        try {
            fos = new FileOutputStream(localDirectoryAndFieName);
            //下面的函数实现文件的下载功能，参数的设置解决了ftp服务中的中文问题。这里要记得更改eclipse的编码格式为utf-8
            ftpClient.retrieveFile(new String(ftpDirectoryAndFileName.getBytes(), "iso-8859-1"), fos);
            fos.close();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }finally{

            this.closeFtp();
        }

    }

    /**
     *从本地上传文件到ftp服务器
     * @param ftpDirectoryAndFileName
     * @param localDirectoryAndFieName
     * @return
     */
    public boolean upLoading(String ftpDirectoryAndFileName,String localDirectoryAndFieName){
        if(!ftpClient.isConnected()){
            return false;
        }

        FileInputStream fis = null;

        try {
            Log.d("ftputil:","开始上传ftp.....");
            fis = new FileInputStream(localDirectoryAndFieName);
            //和文件的下载基本一致，但是要注意流的写法
            ftpClient.storeFile(new String(ftpDirectoryAndFileName.getBytes(), "utf-8"), fis);//iso-8859-1
            fis.close();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("catch++++++++++++",e.toString());
            e.printStackTrace();
            return false;
        }finally{
            this.closeFtp();
        }
    }

}
