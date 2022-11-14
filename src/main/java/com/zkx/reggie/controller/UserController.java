package com.zkx.reggie.controller;


import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.User;
import com.zkx.reggie.service.UserService;

import com.zkx.reggie.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info(code);

         /*   SMSUtils.sendMessage();*/

            session.setAttribute(phone,code);
        }


        return R.success("success");
    }
}
