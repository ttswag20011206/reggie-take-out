package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.mapper.SetmealMapper;
import com.zkx.reggie.pojo.Setmeal;
import com.zkx.reggie.pojo.SetmealDish;
import com.zkx.reggie.pojo.SetmealDto;
import com.zkx.reggie.service.SetmealDishService;
import com.zkx.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    SetmealDishService setmealDishService;

    //保存套餐，再套餐表中添加数据，再套餐菜品表中添加数据
    @Override
    public void saveWithDish( SetmealDto setmealDto) {

        //先保存套餐表
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 再保存套餐菜品表

        setmealDishService.saveBatch(setmealDishes);

    }
}
