package esti;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {
    private MessageDigest md5;

    public MD5Encoder() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");
    }

    public String encode(String str) {
        byte[]bytes=null;
        try {
            bytes = md5.digest(str.getBytes("utf-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String s=new String(bytes);
        return  s;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MD5Encoder md5Encoder = new MD5Encoder();
        String str1 = "achang111jjhhh";
        String str2 = "achang111jjhhh";
        String md1 = md5Encoder.encode(str1);
        String md2 = md5Encoder.encode(str2);
        System.out.println("md1" + md1);
        System.out.println("md2" + md2);
        System.out.println(md1.equals(md2));
    }
}
