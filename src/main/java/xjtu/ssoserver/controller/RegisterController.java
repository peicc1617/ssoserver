package xjtu.ssoserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xjtu.ssoserver.dao.UserMapper;
import xjtu.ssoserver.entity.User;

/**
 * @基本功能:用户注册
 * @program:ssoserver
 * @author:peicc
 * @create:2020-02-11 20:07:59
 **/
@Controller
public class RegisterController {
    private Logger LOGGER= LoggerFactory.getLogger(getClass());
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/regist")
    @ResponseBody
    public String registUser(@ModelAttribute  User user){
        LOGGER.info("当前用户注册信息为："+user);
        userMapper.addUser(user);
        return "注册成功";
    }
}
