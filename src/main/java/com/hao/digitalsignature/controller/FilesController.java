package com.hao.digitalsignature.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.digitalsignature.encryption.AES1;
import com.hao.digitalsignature.encryption.DSASign;
import com.hao.digitalsignature.entity.Download;
import com.hao.digitalsignature.entity.User;
import com.hao.digitalsignature.mapper.DownloadMapper;
import com.hao.digitalsignature.mapper.UserMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.hao.digitalsignature.entity.Files;
import com.hao.digitalsignature.mapper.FileMapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;

import java.util.List;

@RestController
public class FilesController {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DownloadMapper downloadMapper;

    @PutMapping("/file/update")
    public String update(int id,String name){
        Files file = fileMapper.selectById(id);
        if (file!=null){
            file.setPicture_name(name);
            fileMapper.updateById(file);
            return "success";
        }
        else
            return "fail";
    }
    @PostMapping("/file/save")
    public String save(@RequestBody Files file){
        System.out.println(file);
        BigInteger back[]=new BigInteger[2];
        int j=0;
        Base64 base64 = new Base64();
        DSASign dsa = new DSASign();
        dsa.initKeys();
        //要签名的数据，传入AES加密后的内容
        String message = file.getPicture_realname();
        System.out.println("签名的数据："+message);
        BigInteger sig[] = dsa.signature(message.getBytes());
        String dig=sig[0]+";"+sig[1]+";"+dsa._hashInZq(message.getBytes());
        file.setDig(dig);
        int i = fileMapper.insert(file);
        if (i>0)
            return "success";
        else
            return "fail";
    }

    @PostMapping(value="/file/verify")
    public String verify(@RequestBody String obj){

      String[] str = obj.substring(1,obj.length()-1).split(";");
      for(int i =0;i<str.length;i++)
          System.out.println(str[i]);
        Files file = fileMapper.selectById(str[0]);
        System.out.println(file);
        if(file.getCreatetime().equals( str[4])){
            System.out.println("success");
            QueryWrapper<Download> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("picture_realname", file.getPicture_realname());
            Download download = downloadMapper.selectOne(queryWrapper);
            String aes =download.getaes();
            return aes;
        }else {
            System.out.println("failed");
        }
        return "0";
    }
    @PostMapping(value = "/file/encrypt")
    public String encrypt(@RequestBody String str){
        String[] strs =str.substring(1,str.length()-1).split(";");
        for(int i =0;i<strs.length;i++)
            System.out.println(strs[i]);
        try {
            String encryContent= AES1.decryptAES(strs[2],strs[4]);
            strs[2]=encryContent;
            String newStr =new String();
            for(int i =0;i<strs.length-3;i++) {
                newStr += strs[i];
                newStr += ";";
            }
            newStr=newStr.substring(0,newStr.length()-1);
            QueryWrapper<Files> wrapper = new QueryWrapper<>();
            wrapper.eq("picture_realname", strs[5]);
            Files files = fileMapper.selectOne(wrapper);
            System.out.println(files.getDig());
            System.out.println(newStr);
            if(files.getDig().equals(newStr)){
                return "success";
            }else {
                return "failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @GetMapping("/file/findAll")
    public List<Files> find(){
        return  fileMapper.selectList(null);
    }
}
