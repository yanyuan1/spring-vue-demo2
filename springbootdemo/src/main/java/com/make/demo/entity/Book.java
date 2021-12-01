package com.make.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author java
 */
@TableName("book")
@Data
@NoArgsConstructor
public class Book {
    @TableId( type = IdType.AUTO)
    private int id ;
    private String name ;
    private BigDecimal price ;
    private String author ;
   // @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    private String cover;

}
