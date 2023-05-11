package com.yikolemon;

import okhttp3.*;

import java.io.*;
import java.util.Properties;

public class App 
{
    public static OkHttpClient client = new OkHttpClient();
    public static void main( String[] args ) throws Exception {
        Process process = Runtime.getRuntime().exec("cmd /c call .\\关闭windows系统代理.bat");
        System.out.println(getRes(process));
        Process process1 = Runtime.getRuntime().exec("netsh wlan add profile filename=wifi.xml");
        System.out.println(getRes(process1));
        Process process2 = Runtime.getRuntime().exec("netsh wlan connect name=NUFE-STU");
        System.out.println(getRes(process2));
        //让wifi连接上
        confirmWifiConnect();
        //校园网压验证
        sendPost();
        while (!checkNet()){
            //检测网络畅通
            sendPost();
        }
    }

    static String getRes(Process process) throws InterruptedException, IOException {
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        byte[] data = new byte[1024];
        String result = "";
        while(inputStream.read(data) != -1) {
            result += new String(data,"GBK");
            result=result.trim();
        }
        return result;
        //System.out.println(result);
    }

    static void sendPost() throws IOException {
        String url = "http://10.200.253.5/drcom/login";
        InputStream in = new BufferedInputStream(new FileInputStream("profiles.properties"));
        Properties p = new Properties();
        p.load(in);
        String username = (String)p.get("username");
        String password = (String)p.get("password");
        //get请求
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append("?callback=dr1003");
        builder.append("&DDDDD="+username+"@njxy");
        builder.append("&upass="+password);
        url=builder.toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println( response.body().string());
        } catch (IOException e) {
            System.out.println("等待wifi连接");
        }
    }

    static boolean checkNet() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("ping www.baidu.com");
        String res = getRes(process);
        if (res.contains("请求超时")){
            return false;
        }
        //请求超时。
        return true;
    }

    static void confirmWifiConnect() throws IOException, InterruptedException {
        //flag为连接状态
        boolean flag=false;
        //未连接上，循环检测
        while (!flag) {
            Process process = Runtime.getRuntime().exec("netsh wlan show interfaces");
            String s=getRes(process);
            System.out.println(s);
            if (s.contains("已连接")) {
                flag=true;
            }else {
                //未连接，延迟200ms后检测下一次
                Thread.sleep(200);
            }
        }
    }
}
