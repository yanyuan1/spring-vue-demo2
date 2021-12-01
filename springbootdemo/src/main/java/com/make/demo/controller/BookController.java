package com.make.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.make.demo.common.Result;
import com.make.demo.entity.Book;
import com.make.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author java
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookMapper BookMapper;




    @PostMapping
    public Result<?> save(@RequestBody Book book){

        BookMapper.insert(book);
        return Result.success();
    }

    @RequestMapping(method=RequestMethod.PUT)
    public Result<?> update(@RequestBody Book book) {
        BookMapper.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        BookMapper.deleteById(id);
        return Result.success();
    }




    @GetMapping
    public Result<?> findPage(@RequestParam (defaultValue = "1")Integer pageNum,
                              @RequestParam (defaultValue = "10")Integer pageSize,
                              @RequestParam (defaultValue = "")String search) {


        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Book::getName,search);
        }
        Page<Book> BookPage = BookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        //这两行完全可用
        //  Page<Book> BookPage = BookMapper.selectPage(new Page<>(pageNum, pageSize),
        //       Wrappers.<Book>lambdaQuery().like(Book::getNickname, search));


        return Result.success(BookPage);

    }
}
