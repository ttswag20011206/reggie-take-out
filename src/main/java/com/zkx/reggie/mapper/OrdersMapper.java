package com.zkx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkx.reggie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
