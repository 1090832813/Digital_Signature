package com.hao.digitalsignature.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.digitalsignature.encryption.DSASign;
import com.hao.digitalsignature.encryption.RSAEncrypt;
import com.hao.digitalsignature.entity.User;
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
import java.math.BigInteger;
import java.util.List;

@RestController
public class FilesController {

    @Autowired
    private FileMapper fileMapper;
//    @PostMapping("/upload")
//    public String up(String name, MultipartFile photo, HttpServletRequest request) throws IOException{
//        //自定文件名
//        System.out.println(name);
//        //文件原名
//        System.out.println(photo.getOriginalFilename());
//        //文件类型
//        System.out.println(photo.getContentType());
//
//        String path=request.getServletContext().getRealPath("/upload/");
//        System.out.println(path);
//        saveFile(photo,path);
//        return "上传成功";
//    }
//
//    public void saveFile(MultipartFile photo,String path) throws IOException{
//        File dir = new File(path);
//        if (!dir.exists()){
//            dir.mkdir();
//        }
//        File file = new File(path+photo.getOriginalFilename());
//        photo.transferTo(file);
//    }
    @GetMapping("file/search")
    public Object search(String name){

        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.eq("picture_realname", name);
        Files files = fileMapper.selectOne(wrapper);
        return files;
    }

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
    @GetMapping("/file/findAll")
    public List<Files> find(){
        return  fileMapper.selectList(null);
    }
}
