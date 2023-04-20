package com.hao.digitalsignature.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;

public class User {
    @TableId(type = IdType.AUTO)
    private Integer user_id;
    private String user_name;
    private String password;
    private String realname;
    private String email;
    private String telephone;
    private String country;

    @TableField(exist = false)
    private List<Files> files;

    public User(Integer user_id, String user_name, String password, String realname, String email, String telephone, String country) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
        this.realname = realname;
        this.email = email;
        this.telephone = telephone;
        this.country = country;

    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", country='" + country + '\'' +
                ", files=" + files +
                '}';
    }
}
