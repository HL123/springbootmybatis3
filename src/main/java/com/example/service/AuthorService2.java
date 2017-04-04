package com.example.service;

import com.example.dao.AuthorMapper2;
import com.example.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * Created by huanglei on 17/4/4.
 */
@Service
@ComponentScan({"com.example.dao"})
@ComponentScan({"com.example.dao"})
public class AuthorService2 {

    @Autowired
    private AuthorMapper2 authorMapper2;

    public Author findAuthor(Long id) {
        return this.authorMapper2.findAuthor(id);
    }
}
