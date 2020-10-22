package com.bailihui.shop.controller;

import com.bailihui.shop.dto.Result;
import com.bailihui.shop.pojo.TbCategory;
import com.bailihui.shop.service.TbCategoryService;
import com.bailihui.shop.util.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 21:33
 */
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private TbCategoryService tbCategoryService;

    @PostMapping("/add")
    public Result addCategory(@RequestBody TbCategory tbCategory) {
        tbCategoryService.insertCategory(tbCategory);
        return new Result<>(true, "SUCCESS", null);
    }

    @GetMapping("/{id}")
    public Result<TbCategory> findCategory(@PathVariable(name = "id") Integer id) {
        TbCategory tbCategory = tbCategoryService.selectCategoryById(id);
        return new Result<>(true, "SUCCESS", tbCategory);
    }

    @GetMapping("/all")
    public Result<List<TbCategory>> findAll() {
        List<TbCategory> tbCategories = tbCategoryService.selectAll();
//        Page<TbCategory> tbCategoryPage = new Page<>(tbCategories.size(), 1, tbCategories);
        return new Result<>(true, "SUCCESS", tbCategories);
    }

    @PutMapping("/update")
    public Result updateCategory(TbCategory tbCategory) {
        tbCategoryService.updateCategory(tbCategory);
        return new Result<>(true, "SUCCESS", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable(name = "id") Integer id) {
        tbCategoryService.deleteCategoryById(id);
        return new Result<>(true, "SUCCESS", null);
    }
}
