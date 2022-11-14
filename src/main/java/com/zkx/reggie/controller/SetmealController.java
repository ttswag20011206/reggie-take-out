package com.zkx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.*;
import com.zkx.reggie.service.CategoryService;
import com.zkx.reggie.service.SetmealDishService;
import com.zkx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    CategoryService categoryService;


    @PostMapping
    public R<String> saveWithDish(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);

        return R.success("操作成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page<Setmeal> pageInfo = new Page<>();

        Page<SetmealDto> setmealDtoPage = new Page<>();


        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByDesc(Setmeal::getPrice);

        setmealService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"recodes");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String name = category.getName();
                setmealDto.setCategoryName(name);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);

    }

    @GetMapping("/list")
    public R<List<Setmeal>> list (Setmeal setmeal){



        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());

        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());

        List<Setmeal> list = setmealService.list(wrapper);

        return R.success(list);
    }
}
