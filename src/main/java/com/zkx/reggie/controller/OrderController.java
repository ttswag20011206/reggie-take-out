package com.zkx.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.reggie.commons.CustomException;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.AddressBook;
import com.zkx.reggie.pojo.Orders;
import com.zkx.reggie.pojo.ShoppingCart;
import com.zkx.reggie.service.AddressBookService;
import com.zkx.reggie.service.OrdersService;
import com.zkx.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    AddressBookService addressBookService;

    @Autowired
    OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        ordersService.submit(orders);

        return R.success("操作成功");

    }

}
