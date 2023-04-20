package com.hao.digitalsignature.controller;

import com.hao.digitalsignature.entity.User;
import com.hao.digitalsignature.mapper.UserMapper;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

//    @GetMapping("/user/{id}")
//    public String getUserById(@PathVariable int id){
//        System.out.println(id);
//        return "获取用户信息";
//    }
//    @PostMapping
//    public String save(User user){return "添加";}
//    @PutMapping
//    public String update(User user){return "更新";}
//    @DeleteMapping
//    public String deleteById(@PathVariable int id){
//        System.out.println(id);
//        return "删除";
//    }
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
    public String save(User user){
        int i = userMapper.insert(user);
        if (i>0)
            return "success";
        else
            return "fail";
    }
    @PutMapping("/user/update")
    public String update(int id,String name){
        User user = userMapper.selectById(id);
        if (user!=null){
            user.setUser_name(name);
            userMapper.updateById(user);
            return "success";
        }
        else
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
