package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.mapper.DishDtoMapper;
import com.zkx.reggie.pojo.DishDto;
import com.zkx.reggie.pojo.DishFlavor;
import com.zkx.reggie.service.DishDtoService;
import com.zkx.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishDtoServiceImpl extends ServiceImpl<DishDtoMapper, DishDto> implements DishDtoService {


}
