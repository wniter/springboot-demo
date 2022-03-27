package com.example.bookfeign.user;

import lombok.Data;

/**
 * @create 2022-03-08 22:40
 */
@Data
public class User {
    String name;
    Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" + "name:" + name + ",age:" + age + "}";
    }
}
