package com.zkx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkx.reggie.pojo.Orders;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
