package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)    //指定当前注解可以用在哪些地方
@Retention(RetentionPolicy.RUNTIME)  //用于描述这个注解什么时候生效
public @interface AutoFill {   // 自定义注解
    // 数据库操作类型
    OperationType value();  // OperationType是一个枚举类，枚举了UPDATE和INSERT两个操作
}
