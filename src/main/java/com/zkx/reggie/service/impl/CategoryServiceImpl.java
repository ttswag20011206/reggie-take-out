package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.commons.CustomException;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.mapper.CategoryMapper;
import com.zkx.reggie.pojo.Category;
import com.zkx.reggie.pojo.Dish;
import com.zkx.reggie.pojo.Setmeal;
import com.zkx.reggie.service.CategoryService;
import com.zkx.reggie.service.DishService;
import com.zkx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    @Override
    public void remove(Long id) {
        //如果分类下有菜品不能删
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        int count = (int) dishService.count(dishLambdaQueryWrapper);
        log.info(String.valueOf(count));

        if(count>0){
           throw new CustomException("该分类下已经有菜");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count2 = (int) setmealService.count(setmealLambdaQueryWrapper);

        if(count2>0){
            throw new CustomException("该分类下已经有套餐");
        }

        super.removeById(id);
    }
}
