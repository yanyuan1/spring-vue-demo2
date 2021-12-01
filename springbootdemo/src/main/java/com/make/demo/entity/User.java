package com.make.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author java
 */
@TableName("user")
@Data
@NoArgsConstructor
public class User {
    @TableId( type = IdType.AUTO)
    private int id ;
    private String username ;
    private String password ;
    private String nickName ;
    private int  age;
    private String sex ;
    private String address;
}
