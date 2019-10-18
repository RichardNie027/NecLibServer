package com.nec.lib.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {

    /**
     *
     * @param URL
     * @param param
     * @param method
     * @param contentType "xml", "x-www-form-urlencoded", "json"
     * @return
     */
    public static String request(String URL, String param, String method, String contentType) {

        HttpURLConnection conn = null;
        // 数据输出流，输出参数信息
        DataOutputStream dataOut = null;
        // 数据输入流，处理服务器响应数据
        BufferedReader dataIn = null;

        method = method==null || (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("POST"))? "GET" : method.toUpperCase();

        try {
            java.net.URL url = new java.net.URL(URL);

            // 将url以open方法返回的urlConnection连接强转为HttpURLConnection连接(标识一个url所引用的远程对象连接)
            // 此时cnnection只是为一个连接对象,待连接中
            conn = (HttpURLConnection) url.openConnection();

            // 设置连接输出流为true,默认false (post请求是以流的方式隐式的传递参数)
            conn.setDoOutput(true);
            // 设置连接输入流为true
            conn.setDoInput(true);
            // 设置请求方式
            conn.setRequestMethod("GET");
            // post请求缓存设为false
            conn.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            conn.setInstanceFollowRedirects(true);

            // 设置内容的类型,设置为经过urlEncoded编码过的form参数
            conn.setRequestProperty("Content-Type", "application/" + contentType);
            //conn.setRequestProperty("accept", "application/xml");
            // conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;
            // MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立连接
            // (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            conn.connect();

            if(method.equalsIgnoreCase("POST")) {
                // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
                dataOut = new DataOutputStream(conn.getOutputStream());
                // 将参数输出到连接
                dataOut.writeBytes(param != null ? param : "");
                // 输出完成后刷新并关闭流
                dataOut.flush();
            }

            // 连接发起请求,处理服务器响应 (从连接获取到输入流并包装为bufferedReader)
            dataIn = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            // 用来存储响应数据
            StringBuilder sb = new StringBuilder();
            // 循环读取流
            while ((line = dataIn.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOut != null) {
                    dataOut.close();
                }
                if (dataIn != null) {
                    dataIn.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
