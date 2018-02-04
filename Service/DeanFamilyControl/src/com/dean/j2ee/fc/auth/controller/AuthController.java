package com.dean.j2ee.fc.auth.controller;

import com.dean.j2ee.fc.auth.service.AuthService;
import com.dean.j2ee.framework.controller.ConvenientController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户认证控制器
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController extends ConvenientController {

    @Autowired
    private AuthService authService;

    /**
     * 用户认证登陆
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object login(@PathVariable String username, @PathVariable String password) {
        return authService.login(username, password);
    }

}
