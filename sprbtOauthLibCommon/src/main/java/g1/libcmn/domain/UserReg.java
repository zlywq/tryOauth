package g1.libcmn.domain;

import java.io.Serializable;


public class UserReg extends UserInfo implements Serializable{


	private static final long serialVersionUID = 3240822564551843484L;

	
	private String password;
	private String pwd2;

	private String msgToken;
	
		
	
	

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	

	public String getMsgToken() {
		return msgToken;
	}
	public void setMsgToken(String msgToken) {
		this.msgToken = msgToken;
	}

	
}
