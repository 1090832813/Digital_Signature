package com.hao.digitalsignature.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName(value = "downpicture")
public class Download {
    private Integer user_id;
    private String picture_realname;
    private String aes;

    public Download(Integer user_id, String picture_realname, String aes) {
        this.user_id = user_id;
        this.picture_realname = picture_realname;
        this.aes = aes;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPicture_realname() {
        return picture_realname;
    }

    public void setPicture_realname(String picture_realname) {
        this.picture_realname = picture_realname;
    }

    public String getaes() {
        return aes;
    }

    public void setaes(String aes) {
        this.aes = aes;
    }

    @Override
    public String toString() {
        return "Download{" +
                "user_id=" + user_id +
                ", picture_realname='" + picture_realname + '\'' +
                ", aes='" + aes + '\'' +
                '}';
    }
}
