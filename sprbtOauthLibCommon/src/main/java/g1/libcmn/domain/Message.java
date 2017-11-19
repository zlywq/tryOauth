package g1.libcmn.domain;

import java.io.Serializable;


//SELECT `messageId`,`userId`,`title`,`content`,`isDeleted`,`createTime`,`updateTime` FROM Message
//INSERT INTO Message (`messageId`,`userId`,`title`,`content`,`isDeleted`,`createTime`,`updateTime`) VALUES (#{messageId},#{userId},#{title},#{content},#{isDeleted},#{createTime},#{updateTime}) 
//UPDATE Message SET `messageId`=#{messageId},`userId`=#{userId},`title`=#{title},`content`=#{content},`isDeleted`=#{isDeleted},`createTime`=#{createTime},`updateTime`=#{updateTime} WHERE ...
//DELETE FROM Message WHERE ...


public class Message implements Serializable{
	private long messageId;
	private long userId;
	private long groupId;
	private String title;
	private String content;
	private byte isDeleted;
	private int createTime;
	private int updateTime;

	public long getMessageId(){
		return messageId;
	}
	public void setMessageId(long messageId){
		this.messageId=messageId;
	}

	public long getUserId(){
		return userId;
	}
	public void setUserId(long userId){
		this.userId=userId;
	}
	
	public long getGroupId(){
		return groupId;
	}
	public void setGroupId(long groupId){
		this.groupId=groupId;
	}

	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title=title;
	}

	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content=content;
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

