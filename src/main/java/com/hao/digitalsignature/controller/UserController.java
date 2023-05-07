package com.hao.digitalsignature.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.digitalsignature.encryption.DSASign;
import com.hao.digitalsignature.entity.User;
import com.hao.digitalsignature.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/user/findAll")
    public List<User> find(){
        return  userMapper.selectAllUserAndFiles();
    }

    @GetMapping("/user/allUser")
    public List query() {
        List<User> list = userMapper.selectList(null);
        System.out.println(list);
        return list;
    }

    @PostMapping("/user/save")
    public String save(@RequestBody User user){
        System.out.println(user);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        User exUser = userMapper.selectOne(wrapper);

        if(exUser!=null){
            System.out.println("存在");
            System.out.println(exUser);
            return "exist";
        }
        System.out.println("不存在");
        DSASign dsaSign=new DSASign();
        String dsakey = dsaSign.initKeys();
        user.setDsakey(dsakey);
        int i = userMapper.insert(user);
        if (i>0)
            return "success";
        else
            return "fail";
    }

    @PostMapping("/user/login")
    public Object login(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        User exUser = userMapper.selectOne(wrapper);
        if(exUser==null){
            return "not_exist";
        }else{
            if(exUser.getPassword().equals( user.getPassword()))
                return exUser ;
            else
                return "password_error";
        }
    }

    @PutMapping("/user/update")
    public String update(@RequestBody User user){
//        User user = userMapper.selectById(id);
//        if (user!=null){
//            user.setUser_name(name);
//            userMapper.updateById(user);
//            return "success";
//        }
//        else
           return "fail";
    }
    @DeleteMapping("/user/delete")
    public String delete(int id){
        int i= userMapper.deleteById(id);
        if (i>0)
            return "success";
        else
            return "fail";
    }
}
