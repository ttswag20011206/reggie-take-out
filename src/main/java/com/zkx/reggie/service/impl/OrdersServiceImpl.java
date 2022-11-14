package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.commons.BaseContext;
import com.zkx.reggie.commons.CustomException;
import com.zkx.reggie.mapper.OrdersMapper;
import com.zkx.reggie.pojo.AddressBook;
import com.zkx.reggie.pojo.OrderDetail;
import com.zkx.reggie.pojo.Orders;
import com.zkx.reggie.pojo.ShoppingCart;
import com.zkx.reggie.service.AddressBookService;
import com.zkx.reggie.service.OrderDetailService;
import com.zkx.reggie.service.OrdersService;
import com.zkx.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service

public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    OrderDetailService orderDetailService;
    @Override
    @Transactional
    public void submit(Orders orders) {

        //获取用户的id
            Long userId = BaseContext.getCurrentId();
        //根据用户的id查询购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        //获取用户的地址
        Long addressBookId = orders.getAddressBookId();
        AddressBook address = addressBookService.getById(addressBookId);

        if(address==null){
            throw new CustomException("用户地址异常不能下单");
        }

        Long orderId = IdWorker.getId();
        //保证在多线程的情况下计算正常
        AtomicInteger amount = new AtomicInteger();

        List<OrderDetail> list = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setName(item.getName());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        //向orders表中插入一条数据
        orders.setUserId(userId);
        orders.setStatus(2);
        orders.setId(orders.getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setNumber(String.valueOf(orderId));
        orders.setPhone(address.getPhone());
        orders.setConsignee(address.getConsignee());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAddress(
                address.getProvinceName()==null? "": address.getProvinceName()
                +(address.getCityName()==null? "":address.getCityName())
                + address.getDistrictName()==null?"":address.getDistrictName()
    );
        this.save(orders);


        orders.setOrderTime(LocalDateTime.now());

        //向orderDetail表中插入多条数据
        orderDetailService.saveBatch(list);
        //清楚购物车

        shoppingCartService.remove(wrapper);


    }
}
