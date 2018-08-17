package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder 
{
	private MessageDigest md5;
	public MD5Encoder() throws NoSuchAlgorithmException 
	{
		// TODO Auto-generated constructor stub
		md5=MessageDigest.getInstance("MD5");
	}
	public String encode(String str)
	{
		
		return md5.digest(str.getBytes()).toString();
	}
}
