package com.zkx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkx.reggie.pojo.DishDto;
import com.zkx.reggie.pojo.DishFlavor;
import com.zkx.reggie.service.DishFlavorService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DishDtoMapper extends BaseMapper<DishDto> {

    void save(DishDto dishDto);

}
