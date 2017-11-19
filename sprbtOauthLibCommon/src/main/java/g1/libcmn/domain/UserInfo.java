package g1.libcmn.domain;

import java.io.Serializable;



//SELECT `userId`,`nickName`,`mobile`,`email`,`description`,`createTime`,`updateTime` FROM User
//INSERT INTO User (`userId`,`nickName`,`mobile`,`email`,`description`,`createTime`,`updateTime`) VALUES (#{userId},#{nickName},#{mobile},#{email},#{description},#{createTime},#{updateTime}) 
//UPDATE User SET `userId`=#{userId},`nickName`=#{nickName},`mobile`=#{mobile},`email`=#{email},`description`=#{description},`createTime`=#{createTime},`updateTime`=#{updateTime} WHERE ...
//DELETE FROM User WHERE ...


public class UserInfo implements Serializable{

	private static final long serialVersionUID = 7219899084771095116L;
	private long userId;
	private String nickName;
	private String mobile;
	private String email;
	private String description;
	private byte isDisabled;
	private byte isDeleted;
	private int createTime;
	private int updateTime;

	public long getUserId(){
		return userId;
	}
	public void setUserId(long userId){
		this.userId=userId;
	}

	public String getNickName(){
		return nickName;
	}
	public void setNickName(String nickName){
		this.nickName=nickName;
	}

	public String getMobile(){
		return mobile;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}

	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email=email;
	}

	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}

	public byte getIsDisabled(){
		return isDisabled;
	}
	public void setIsDisabled(byte isDisabled){
		this.isDisabled=isDisabled;
	}

	public byte getIsDeleted(){
		return isDeleted;
	}
	public void setIsDeleted(byte isDeleted){
		this.isDeleted=isDeleted;
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

