package com.dean.j2ee.fc.auth.db;

import com.dean.j2ee.fc.auth.model.AuthEntity;
import com.dean.j2ee.framework.db.ConvenientDB;
import com.dean.j2ee.framework.utils.TextUils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 用户认证 数据库操作
 */
@Repository
public class AuthDB extends ConvenientDB {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存指定用户认证数据->DB
     *
     * @param authEntity
     */
    public void saveOrUpdate(AuthEntity authEntity) {
        if (authEntity != null)
            saveOrUpdate(sessionFactory, authEntity);
    }

    /**
     * 通过用户名/密码/用户名和密码 查找指定用户认证数据
     *
     * @param params
     * @return
     */
    public AuthEntity find(String... params) {
        AuthEntity authEntity = null;

        String username, password = null;

        if (params != null) {
            username = params[0];
            if (params.length > 1)
                password = params[1];

            Map<String, Object> dbParams = getParamMap();
            dbParams.put("username", username);
            if (!TextUils.isEmpty(password))
                dbParams.put("password", password);

            authEntity = find(sessionFactory, AuthEntity.class, dbParams);
        }

        return authEntity;
    }

}
