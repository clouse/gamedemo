package com.atet.api.utils;

import android.text.TextUtils;



/**
 * @description: 加解密工具类
 *
 * @author: LiuQin
 * @date: 2015年7月18日 下午3:11:43 
 */
public class EncryptUtilsBak {
	public static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1qSc4idfWls43XQp+HkF4enRu1iDCD3YKfbmIbiD6j257RfxBA3PLVWppWWRLmv1M+mTv+pRKXUzyv9VNWlfQhqEgo7AFxzPjrDKHKNicN9LtZSTRqz/p9rimzxpuJP4z9OB1GfNBR5Bs/hTK0jLEpMyCsMA8q3Su3v15cKOZiQIDAQAB";
    static{
    	System.loadLibrary("crypto");
        System.loadLibrary("_All_ATET_Market_Crypto");
        
        PUBLICKEY=mencrypt(PUBLICKEY);
    }
	
	public static String signPostData(String content){
		String result = null;
//		result = "123456";
		result = payEncrypt(content, PUBLICKEY);
		return result;
	}
	
	public static boolean checkSign(String content, String sign){
		boolean result = false;
		String contentSign = payEncrypt(content, PUBLICKEY);
		if(contentSign.equals(sign)){
			result = true;
		}
		return result;
	}
	
    public static String payEncrypt(String content, String key){
        if(TextUtils.isEmpty(content) || TextUtils.isEmpty(key)){
            return null;
        }
        String txt=mencrypt(content);
        return encrypt(txt, key);
    }
    
	/**
     * 加密
     * @param content 需要加密的内容
     * @param key 加密是采用的public key
     * @return 加密后的密文
     */
    public static String encryptAES(String content, String key)
    {  
    	return encrypt(content, key);
    }  
    
    /**
     * 解密
     * @param cryptograph 需要解密的密文
     * @param key 加密时采用的public key
     * @return 解密后的内容文本
     */
    public static String decryptAES(String cryptograph, String key)
    {  
    	return decrypt(cryptograph, key);
    }  
    
	public static native String encrypt(String code, String key);
	public static native String decrypt(String code, String key);
	public static native String mencrypt(String code);

}
