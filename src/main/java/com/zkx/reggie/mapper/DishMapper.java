package com.zkx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkx.reggie.pojo.Dish;
import com.zkx.reggie.pojo.DishDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper  extends BaseMapper<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto returnDisDto(Long id);
}
