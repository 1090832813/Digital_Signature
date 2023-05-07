package com.hao.digitalsignature.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("picture")

public class Files {
    @TableId(type = IdType.AUTO)

    private Integer picture_id;
    private String picture_name;
    private String picture_type;
    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private String picture_user;
    private String createtime;
    private String picture_realname;
    private String dig;

    public Files(Integer picture_id, String picture_name, String picture_type, String picture_user, String createtime, String picture_realname, String dig) {
        this.picture_id = picture_id;
        this.picture_name = picture_name;
        this.picture_type = picture_type;
        this.picture_user = picture_user;
        this.createtime = createtime;
        this.picture_realname = picture_realname;
        this.dig = dig;
    }

    public Integer getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(Integer picture_id) {
        this.picture_id = picture_id;
    }

    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getPicture_type() {
        return picture_type;
    }

    public void setPicture_type(String picture_type) {
        this.picture_type = picture_type;
    }

    public String getPicture_user() {
        return picture_user;
    }

    public void setPicture_user(String picture_user) {
        this.picture_user = picture_user;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPicture_realname() {
        return picture_realname;
    }

    public void setPicture_realname(String picture_realname) {
        this.picture_realname = picture_realname;
    }

    public String getDig() {
        return dig;
    }

    public void setDig(String dig) {
        this.dig = dig;
    }

    @Override
    public String toString() {
        return "Files{" +
                "picture_id=" + picture_id +
                ", picture_name='" + picture_name + '\'' +
                ", picture_type='" + picture_type + '\'' +
                ", picture_user=" + picture_user +
                ", createtime='" + createtime + '\'' +
                ", picture_realname='" + picture_realname + '\'' +
                ", dig='" + dig + '\'' +
                '}';
    }
}
