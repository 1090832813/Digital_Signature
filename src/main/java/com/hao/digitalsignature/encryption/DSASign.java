package com.hao.digitalsignature.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import com.hao.digitalsignature.encryption.RSAEncrypt;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DSASign {
    public BigInteger p,q,g;
    public BigInteger x,y;
//160位随机数
    public BigInteger _randomInZq(){
        BigInteger r= null;
        do {

            r = new BigInteger(160, new SecureRandom());
        }while(r.compareTo(q) >=0);

        return r;
    }
    //SHA1 Hasn函数
    public BigInteger _hashInZq(byte m[]){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(m);
            byte b[] = new byte[17];
            System.arraycopy(md.digest(), 0, b, 1, 16);
            return new BigInteger(b);
        }catch (NoSuchAlgorithmException e){
            System.out.print("This cannot happen!");
        }
        return null;
    }
    //生成公钥私钥
    public void initKeys(){
        q = new BigInteger(160, 100, new SecureRandom());
        // 判定100%为素数
        do {
            BigInteger t = new BigInteger(512, new SecureRandom());
            p = t.multiply(q).add(BigInteger.ONE);

        }while(!p.isProbablePrime(100));

        BigInteger h = _randomInZq();
        g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        x = _randomInZq();
        y = g.modPow(x, p);
        System.out.println("p : " + p);
        System.out.println("q : " + q);
        System.out.println("g : " + g);
        System.out.println("发送方私钥x : " + x);
        System.out.println("发送方公钥y : " + y);
    }
    //产生签名
    public BigInteger[] signature(byte m[]){
        BigInteger k = _randomInZq();
        BigInteger sig[] = new BigInteger[2];
        //得到r
        sig[0] = g.modPow(k, p).mod(q);
        //得到S
        sig[1] = _hashInZq(m).add(x.multiply(sig[0])).mod(q)
                .multiply(k.modInverse(q)).mod(q);
        return sig;
    }
    //验证
    public boolean verify(byte m[], BigInteger sig[]){
        BigInteger w = sig[1].modInverse(q);
        BigInteger u1 = _hashInZq(m).multiply(w).mod(q);
        BigInteger u2 = sig[0].multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
        System.out.println("验证：");
        System.out.println("v = " + v);
        System.out.println("r = " + sig[0]);
        return v.compareTo(sig[0]) == 0;
    }

    public static void main(String args[]) throws Exception {
        String publicPath = "C:\\Users\\10908"; //公匙存放位置
        String privatePath = "C:\\Users\\10908"; //私匙存放位置
        BigInteger back[]=new BigInteger[2];
        int j=0;
        Base64 base64 = new Base64();
        DSASign dsa = new DSASign();

        //生成公钥和私钥
        dsa.initKeys();
        System.out.println("");
        //要签名的数据，传入AES加密后的内容
        String message = "NUJCQkJEQjRCRDBFODMwRTJCODkyOTQ1N0Y4NkEyNzU=";
        System.out.println("签名的数据："+message);
        BigInteger sig[] = dsa.signature(message.getBytes());

        //消息摘要
        System.out.println("消息摘要："+dsa._hashInZq(message.getBytes())+"\n");

        //对消息摘要进行签名得到r和s,在使用RSA进行加密并发送
        for (int i =0;i< sig.length;i++){
        System.out.println("签名:"+sig[i].toString());
         back[i]= new BigInteger(RSAEncrypt.RSA(sig[i].toString()));
        }
        System.out.println("\n需要解密的信息:"+back[0]);
        System.out.println("           "+back[1]);

        System.out.println("验证结果:" + dsa.verify(message.getBytes(),back) );
    }

}
