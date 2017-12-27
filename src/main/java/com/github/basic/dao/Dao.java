package com.github.basic.dao;

import com.github.basic.annotation.Sql;

/**
 *
 * Created by daiwei on 2017/12/27.
 */
public interface Dao {
    @Sql("select * from t_task where id = {0}")
    void findById(int id);
}
