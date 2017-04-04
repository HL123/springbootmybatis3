package com.example.controller;

import com.example.domain.Author;
import com.example.service.AuthorService2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huanglei on 17/4/4.
 */
@RestController
@RequestMapping(value="/data/mybatis/author2")
@MapperScan("com.example.dao")
public class AuthorController2 {

    @Autowired
    private AuthorService2 authorService2;
    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/{userId:\\d+}", method = RequestMethod.GET)
    public Author getAuthor(@PathVariable Long userId, HttpServletRequest request) {
        Author author = this.authorService2.findAuthor(userId);
        if(author == null){
            throw new RuntimeException("查询错误");
        }
        return author;
    }
}
