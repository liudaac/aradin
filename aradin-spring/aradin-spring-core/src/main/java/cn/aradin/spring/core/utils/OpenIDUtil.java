package cn.aradin.spring.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * OPENID Algo
 * @author liudaac
 *
 */
public class OpenIDUtil {
	
	private final static String opensalt = "opensalt";
	
	private final static String unionsalt = "unionsalt";
	
	private final static char[] charray = {'0','1','2','3','4','5','6','7','8','9','α','β',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	public static void main(String[] args) {
		long test = 259866760139232131L;
		String openid = openid(test, "product");
		System.out.println(openid);
		System.out.println(openid.length());
	}
	
	private static String longToChar(long id) {
		StringBuffer strBuf = new StringBuffer();
		int bitnum = 64;
		while (bitnum > 0) {
			bitnum = bitnum - 6;
			if (bitnum < 0) {
				bitnum = 0;
			}
			int index = (int)((id >> bitnum) & 0x3FL);
			if (index > 0) {
				System.out.println(charray[index]);
				strBuf.append(charray[index]);
			}
		}
		return strBuf.toString();
	}
	
	public static String openid(Long id, String product) {
		//md5(id+product)*2插入idTchar
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(id);
		strBuf.append("-");
		if (StringUtils.isNotBlank(product)) {
			strBuf.append(product);
		}
		strBuf.append(opensalt);
		String md5 = Md5Util.getMd5(strBuf.toString().getBytes());
		String idTStr = longToChar(id);//length <= 10
		StringBuffer openBuf = new StringBuffer("On");
		for(int i=0;i<idTStr.length(); i++) {
			openBuf.append(idTStr.charAt(i));
			openBuf.append(md5.charAt(i*2));
			openBuf.append(md5.charAt(i*2+1));
		}
		return openBuf.toString();
	}
	
	public static String unionid(Long id, String product, Long developer_id) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(id);
		strBuf.append("-");
		strBuf.append(developer_id);
		strBuf.append("-");
		strBuf.append(product);
		strBuf.append(unionsalt);
		String md5 = Md5Util.getMd5(strBuf.toString().getBytes());
		String idTStr = longToChar(id);//length <= 10
		StringBuffer unionBuf= new StringBuffer("Un");
		for(int i=0;i<idTStr.length(); i++) {
			unionBuf.append(idTStr.charAt(i));
			unionBuf.append(md5.charAt(i*2));
			unionBuf.append(md5.charAt(i*2+1));
		}
		return unionBuf.toString();
	}
}
