package com.frame.utils;

import java.util.UUID;

public class PasswordUtil {

	/**
	 * 匹配密码
	 * @param salt 盐
	 * @param rawPass 明文 
	 * @param encPass 密文
	 * @return
	 */
	public static boolean matches(String salt, String rawPass, String encPass) {
		return encode(rawPass, salt).equals(encPass);
	}


	/**
	 * 匹配密码
	 *
	 * @param rawPass 明文
	 * @param encPass 密文
	 * @return
	 */
	public static boolean matches(String rawPass, String encPass) {
		return encode(rawPass).equals(encPass);
	}


	/**
	 * 明文密码加密
	 * @param rawPass 明文
	 * @param salt	加密盐
	 * @return
	 */
	public static String encode(String rawPass, String salt) {
		return CryptoUtil.md5Encode(rawPass, s -> s + salt);
	}


	/**
	 * 明文密码加密
	 * @param rawPass 明文
	 * @return
	 */
	public static String encode(String rawPass) {
		return CryptoUtil.md5Encode(rawPass, null);
	}

	/**
	 * 生成随机加密盐
	 * @return
	 */
	public static String generateSalt() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
	}

	public static void main(String[] args) {
		String encode = encode("123");
		System.out.println(encode);
	}
}
