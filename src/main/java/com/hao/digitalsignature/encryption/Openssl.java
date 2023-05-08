package com.hao.digitalsignature.encryption;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Openssl {
    public static synchronized Map<String, String> generatePkcs1KeyPairMap(String name) throws IOException {
        File file = new File("C:\\ProgramData\\rsk_key");
        file.mkdirs();
        Map<String, String> keyPairMap = new HashMap<>(2);
        String filePath = "C:\\ProgramData\\rsk_key\\";
//        if(!filePath.endsWith(File.separator)){
//            filePath += File.separator;
//        }
        String privateCmd="openssl genrsa -out "+filePath+name+"_rsa_sk.pem 2048";
        String publicCmd="openssl  rsa -in  "+filePath+name+"_rsa_sk.pem -pubout -out "+filePath+name+"_rsa_pk.pem";
        String privateCmdPkcs8="openssl pkcs8 -topk8 -inform PEM -in "+filePath+name+"_rsa_sk.pem -outform PEM -nocrypt -out "+filePath+name+"_pkcs8_rsa_sk.pem";
        Map<String,String> map = new HashMap<>();
        map.put("privateKey",privateCmd);
        map.put("publicKey",publicCmd);
        map.put("privateKeyPkcs8",privateCmdPkcs8);
        String finalFilePath = filePath;

        map.forEach((k, v)->{
            Process p = null;
            try
            {
                //log.info("执行命令:"+v);
                System.out.println("执行命令:"+v);
                String[] command = { "cmd", "/c", v };
                p = Runtime.getRuntime().exec(command);

                ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
                InputStream errorInStream = new BufferedInputStream(p.getErrorStream());
                InputStream processInStream = new BufferedInputStream(p.getInputStream());

                int num = 0;
                byte[] bs = new byte[1024];
                while ((num = errorInStream.read(bs)) != -1) {
                    resultOutStream.write(bs, 0, num);
                }
                while ((num = processInStream.read(bs)) != -1) {
                    resultOutStream.write(bs, 0, num);
                }
                String result = new String(resultOutStream.toByteArray(), "gbk");
                //log.info("执行结果:"+result);
                System.out.println("执行结果:"+result);
                errorInStream.close();
                processInStream.close();
                resultOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (p != null) {
                    p.destroy();
                }
                ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
                BufferedInputStream fs  = null;
                try {
                    fs = new BufferedInputStream(new FileInputStream(new File(finalFilePath +k+".pem")));
                    int num = 0;
                    byte[] bs = new byte[1024];
                    while ((num = fs.read(bs)) != -1) {
                        resultOutStream.write(bs, 0, num);
                    }
                    String result = new String(resultOutStream.toByteArray(), "gbk");
                    keyPairMap.put(k, result);
                } catch (FileNotFoundException e) {
                    //log.error(e.getMessage(),e);
                } catch (UnsupportedEncodingException e) {

                    //log.error(e.getMessage(),e);
                } catch (IOException e) {
                    //log.error(e.getMessage(),e);
                }finally {
                    if(resultOutStream != null){
                        try {
                            resultOutStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fs != null){
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return keyPairMap;
    }
    public static void main(String args[]) throws IOException {
        generatePkcs1KeyPairMap("1090832813@qq.com") ;
    }
}
