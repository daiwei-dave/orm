package com.github;


import com.github.verso.VDataSource;
import com.github.verso.session.Session;
import com.github.verso.session.VSession;

import javax.sql.DataSource;

public class MyTest1 {
    private static String driverClassName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/orm?useUnicode=true&characterEncoding=utf8";

    public static void main(String args[]) throws Exception {
        DataSource data = new VDataSource(driverClassName, url, "root", "123456");
        Session session = new VSession(data);
        session.exec("select * from t_task where id=8");
    }
}
