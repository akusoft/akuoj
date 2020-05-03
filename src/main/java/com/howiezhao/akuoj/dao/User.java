package com.howiezhao.akuoj.dao;

/**
 * @author LiMing
 * @since 2020/4/28 17:03
 **/
public class User {
    private String name;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + password +
                '}';
    }
}
