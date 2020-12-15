package cn.aradin.spring.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	
	public static final ThreadLocal<MessageDigest> MD5_DIGESTER_CONTEXT = new ThreadLocal<MessageDigest>() {
		protected synchronized MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	public static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 通过文件二进制数据计算md5值
	 * 
	 * @param fileData
	 *            文件二进制数据
	 * @return md5
	 */
	public static String getMd5(byte[] fileData) {
		MessageDigest digester = MD5_DIGESTER_CONTEXT.get();
		digester.update(fileData);
		byte[] md5Bytes = digester.digest();

		int length = md5Bytes.length;
		char[] md5String = new char[length * 2];
		int k = 0;
		for (int i = 0; i < length; i++) {
			byte b = md5Bytes[i];
			md5String[k++] = HEX_DIGITS[b >>> 4 & 0xf];
			md5String[k++] = HEX_DIGITS[b & 0xf];
		}
		return new String(md5String);
	}
	
	public static String getSHA256(byte[] fileData) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(fileData);
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (Exception e) {
			System.out.println("getSHA256 is error" + e.getMessage());
		}
		return encodeStr;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		String temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				builder.append("0");
			}
			builder.append(temp);
		}
		return builder.toString();
	}
}
