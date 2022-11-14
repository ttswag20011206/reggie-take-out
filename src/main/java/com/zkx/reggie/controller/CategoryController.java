package com.zkx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.Category;
import com.zkx.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody Category category){
        System.out.println(1);
        try{
            categoryService.save(category);
            return R.success("增加成功");
        }
        catch (Exception exception){
            exception.printStackTrace();
            return R.error("失败");
        }
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,wrapper);

        return R.success(pageInfo);

    }

    @DeleteMapping()
    public R<String> delete( Long id){
            categoryService.remove(id);
            return R.success("删除成功");

    }

    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list( Category category){


        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();

        wrapper.eq(category.getType() != null,Category::getType,category.getType());

        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(wrapper);

        return R.success(list);
    }

}
