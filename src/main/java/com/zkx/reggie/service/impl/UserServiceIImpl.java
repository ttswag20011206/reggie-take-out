package com.zkx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkx.reggie.mapper.UserMapper;
import com.zkx.reggie.pojo.User;
import com.zkx.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
