package com.zkx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkx.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
