package com.hao.digitalsignature.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName(value = "downpicture")
public class Download {
    private String picture_user;
    private String picture_realname;
    private String aes;

    public Download(String picture_user, String picture_realname, String aes) {
        this.picture_user = picture_user;
        this.picture_realname = picture_realname;
        this.aes = aes;
    }

    public String getPicture_user() {
        return picture_user;
    }

    public void setPicture_user(String user_id) {
        this.picture_user = user_id;
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
                "picture_user=" + picture_user +
                ", picture_realname='" + picture_realname + '\'' +
                ", aes='" + aes + '\'' +
                '}';
    }
}
