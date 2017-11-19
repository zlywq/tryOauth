package g1.libcmn.util;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptAES {
	
	public static final String SymmetricEncryption_SecretKey = "demo16char123456";
	
	public static final String VIPARA_str = "0102030405060708";
//	static final public byte[] VIPARA_bytes = { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

    //KeyGenerator 提供对称密钥生成器的功能，支持各种算法  
    private KeyGenerator keygen;  
    //SecretKey 负责保存对称密钥  
    private SecretKey secretKey;  
    private SecretKeySpec secretKeySpec;
    //Cipher负责完成加密或解密工作  
    private Cipher cipherEncrypt, cipherDecrypt;  

      
//    public EncryptAES(String password){ 
//    	try{
//    		if (Util1.isStringEmpty(password)){
//    			password = Const.SymmetricEncryption_SecretKey;
//    		}
//    		
////    		Security.addProvider(new com.sun.crypto.provider.SunJCE());  
//            keygen = KeyGenerator.getInstance("AES");
////            SecureRandom secureRandom = new SecureRandom(password.getBytes(Const.Charset_Encoding_UTF_8));
//            /*
//             * 解决错误 javax.crypto.BadPaddingException: Given final block not properly padded
//             * 参考 解决Linux操作系统下AES解密失败的问题  http://free4wp.com/%E8%A7%A3%E5%86%B3linux%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F%E4%B8%8Baes%E8%A7%A3%E5%AF%86%E5%A4%B1%E8%B4%A5%E7%9A%84%E9%97%AE%E9%A2%98.html
//             */
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//            secureRandom.setSeed(password.getBytes(Const.Charset_Encoding_UTF_8));
//            keygen.init(128,secureRandom);
//
//            secretKey = keygen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
//            
//            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes()); 
//
//            cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec,zeroIv);
//            cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec,zeroIv);
//    	}catch(Exception e){
//    		HuiHuoException.throwEx(e);
//    	}
//    }  
      
    public EncryptAES(String password){ 
    	try{
    		if (Util1.isStringEmpty(password)){
    			password = SymmetricEncryption_SecretKey;
    		}
    		SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES"); //看来这里要求密码长度为8的倍数.java.security.InvalidKeyException: Invalid AES key length: 11 bytes
    		
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA_str.getBytes()); 
//    		IvParameterSpec zeroIv = new IvParameterSpec(VIPARA_bytes);

            cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec,zeroIv);
            cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec,zeroIv);
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }  

    public String encrypt(String str){ 
    	try{
    		byte[] bytes = str.getBytes(Const.Charset_Encoding_UTF_8);  
    		
//    		cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);  
//    		cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] bytes2 = cipherEncrypt.doFinal(bytes);
            String base64Str = Util1.convertBytesToBase64Str(bytes2);
            return base64Str;
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }
    public String decrypt(String base64Str){
    	try{
    		byte[] bytes1 = Util1.convertBytesFromBase64Str(base64Str);

//    		cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);
//    		cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] bytes2 = cipherDecrypt.doFinal(bytes1);
            String retStr = new String(bytes2, Const.Charset_Encoding_UTF_8);
            return retStr;
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }  
    
    
    
    
    
    
/*
 * OK
 */
//    public static String a1_encryptAES(String content, String password) {
//        try {
//        	byte[] byteContent = content.getBytes("utf-8");
//        	
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(password.getBytes(Const.Charset_Encoding_UTF_8)));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            byte[] retBytes = cipher.doFinal(byteContent);
//            String base64Str = Util1.convertBytesToBase64Str(retBytes);
//            return base64Str;
//        } catch (Exception e) {
//        	throw new RuntimeException(e);
//        } 
//    }
//    public static String a1_decryptAES(String base64Str, String password) {
//        try {
//        	byte[] content = Util1.convertBytesFromBase64Str(base64Str);
//        	
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(password.getBytes(Const.Charset_Encoding_UTF_8)));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] retBytes = cipher.doFinal(content);
//            String retStr = new String(retBytes, Const.Charset_Encoding_UTF_8);
//            return retStr;
//        } catch (Exception e) {
//        	throw new RuntimeException(e);
//        }
//    }
    
    
    
    
    
    
    
    
    
    
    
}
