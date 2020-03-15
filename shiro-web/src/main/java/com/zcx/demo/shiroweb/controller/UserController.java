package com.zcx.demo.shiroweb.controller;

import com.zcx.demo.shiroweb.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @ResponseBody
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String login(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(user.isRememberMe());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "登录成功";
    }

//    @RequiresRoles("admin")
    @RequestMapping("testRole1")
    public String testRole1(){
        return "admin test1 success";
    }

//    @RequiresRoles("admin1")
    @RequestMapping("/testRole2")
    public String testRole2(){
        return "admin test2 success";
    }
    @RequiresPermissions({"",""})
    @RequestMapping("/testPerm1")
    public String testPerm1(){
        return "Perm test1 success";
    }
    @RequestMapping("/testPerm2")
    public String testPerm2(){
        return "perm test2 success";
    }
}
