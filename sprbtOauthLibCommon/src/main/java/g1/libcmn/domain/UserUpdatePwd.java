package g1.libcmn.domain;



import java.io.Serializable;

public class UserUpdatePwd extends UserReg implements Serializable{

	private static final long serialVersionUID = 1089761879595854219L;

	private String oldPassword;

	private String msgToken;
	
	
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getMsgToken() {
		return msgToken;
	}
	public void setMsgToken(String msgToken) {
		this.msgToken = msgToken;
	}

}
