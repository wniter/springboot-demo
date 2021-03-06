package com.example.springboot.adminmanager.controller.admin;

import com.example.springboot.adminmanager.common.JsonResult;
import com.example.springboot.adminmanager.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController {
    @RequestMapping(value = {"/admin/login"}, method = RequestMethod.GET)
    public String login() {

        return "admin/login";
    }

    @RequestMapping(value = {"/admin/login"}, method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, ModelMap model
    ) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            return redirect("/admin/index");
        } catch (AuthenticationException e) {
            model.put("message", e.getMessage());
        }
        return "admin/login";
    }

    @RequestMapping(value = {"/user/login"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult loginRestful(@RequestParam("username") String username,
                            @RequestParam("password") String password
    ) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            return JsonResult.success("ok",token);
        } catch (AuthenticationException e) {
            return JsonResult.failure("??????????????????????????????");
        }
    }

    @RequestMapping(value = {"/admin/logout"}, method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return redirect("admin/login");
    }

}
