package com.core.security.shiro;

import java.security.MessageDigest;

public class PasswordHelper {

	/**
	 * 根据用户名与密码做md5单向hash加密
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码明文
	 * @return md5(username+password)
	 */

	public String encryptPassword(String password) {

		String inStr = password;

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	public static void main(String[] args) {
		System.out.println(new PasswordHelper().encryptPassword("5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5"));
	}
}