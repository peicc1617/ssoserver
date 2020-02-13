package xjtu.ssoserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @基本功能:
 * @program:spider
 * @author:peicc
 * @create:2020-02-08 19:35:30
 **/
public class HttpUtil {
    private static Logger LOGGER= LoggerFactory.getLogger(HttpUtil.class);
    public static String sendHttpRequest(String httpUrl, Map<String,String> params) throws IOException {
        URL url=new URL(httpUrl);
        HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
        //设置请求方法
        httpConnection.setRequestMethod("POST");
        //设置输出
        httpConnection.setDoOutput(true);
        if (params!=null&&params.size()>0){
            //拼接请求参数
            StringBuilder sb=new StringBuilder();
            params.forEach((key,value)->{
                sb.append("&")
                        .append(key)
                        .append("=")
                        .append(value);
            });
            LOGGER.info("进入服务端"+sb.substring(1)+"验证token信息");
            httpConnection.getOutputStream().write(sb.substring(1).getBytes("UTF-8"));
        }
        //发送到服务器
        httpConnection.connect();
        //获取远程响应的内容.
        String responseContent = StreamUtils.copyToString(httpConnection.getInputStream(), Charset.forName("utf-8"));
        //关闭连接
        httpConnection.disconnect();
        return responseContent;
    }
    /***
     * @函数功能：销毁客户端session信息
     * @param httpUrl:
     * @param jesessionId:
     * @return：void
     */
    public static void sendHttpRequest(String httpUrl,String jesessionId) throws IOException {
        URL url=new URL(httpUrl);
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
        //设置请求方法
        httpURLConnection.setRequestMethod("POST");
        //设置输出
        httpURLConnection.setDoOutput(true);
        //设置cookie
        httpURLConnection.setRequestProperty("Cookie","JSESSIONID="+jesessionId);
        //连接
        httpURLConnection.connect();
        httpURLConnection.getInputStream();
        httpURLConnection.disconnect();
    }
}
