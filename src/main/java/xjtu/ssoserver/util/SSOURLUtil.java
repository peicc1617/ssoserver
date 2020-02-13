package xjtu.ssoserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;

/**
 * @基本功能:
 * @program:spider
 * @author:peicc
 * @create:2020-02-08 23:46:18
 **/
public class SSOURLUtil {
    private static Logger LOGGER= LoggerFactory.getLogger(SSOURLUtil.class);
    public  static String SSO_URL;
    public  static String CLIENT_URL;
    private static Properties properties=new Properties();
    static {
        try {
            LOGGER.info("加载配置文件application.properties");
            properties.load(SSOURLUtil.class.getClassLoader().getResourceAsStream("application.properties"));
            SSO_URL=properties.getProperty("sso.url");
            CLIENT_URL=properties.getProperty("client.url");
        }
        catch (IOException e) {
            LOGGER.error("配置文件application.properties加载出错");
            e.printStackTrace();
        }
    }
    /*** 
     * @函数功能：当客户端请求被拦截跳往统一认证中心时，需要携带重定向地址，以便在认证通过后进行重定向
     * @param req:
     * @return：java.lang.String
     */
    public static String getSendRedirectURL(HttpServletRequest req){
        StringBuilder sb=new StringBuilder();
        sb.append(CLIENT_URL)
                .append(req.getContextPath())
                .append(req.getServletPath());
        return sb.toString();
    }

}
