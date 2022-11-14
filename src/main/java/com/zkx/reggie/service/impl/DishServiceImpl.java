package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.mapper.DishMapper;
import com.zkx.reggie.pojo.Dish;
import com.zkx.reggie.pojo.DishDto;
import com.zkx.reggie.pojo.DishFlavor;
import com.zkx.reggie.service.DishFlavorService;
import com.zkx.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {

        //先保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long id = dishDto.getId();//这个菜的id
        //把这个菜的口味添加到口味表中

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto returnDisDto(Long id) {
        //回显dish
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> flavors = dishFlavorService.list(wrapper);

        dishDto.setFlavors(flavors);


        return dishDto;
    }

    @Override
    public void updateDish(DishDto dishDto) {

        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        Long id = dishDto.getId();

        flavors = flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

  /*  @Override
    public void deleteDish(Long ids) {

        //先删除dish信息
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getId,ids);

        this.remove(dishLambdaQueryWrapper);

        //再删除口味表中的数据

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,ids);

        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

    }*/
}
