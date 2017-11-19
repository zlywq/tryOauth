package g1.libcmn.domain;

import java.io.Serializable;

//table users


public class UserLogin implements Serializable {

	private static final long serialVersionUID = 5554433053402683189L;
	
	private String username;
	private String password;
	private byte enabled;
	private int createTime;
	private int updateTime;

	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username=username;
	}

	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}

	public byte getEnabled(){
		return enabled;
	}
	public void setEnabled(byte enabled){
		this.enabled=enabled;
	}

	public int getCreateTime(){
		return createTime;
	}
	public void setCreateTime(int createTime){
		this.createTime=createTime;
	}

	public int getUpdateTime(){
		return updateTime;
	}
	public void setUpdateTime(int updateTime){
		this.updateTime=updateTime;
	}
	
	
	
}
