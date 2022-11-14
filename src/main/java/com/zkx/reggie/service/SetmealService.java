package com.zkx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkx.reggie.pojo.Setmeal;
import com.zkx.reggie.pojo.SetmealDto;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);
}
