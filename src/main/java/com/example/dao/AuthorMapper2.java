package com.example.dao;

import com.example.domain.Author;
import org.apache.ibatis.annotations.Param;


/**
 * Created by huanglei on 17/4/4.
 */
public interface AuthorMapper2 {
    Author findAuthor(@Param("id") Long id);
}
