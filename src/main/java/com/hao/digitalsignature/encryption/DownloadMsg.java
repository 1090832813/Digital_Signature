package com.hao.digitalsignature.encryption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public class DownloadMsg {

    public static void downloadByStringContent(HttpServletRequest request,
                                               HttpServletResponse response,
                                               String fileName, String content)
            throws IOException {
        //设置向浏览器端传送的文件格式
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            // firefox浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            // IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
            // 谷歌
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        BufferedInputStream inp = null;
        OutputStream out = response.getOutputStream();
        try {
            inp = new BufferedInputStream(new ByteArrayInputStream(content.getBytes("utf-8")));
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inp.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inp != null) {
                inp.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
