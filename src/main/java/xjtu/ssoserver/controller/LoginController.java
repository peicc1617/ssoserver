package xjtu.ssoserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import xjtu.ssoserver.dao.ClientInfoMapper;
import xjtu.ssoserver.dao.TokenMapper;
import xjtu.ssoserver.dao.UserMapper;
import xjtu.ssoserver.entity.ClientInfo;
import xjtu.ssoserver.entity.Result;
import xjtu.ssoserver.entity.Token;
import xjtu.ssoserver.entity.User;
import xjtu.ssoserver.util.HttpUtil;
import xjtu.ssoserver.util.SSOURLUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @基本功能:
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-06 18:00:10
 **/
@Controller
public class LoginController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    ClientInfoMapper clientInfoMapper;
    private Logger LOGGER= LoggerFactory.getLogger(getClass());
    @RequestMapping("/login")
    public String login(String userName, String password, String redirectUrl, HttpServletRequest req, HttpSession session, HttpServletResponse resp){
        LOGGER.info("当前输入用户名为："+userName+",密码为："+password);
        User user=userMapper.getUser(userName,password);
        if (user!=null) {// 验证通过
            LOGGER.info("用户名、密码正确,当前用户信息为："+user.toString());
            //生成token
            String token = UUID.randomUUID().toString();
            long expiryTime=1000*60*60*24;//设置过期时间(一天)
            //user转换为json字符串
            String userInfo= JSONObject.toJSONString(user);
            //存储token信息
            tokenMapper.storeToken(token,userInfo,expiryTime);
            //将token信息写入到session中
            session.setAttribute("token",token);
            //将token信息写入到cookie中
            Cookie cookie=new Cookie("token",token);
            //重定向地址，形如：http://www.spider.xjtu.com:8084/serviceRegister.html
//            String cookieDomain=redirectUrl.substring(redirectUrl.indexOf(".")+1,redirectUrl.lastIndexOf(":"));
//            cookie.setDomain("xjtu.com");
            cookie.setDomain("localhost");
            //将cookie写入客户端
            resp.addCookie(cookie);
            //将token信息写入session
            session.setAttribute("token",token);
            String str=req.getParameter(redirectUrl);
            //重定向到之前访问页面
            return "redirect:"+redirectUrl;
        } else {
            LOGGER.info("用户名、密码错误，重定向到登录页面");
            return "login";
        }
    }
    /***
     * @函数功能：判断用户是否登录
     * @param redirectUrl:
     * @param session:
     * @param model:
     * @return：java.lang.String
     */
    @RequestMapping(value = "/checkLogin",method = RequestMethod.GET)
    public String checkLogin(@RequestParam(name = "redirectUrl",required = false)String redirectUrl, HttpSession session, Model model){
        //@RequestParam String redirectUrl, HttpSession session, Model model
        //判断是否存在全局会话

        String token=(String) session.getAttribute("token");// 如果能取出token信息，说明全局会话存在
        if (StringUtils.isEmpty(token)){
            //如果为空，表明没有全局会话
            model.addAttribute("redirectUrl",redirectUrl);
//            map.put("redirectUrl",redirectUrl);
            LOGGER.info("当前用户没有登录，即将跳转到统一登录页面");
            //跳转到登录页面
            return "login";
        } else {
            //有全局会话，直接放行
//            model.addAttribute("userInfo",userInfo);
//            map.put("userInfo",userInfo);
            return "redirect:"+redirectUrl;

        }
    }
    /***
     * @函数功能：
     * @param token: 客户端携带的token信息
     * @param clientLogOutURL:客户端注销地址
     * @param jesessionId:客户端sessionId，用于销毁客户端对应session
     * @return：java.lang.String
     */
    @RequestMapping(value = "/verify",method=RequestMethod.POST)
    @ResponseBody
    public Result verifyToken(String token, String clientLogOutURL, String jesessionId){
        if (tokenMapper.containsToken(token)!=null){//判断token是否存在，防止伪造的token信息
            LOGGER.info("token信息正确");
            //取出token对应的id以及用户信息
            Token info=tokenMapper.getInfo(token);
            long tokenId=info.getId();
            String userInfo=info.getUserInfo();
            //json字符串转换成对象
            User user= JSON.parseObject(userInfo,User.class);
            //存储客户端登出地址以及sessionId
            LOGGER.info("存储客户端登出地址："+clientLogOutURL+",客户端sessionId:"+jesessionId);
            clientInfoMapper.storeClientInfo(tokenId,clientLogOutURL,jesessionId);
            return Result.success(user);

        }
        LOGGER.info("token信息错误");
        return Result.failure("token信息验证未通过");
    }
    @RequestMapping("/logOut")
    public void logOut(HttpServletRequest req,HttpServletResponse resp,HttpSession session,Model model) throws IOException {
        LOGGER.info("注销用户登录信息");
        //获取重新登录后的重定向地址（点击注销的那个页面的地址）
        String redirectUrl=req.getHeader("referer");
        model.addAttribute("redirectUrl",redirectUrl);
        //取出session中的token信息
        String token=(String)session.getAttribute("token");
        //获取tokenId(可能为空)
        Long tokenId=tokenMapper.getTokenIdByToken(token);
        //获取客户端登出地址以及sessionId
        if(tokenId!=null){
            List<ClientInfo> clientInfoList=clientInfoMapper.getAllClientInfoByTokenId(tokenId);
            if (clientInfoList!=null&&clientInfoList.size()>0) {
                clientInfoList.forEach(clientInfo -> {
                    try {
                        //销毁每个客户端的会话
                        HttpUtil.sendHttpRequest(clientInfo.getClientLogOutURL(),clientInfo.getJesessionId());
                    }
                    catch (IOException e) {
                        LOGGER.error("退出登录异常");
                        e.printStackTrace();
                    }
                });
            }
            //清空token数据库对应记录
            tokenMapper.removeToken(token);
            //清空client_info数据库对应记录
            clientInfoMapper.removeClientInfoByTokenId(tokenId);
        }
        //销毁全局会话
        session.invalidate();
        //清空cookie信息
        Cookie cookie=new Cookie("token","");
        cookie.setDomain("xjtu.com");
        cookie.setMaxAge(0);// 删除cookie
        resp.addCookie(cookie);
        //跳转到退出页面
        resp.sendRedirect("http://www.sso.xjtu.com:8090"+"/checkLogin?redirectUrl="+ redirectUrl);
//        return "login";
    }
}
