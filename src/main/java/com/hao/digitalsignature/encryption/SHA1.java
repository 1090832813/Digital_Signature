import java.util.Scanner;

public class SHA1
{

    private final int A=0x67452301;
    private final int B=0xefcdab89;
    private final int C=0x98badcfe;
    private final int D=0x10325476;
    private final int E=0xc3d2e1f0;

    private int Atemp,Btemp,Ctemp,Dtemp,Etemp;

    private  void init()
    {
        Atemp = A;
        Btemp = B;
        Ctemp = C;
        Dtemp = D;
        Etemp = E;
    }

    private int shift(int a , int s)   //循环左移
    {
        return (a << s) | (a >>> (32 - s));
    }

    private int[] add(String str)		//字符串转换为int并完成添加
    {
        int num = ((str.length() + 8) / 64) + 1;
        int strByte[] = new int[num*16];
        for(int i = 0 ; i < num*16 ; i ++)
        {
            strByte[i] = 0;
        }
        int len = str.length() , i;
        for(i = 0 ; i < len ; i ++)
        {
            strByte[i>>2] |= (str.charAt(i) & 0xff )<<(((i^3) % 4)*8);    //四个字符一个int，每个字符的8位放到对应位置
        }
        strByte[i>>2] |= 0x80 << (((i^3) % 4)*8);   //最后位加1后补0
        strByte[num*16 - 1] = str.length() * 8;   //保存长度
        return strByte;
    }

    private void Mainloop(int num[])		//循环加密
    {
        int w[] = new int[80];
        for(int i = 0 ; i < 16 ; i ++) w[i] = num[i];
        for(int i = 16 ; i < 80 ; i ++)
        {
            w[i] = shift(w[i-3] ^ w[i-8] ^ w[i-14] ^ w[i-16], 1);
        }

        int F , k;  //每轮的循环运算顺序有g决定，F是决定abc的
        int a = Atemp , b = Btemp , c = Ctemp , d = Dtemp , e = Etemp;
        for(int i = 0 ; i < 80 ; i ++)  //四轮运算
        {
            if(i < 20)
            {
                F = (b & c) | (( ~b ) & d);
                k = 0x5a827999;
            }
            else if(i < 40)
            {
                F = b ^ c ^ d ;
                k = 0x6ed9eba1;
            }
            else if(i < 60)
            {
                F = (b & c) | (b & d) | ( c & d);
                k = 0x8f1bbcdc;
            }
            else
            {
                F = b ^ c ^ d ;
                k = 0xca62c1d6;
            }
            int tmp = shift(a , 5) + e + w[i] + k + F;
            e = d ;
            d = c ;
            c = shift(b , 30) ;
            b = a;
            a = tmp;
        }
        Atemp = a + Atemp;					//保存当前组结果
        Btemp = b + Btemp;
        Ctemp = c + Ctemp;
        Dtemp = d + Dtemp;
        Etemp = e + Etemp;
    }

    private String changeHex(int a)  //转换为字符串
    {
        String str = "";
        for(int i = 0 ; i < 4 ; i ++)
        {
            str += String.format("%2s" , Integer.toHexString(((a >> (i^3)*8) % (1<<8)) & 0xff)).replace(' ', '0') ;
        }
        return str;
    }

    public String Md5(String str)
    {
        init();
        int strByte[] = add(str);
        for(int i = 0 ; i < strByte.length / 16 ; i ++)
        {
            int num[] = new int[16];
            for(int j = 0 ; j < 16 ; j ++)
            {
                num[j] = strByte[i*16+j];
                //System.out.println(num[j]);
            }
            Mainloop(num);
        }
        return changeHex(Atemp) + changeHex(Btemp) + changeHex(Ctemp) + changeHex(Dtemp) + changeHex(Etemp);
    }

    private static SHA1 instance;

    public static SHA1 getInstance()
    {
        if(instance == null)
        {
            instance = new SHA1();
        }
        return instance;
    }

    private SHA1(){};

    public static void main(String[] args) throws Exception
    {
        Scanner cin = new Scanner(System.in);
        String str = new String();
        while(cin.hasNext())
        {
            str = cin.next();
            String str2 = SHA1.getInstance().Md5(str);
            System.out.println(str2);
        }
        //System.out.println(md5(str));
    }

}