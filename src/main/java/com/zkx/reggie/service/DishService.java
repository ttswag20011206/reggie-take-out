package com.zkx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkx.reggie.pojo.Dish;
import com.zkx.reggie.pojo.DishDto;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto returnDisDto(Long id);

    void updateDish(DishDto dishDto);

    /*void deleteDish(Long ids);*/

}
