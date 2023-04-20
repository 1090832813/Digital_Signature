package com.hao.digitalsignature.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.UUID;

@RequestMapping("/file")
@RestController
public class FileDealController {
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
    public static void downFile(@RequestBody String fileName, HttpServletRequest request,
                                HttpServletResponse response) {
        // 得到要下载的文件名
        //String fileName = filename.substring(4);

        fileName = fileName.substring(0,fileName.length()-1);
//        fileName="666.jpg";

        try {
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
            // 设置响应头，控制浏览器下载该文件
           // response.setContentType("image/png");
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

}
