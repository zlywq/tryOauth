package g1.libcmn.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;


public class EncryptUtil {
	private static final String SECRET_PWD = "SECRET_PWD"; 
	private static final PasswordEncoder encoder = new StandardPasswordEncoder(SECRET_PWD); 
	public static String encrypt(String rawPassword) {
		return encoder.encode(rawPassword);
	}
	public static boolean matches(String rawPassword, String password) {
		//还得允许空密码，不然管理员忘了密码进不来了。
		if (Util1.isStringEmpty(rawPassword) && Util1.isStringEmpty(password))
			return true;
		return encoder.matches(rawPassword, password);
	}  
}
