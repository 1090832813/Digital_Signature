package com.hao.digitalsignature.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.digitalsignature.encryption.DSASign;
import com.hao.digitalsignature.encryption.Openssl;
import com.hao.digitalsignature.entity.Files;
import com.hao.digitalsignature.entity.User;
import com.hao.digitalsignature.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/user/findAll")
    public List<User> findAll(){
        return  userMapper.selectList(null);
    }

    @PostMapping("/user/find")
    public User find(@RequestBody String email){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email.substring(1,email.length()-1));
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @PostMapping("/user/save")
    public String save(@RequestBody User user) throws IOException {
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
        Openssl openssl=new Openssl();
        openssl.generatePkcs1KeyPairMap(user.getEmail());
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

    @PostMapping("/user/update")
    public String update(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail()).ne("user_id",user.getUser_id());
        User exUser = userMapper.selectOne(wrapper);
        if (exUser==null){
            int i = userMapper.updateById(user);
            if(i>0)
                return "success";
            else
                return "fail";
        }
        else{
           return "exist";
        }
    }
    @PostMapping("/user/delete")
    public String delete(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", user.getEmail());
        int i = userMapper.delete(wrapper);
        if (i>0)
            return "success";
        else
            return "fail";
    }
}
