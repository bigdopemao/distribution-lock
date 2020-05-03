package com.mao;

import java.io.Serializable;

/**
 * @author bigdope
 * @create 2019-01-04
 **/
public class User implements Serializable {

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
