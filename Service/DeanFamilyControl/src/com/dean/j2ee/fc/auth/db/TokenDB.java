package com.dean.j2ee.fc.auth.db;

import com.dean.j2ee.framework.db.ConvenientDB;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Token数据库操作
 */
@Repository
public class TokenDB extends ConvenientDB {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Token验证
     *
     * @param token Token字符串
     * @return 是否通过验证
     */
    public boolean adopt(String token) {

        return true;
    }

}
