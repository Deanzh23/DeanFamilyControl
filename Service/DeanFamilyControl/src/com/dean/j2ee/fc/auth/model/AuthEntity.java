package com.dean.j2ee.fc.auth.model;

import javax.persistence.*;

/**
 * 用户认证信息
 */
@Entity
@Table(name = "auth", schema = "fc_db")
public class AuthEntity {

    private String username;
    private String password;
    private Long loginDateTime;

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "loginDateTime")
    public Long getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(Long loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthEntity that = (AuthEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (loginDateTime != null ? !loginDateTime.equals(that.loginDateTime) : that.loginDateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (loginDateTime != null ? loginDateTime.hashCode() : 0);
        return result;
    }
}
