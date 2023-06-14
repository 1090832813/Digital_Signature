package com.hao.digitalsignature.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.Random;

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
    public String initKeys(){
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
        String dsakey = q+";"+p+";"+g+";"+x+";"+y;
        System.out.println("p : " + p);
        System.out.println("q : " + q);
        System.out.println("g : " + g);
        System.out.println("发送方私钥x : " + x);
        System.out.println("发送方公钥y : " + y);
        return dsakey;
    }
    //产生签名
    public BigInteger[] signature(byte m[] ,String dsakey){

        String str[] = dsakey.split(";");

        BigInteger q= new BigInteger(str[0]);
        BigInteger p= new BigInteger(str[1]);
        BigInteger g= new BigInteger(str[2]);
        BigInteger x= new BigInteger(str[3]);

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
    public boolean verify( String strsig,String key){
        String[] str = strsig.split(";");

        if(str[2].equals("null")){
            System.out.println("false");
            return false;
        }

        BigInteger[] sig = new BigInteger[2];
        sig[0]=new BigInteger(str[0]);
        sig[1]=new BigInteger(str[1]);
        BigInteger Hm= new BigInteger(str[2]);

        String strs[] = key.split(";");

        BigInteger q= new BigInteger(strs[0]);
        BigInteger p= new BigInteger(strs[1]);
        BigInteger g= new BigInteger(strs[2]);

        BigInteger y= new BigInteger(strs[4]);

        BigInteger w = sig[1].modInverse(q);
        BigInteger u1 = Hm.multiply(w).mod(q);
        BigInteger u2 = sig[0].multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

        return v.compareTo(sig[0]) == 0;
    }

//    public static void main(String[] args) {
//        do {
//            // 定义Nbites的值
//            int N = 16;
//            // 随机生成一个长度为Nbites的整数c
//            Random random = new Random();
//            int c = random.nextInt((int) Math.pow(2, N));
//            // 打印c的值
//            System.out.println("c = " + c);
//            // 将c拆分为两个长度为N/2bits的整数a和b
//            int a = c >> (N / 2); // 右移N/2位得到a
//            int b = c & ((1 << (N / 2)) - 1); // 与N/2位全1的掩码进行按位与运算得到b
//        }while(c == a * Math.pow(2, Math.abs(b)) + b));
//        // 打印a和b的值
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);
//        // 验证c是否等于a*2^|b|+b
//        System.out.println("c == a * Math.pow(2, Math.abs(b)) + b ? " + (c == a * Math.pow(2, Math.abs(b)) + b));
//
//    }
public static String generateCode(int Nbites, int count) {
    Random random = new Random();
    int c = random.nextInt((int)Math.pow(2, Nbites));
    int halfNbites = Nbites / 2;
    int a = c >>> halfNbites;
    int b = c & ((1 << halfNbites) - 1);
    if (c == a * (int)Math.pow(2, Math.abs(b)) + b) {
        if (Integer.toBinaryString(a).length() == halfNbites && Integer.toBinaryString(a).charAt(0) == '1' &&
                Integer.toBinaryString(b).length() == halfNbites && Integer.toBinaryString(b).charAt(0) == '1') {
            return "随机生成的整数c为" + c + "\n" +
                    "拆分后的整数a为" + a + "\n" +
                    "拆分后的整数b为" + b + "\n" +
                    "满足条件c=a*2^|b|+b";
        } else {
            if (count < 10000) { // 设置递归次数上限为10000
                return generateCode(Nbites, count + 1);
            } else {
                return "无法找到符合条件的a和b";
            }
        }
    } else {
        if (count < 10000) {
            return generateCode(Nbites, count + 1);
        } else {
            return "无法找到符合条件的a和b";
        }
    }
}


    public static String generateCode(int Nbites) {
        Random random = new Random();
        int count = 0;
        while (count < 10000) { // 设置最大循环次数为10000
            int c = random.nextInt((int)Math.pow(2, Nbites));
            int halfNbites = Nbites / 2;
            int a = c >>> halfNbites;
            int b = c & ((1 << halfNbites) - 1);
            if (c == a * (int)Math.pow(2, Math.abs(b)) + b) {
                if (Integer.toBinaryString(a).length() == halfNbites && Integer.toBinaryString(a).charAt(0) == '1' &&
                        Integer.toBinaryString(b).length() == halfNbites && Integer.toBinaryString(b).charAt(0) == '1') {
                    return "随机生成的整数c为" + c + "\n" +
                            "拆分后的整数a为" + a + "\n" +
                            "拆分后的整数b为" + b + "\n" +
                            "满足条件c=a*2^|b|+b";
                }
            }
            count++;
        }
        return "无法找到符合条件的a和b";
    }

    public static void main(String[] args) {
        System.out.println(generateCode(8));
    }
//    public static void main(String args[]) throws Exception {
//        String publicPath = "C:\\Users\\10908"; //公匙存放位置
//        String privatePath = "C:\\Users\\10908"; //私匙存放位置
//        BigInteger back[]=new BigInteger[2];
//        int j=0;
//        Base64 base64 = new Base64();
//        DSASign dsa = new DSASign();
//
//        //生成公钥和私钥
//        dsa.initKeys();
//        System.out.println("");
//        //要签名的数据，传入AES加密后的内容
//        String message = "NUJCQkJEQjRCRDBFODMwRTJCODkyOTQ1N0Y4NkEyNzU=";
//        System.out.println("签名的数据："+message);
//        BigInteger sig[] = dsa.signature(message.getBytes(), );
//
//        //消息摘要
//        System.out.println("消息摘要："+dsa._hashInZq(message.getBytes())+"\n");
//
//        //对消息摘要进行签名得到r和s,在使用RSA进行加密并发送
//        for (int i =0;i< sig.length;i++){
//        System.out.println("签名:"+sig[i].toString());
//         back[i]= new BigInteger(RSAEncrypt.RSAen(sig[i].toString()));
//        }
//        System.out.println("\n需要解密的信息:"+back[0]);
//        System.out.println("           "+back[1]);
//
//        System.out.println("验证结果:" + dsa.verify(message.getBytes(),back) );
//    }

}
