package com.example.springboot.adminmanager.controller.admin;

import com.example.springboot.adminmanager.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminIndexController extends BaseController {
    @RequestMapping(value = {"/admin/", "/admin/index"})
    public String index() {

        return "admin/index";
    }

    @RequestMapping(value = {"/admin/welcome"})
    public String welcome() {

        return "admin/welcome";
    }
}
