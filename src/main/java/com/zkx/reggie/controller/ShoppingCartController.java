package com.zkx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.reggie.commons.BaseContext;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.ShoppingCart;
import com.zkx.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        Long currentId = BaseContext.getCurrentId();

        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ShoppingCart::getUserId,currentId);

        //先判断加入的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        //添加的是菜品
        if(dishId!=null){
          wrapper.eq(ShoppingCart::getDishId,dishId);
        }
        //添加的是套餐
        else{
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart dish = shoppingCartService.getOne(wrapper);

        if(dish!=null){
            Integer number = dish.getNumber();
            dish.setNumber(number+1);
            shoppingCartService.updateById(dish);
        }else{
            dish.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            dish =  shoppingCart;
        }

        return R.success(dish);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list (){

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();


        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        wrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        return R.success(list);
    }
}
