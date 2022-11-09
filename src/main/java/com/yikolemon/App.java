package com.yikolemon;

import okhttp3.*;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static OkHttpClient client = new OkHttpClient();
    public static void main( String[] args ) throws IOException, InterruptedException {
        Process pc = Runtime.getRuntime().exec(" cmd /c echo %cd%");
        pc.waitFor();
        InputStream xx = pc.getInputStream();
        byte[] da = new byte[1024];
        String rs = "";
        while(xx.read(da) != -1) {
            rs += new String(da,"GBK");
        }
        System.out.println(rs);
        Process process = Runtime.getRuntime().exec("netsh wlan add profile filename=wifi.xml");

        process.waitFor();
        InputStream inputStream = process.getInputStream();
        byte[] data = new byte[1024];
        String result = "";
        while(inputStream.read(data) != -1) {
            result += new String(data,"GBK");
        }
        System.out.println(result);
        Process process2 = Runtime.getRuntime().exec("netsh wlan connect name=NUFE-STU");
        process2.waitFor();
        InputStream inputStream2 = process2.getInputStream();
        byte[] data2 = new byte[1024];
        String result2 = "";
        while(inputStream2.read(data) != -1) {
            result2 += new String(data,"GBK");
        }
        System.out.println(result2);
        Thread.sleep(2000);
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
            throw new RuntimeException(e);
        }


    }
}
