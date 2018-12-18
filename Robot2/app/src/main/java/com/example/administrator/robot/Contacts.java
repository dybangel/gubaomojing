package com.example.administrator.robot;

import org.litepal.crud.DataSupport;

/**
 * Created by xc on 2018-06-06.
 */

public class Contacts extends DataSupport{
    private int id;
    private String name;
    private String phone;
    private String sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}