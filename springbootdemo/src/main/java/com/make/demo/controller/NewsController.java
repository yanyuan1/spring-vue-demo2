package com.make.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.make.demo.common.Result;
import com.make.demo.entity.News;
import com.make.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author java
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsMapper newsMapper;




    @PostMapping
    public Result<?> save(@RequestBody News news){
        news.setTime(new Date());
        newsMapper.insert(news);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody News news) {
        newsMapper.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        newsMapper.deleteById(id);
        return Result.success();
    }




    @GetMapping
    public Result<?> findPage(@RequestParam (defaultValue = "1")Integer pageNum,
                              @RequestParam (defaultValue = "10")Integer pageSize,
                              @RequestParam (defaultValue = "")String search) {


        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(News::getTitle,search);
        }
        Page<News> newsPage = newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        //这两行完全可用
        //  Page<News> NewsPage = NewsMapper.selectPage(new Page<>(pageNum, pageSize),
        //       Wrappers.<News>lambdaQuery().like(News::getNickname, search));


        return Result.success(newsPage);

    }
}
