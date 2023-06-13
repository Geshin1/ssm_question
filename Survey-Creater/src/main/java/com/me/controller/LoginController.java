package com.me.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.me.dto.EmailMessage;
import com.me.vo.ReturnMessage;
import com.me.po.User;
import com.me.security.Md5Utils;
import com.me.security.TokenCreater;
import com.me.service.UserService;
import com.me.utils.CheckCodeUtills;
import com.me.utils.RedisUtil;
import com.me.utils.SendEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * @Description -> login and register page
 * @Author -> noname
 * @Data
 * @about ->
 **/

@Api(description = "登录API接口")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "登录", notes = "就登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    })
    @RequestMapping("/login")
    @ResponseBody
    public HashMap forLogin(@RequestParam("email")String email, @RequestParam("password")String password,HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        log.info(email);
        User user=userService.loginCheck(email,password);
        //此处应把HashMap改为VO类型返回
        HashMap<String,Object> hashMap=new HashMap<>(16);

        if (user==null){
            hashMap.put("success",true);
            hashMap.put("islogin",false);
            hashMap.put("ismanager",false);
            return hashMap;
        }
        hashMap.put("success",true);
        hashMap.put("islogin",true);
        hashMap.put("ismanager",user.getIdentify().equals(0));
        hashMap.put("email",user.getEmail());
        hashMap.put("name",user.getName());
        session.setAttribute("userid",user.getId());

        //创建token返回并存入redis数据库，设置过期时间为1天
        String token = TokenCreater.createJWT(email,user.getPassword(),user.getName(),86400000);
        redisUtil.set(email,token,86400);

        hashMap.put("token",token);
        return hashMap;
        /*writer.write("success!!");*/
    }


}
