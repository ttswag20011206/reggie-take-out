package com.zkx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.Category;
import com.zkx.reggie.pojo.Dish;
import com.zkx.reggie.pojo.DishDto;
import com.zkx.reggie.pojo.DishFlavor;
import com.zkx.reggie.service.CategoryService;
import com.zkx.reggie.service.DishDtoService;
import com.zkx.reggie.service.DishFlavorService;
import com.zkx.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);

        return R.success("添加成功!");
    }

    @GetMapping("/page")

    public R<Page> page(int page, int pageSize,String name){

        Page<Dish> pageInfo = new Page<>();

        Page<DishDto> dishDtoPageInfo = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByDesc(Dish::getUpdateTime);

        wrapper.eq(name != null,Dish::getName,name);


        dishService.page(pageInfo,wrapper);

        //对象copy
        BeanUtils.copyProperties(pageInfo,dishDtoPageInfo,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> dishDtoList = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            dishDto.setCategoryName(category.getName());

            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoList);

        return  R.success(dishDtoPageInfo);

    }

    @GetMapping("/{id}")
    public R<DishDto> query(@PathVariable Long id){


        DishDto dishDto = dishService.returnDisDto(id);

        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateDish(dishDto);

        return R.success("修改成功");
    }

    @PostMapping("/status/0")
    public R<String> updateStatusStop(Long ids){
        Dish dish = dishService.getById(ids);

        dish.setStatus(0);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Dish::getId, ids);

        dishService.update(dish, lambdaQueryWrapper);

        return R.success("更新成功");
    }

    @PostMapping("/status/1")
    public R<String> updateStatusStart(Long ids){
        Dish dish=dishService.getById(ids);
        dish.setStatus(1);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, lambdaQueryWrapper);
        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> deleteDish(Long ids){
        //采用逻辑删除

        Dish dish = dishService.getById(ids);

        dish.setIsDeleted(1); // 1 表示逻辑已经删除

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Dish::getId,ids);

        dishService.update(dish,wrapper);

        return R.success("删除成功");
    }

   /*  @GetMapping("/list")
    public R<List<Dish>> dishList( Dish dish){


         LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

         wrapper.eq(Dish::getCategoryId,dish.getCategoryId());

         wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

         wrapper.eq(Dish::getStatus,"1");

         List<Dish> list = dishService.list(wrapper);

         return R.success(list);

    }*/


    @GetMapping("/list")
    public R<List<DishDto>> dishList( Dish dish) {

        LambdaQueryWrapper<DishDto> dishDtowrapper = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();

        dishWrapper.eq(Dish::getCategoryId,dish.getCategoryId());


        List<Dish> list = dishService.list(dishWrapper);


        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();


        List<DishDto> dtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long dishID = item.getId();

            Category category = categoryService.getById(dishID);

            if(category!=null){
                String name = category.getName();

                dishDto.setCategoryName(name);
            }

            dishFlavorWrapper.eq(DishFlavor::getDishId,dishID);

            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorWrapper);

            dishDto.setFlavors(flavors);

            return dishDto;

        }).collect(Collectors.toList());

        return R.success(dtoList);
    }
}
