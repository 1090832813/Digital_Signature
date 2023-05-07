package com.hao.digitalsignature.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;

public class User {
    @TableId(type = IdType.AUTO)
    private Integer user_id;
    private String password;
    private String email;
    private String user_name;

    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private String realname;
    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private String telephone;
    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private String country;
    private String dsakey;


    @TableField(exist = false)
    private List<Files> files;
    @TableField(exist = false)
    private String passwordAgain;

    public User(Integer user_id, String password, String email, String user_name, String realname, String telephone, String country, String dsakey) {
        this.user_id = user_id;
        this.password = password;
        this.email = email;
        this.user_name = user_name;
        this.realname = realname;
        this.telephone = telephone;
        this.country = country;
        this.dsakey = dsakey;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getDsakey() {
        return dsakey;
    }

    public void setDsakey(String dsakey) {
        this.dsakey = dsakey;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", user_name='" + user_name + '\'' +
                ", realname='" + realname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", country='" + country + '\'' +
                ", dsakey='" + dsakey + '\'' +
                ", files=" + files +
                ", passwordAgain='" + passwordAgain + '\'' +
                '}';
    }
}
