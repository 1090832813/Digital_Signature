package com.hao.digitalsignature.controller;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.digitalsignature.encryption.AES1;
import com.hao.digitalsignature.encryption.AESmiyao;
import com.hao.digitalsignature.encryption.DownloadMsg;
import com.hao.digitalsignature.encryption.RSAEncrypt;
import com.hao.digitalsignature.entity.Download;
import com.hao.digitalsignature.entity.Files;
import com.hao.digitalsignature.entity.User;
import com.hao.digitalsignature.mapper.DownloadMapper;
import com.hao.digitalsignature.mapper.FileMapper;
import com.hao.digitalsignature.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.*;

@RequestMapping("/file")
@RestController
public class FileDealController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DownloadMapper downloadMapper;

    @RequestMapping(value = "upload")
    public String upload(@RequestParam("file") MultipartFile pic) throws SocketException, IOException {
        String newName = "";
        if (null!=pic&&pic.getSize()>0){//上传了图片
            //获取文件的原始名称
            String oname=pic.getOriginalFilename();//例如:121.jpg
            //获取文件的后缀名
            String suffix=oname.substring(oname.lastIndexOf("."));  //  .jpg
            //新的文件名
            newName= UUID.randomUUID()+suffix;


            try {
                // 获取当前项目下路径：方式一
                File file = new File("");
                String filePath = file.getCanonicalPath();
//                pic.transferTo(new File("../pic",newName));
                pic.transferTo(new File(filePath+"\\src\\main\\resources\\static\\img",newName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newName;
    }

    /**
     * 下载
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    @RequestMapping(value = "download")
    public void downFile(@RequestBody String fileName, HttpServletRequest request,
                                HttpServletResponse response) {
        // 得到要下载的文件名
        fileName = fileName.substring(0,fileName.length()-1);
        try {

            //下载图片
            File filep = new File("");
            String filePath = filep.getCanonicalPath();
            // 上传位置
            String fileSaveRootPath =filePath+"\\src\\main\\resources\\static\\img";

            System.out.println(fileSaveRootPath + "\\" + fileName);
            // 得到要下载的文件
            File file = new File(fileSaveRootPath + "\\" + fileName);

            // 如果文件不存在
            if (!file.exists()) {
                request.setAttribute("message", "您要下载的资源已被删除！！");
                System.out.println("您要下载的资源已被删除！！");
                return ;
            }
            // 处理文件名
            String realname = fileName.substring(fileName.indexOf("_") + 1);

            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(realname, "UTF-8"));
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(fileSaveRootPath + "\\" + fileName);
           // response.setHeader("Location",);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件输入流
            in.close();
            // 关闭输出流
            out.close();

        } catch (Exception e) {
            System.out.println("error");
        }
    }


    @RequestMapping(value = "/downloadMsg")
    public void exportKtrAndKjb(@RequestBody String fileNames, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileNames = fileNames.substring(1,fileNames.length()-1);
        System.out.println(fileNames);
        String fileName=fileNames.split(";")[0];
        String downloadUser = fileNames.split(";")[1];
        System.out.println(fileName);
        System.out.println(downloadUser);
        //获取摘要
        try {

        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.eq("picture_realname", fileName.split("\\.")[0]);
        Files files = fileMapper.selectOne(wrapper);
        System.out.println("文件对象："+files);
        System.out.println("摘要："+files.getDig().split(";")[2]);

        List<String> dig=new ArrayList<String>(Arrays.asList(files.getDig().split(";")));
        dig.add(files.getCreatetime());
        String[] dig1=new String[dig.size()];
        dig.toArray(dig1);
        for(int i=0;i<dig1.length;i++)
            System.out.println("正确签名和时间戳"+dig1[i]);
        //生成AES密钥
        String aesPassword =AESmiyao.getKey();
        if(files.getPicture_realname()!=null){
            Download download=new Download(files.getPicture_user(),files.getPicture_realname(),aesPassword);
            System.out.println(download);
            downloadMapper.insert(download);
            System.out.println("成功");
        }else{
            System.out.println("有了");
        }
        //AES加密
        String encryContent= AES1.encryptAES(files.getDig().split(";")[2],aesPassword);
        //对称加密后的签名
        dig1[2]=encryContent;
        for(int i=0;i<dig1.length;i++)
            System.out.println("加密摘要的签名和时间戳"+dig1[i]);
        //发送RSA
        System.out.println(Arrays.toString(dig1));
        String rsaMsg=new String();
        for(int i=0;i<dig1.length;i++){
            rsaMsg+=dig1[i];
            rsaMsg+=";";
        }
            System.out.println(rsaMsg);
        byte[] rsaEnf=RSAEncrypt.RSAen(rsaMsg.substring(0,rsaMsg.length()/2),downloadUser);
        byte[] rsaEns=RSAEncrypt.RSAen(rsaMsg.substring(rsaMsg.length()/2),downloadUser);
        Base64 base64 = new Base64();
        String cipherf = new String(base64.encode(rsaEnf));
        String ciphers = new String(base64.encode(rsaEns));


        //RSA解密
        //String rsaDef=RSAEncrypt.RSAde(base64.decode(cipherf),files.getPicture_user());
       // String rsaDes=RSAEncrypt.RSAde(base64.decode(ciphers),files.getPicture_user());
        String recRes=cipherf+";"+ciphers;

       DownloadMsg.downloadByStringContent(request, response, fileName.split("\\.")[0], recRes);

        }catch (Exception e) {
            System.out.println("error");
        }
    }

}
