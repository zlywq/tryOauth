package g1.libspr.ibatisMapper;


import org.apache.ibatis.annotations.*;
import g1.libcmn.domain.UserInfo;

import java.util.List;
import java.util.Map;


public interface UserInfoMapper {
	@Select("SELECT  *  FROM User WHERE userId = #{userId} AND isDeleted=0")
	UserInfo getById(@Param("userId") long userId);
	@Select("SELECT  *  FROM User WHERE userId = #{userId}")
	UserInfo getByIdWithDeleted(@Param("userId") long userId);
	
	@Select("SELECT  *  FROM User WHERE userId = #{userId} FOR UPDATE")
	UserInfo getByIdForUpdate(@Param("userId") long userId);
	

	
	


	//有些复杂的sql条件需要使用一点类似脚本的方式，在相关的xml文件中写sql语句，然后在 org.mybatis.spring.SqlSessionFactoryBean 的spring的配置文件中相应配置
	/*
	 * http://fireinjava.iteye.com/blog/1779420
	 * http://blog.csdn.net/unei66/article/details/17792503
	 * http://mybatis.github.io/mybatis-3/dynamic-sql.html
	 */
	List<UserInfo> getByIds(List<Long> userIds);
	List<UserInfo> getByIdsWithDeleted(List<Long> userIds);
	/*
	 * 主要用作示意
	 * map需要2个key。userIds和nickName。其中userIds需要是array而不能是list。
	 */
	List<UserInfo> getByIds_filterInMap(Map<String, Object> map);

	

	
	@Select("SELECT  *  FROM User WHERE mobile = #{mobile} AND isDeleted=0")
	UserInfo getByMobile(@Param("mobile") String mobile);
	
	@Select("SELECT  *  FROM User WHERE mobile = #{mobile}  AND isDeleted=0 FOR UPDATE")
	UserInfo getByMobileForUpdate(@Param("mobile") String mobile);
	
	
	@Select("SELECT  *  FROM User WHERE isDeleted=0")
	List<UserInfo> getUserInfos();
	@Select("SELECT  *  FROM User ")
	List<UserInfo> getUserInfosWithDeleted();
	
	@Select("SELECT  *  FROM User WHERE userId > #{lowerUserId} AND isDeleted=0 ORDER BY userId ASC LIMIT #{pageSize} ")
	List<UserInfo> getUserInfosPaged(@Param("lowerUserId") long lowerUserId, @Param("pageSize") int pageSize);
	@Select("SELECT  *  FROM User WHERE userId > #{lowerUserId} ORDER BY userId ASC LIMIT #{pageSize} ")
	List<UserInfo> getUserInfosPagedWithDeleted(@Param("lowerUserId") long lowerUserId, @Param("pageSize") int pageSize);

		

	@Insert("INSERT INTO User (`userId`,`nickName`,`mobile`,`email`,`description`,`isDisabled`,`isDeleted`,`createTime`,`updateTime`)"
			+" VALUES (#{userId},#{nickName},#{mobile},#{email},#{description},#{isDisabled},#{isDeleted},UNIX_TIMESTAMP(),UNIX_TIMESTAMP())  "
			)
	int insert(UserInfo user);
	@Insert("INSERT INTO User (`userId`,`nickName`,`mobile`,`email`,`description`,`isDisabled`,`isDeleted`,`createTime`,`updateTime`)"
			+" VALUES (#{userId},#{nickName},#{mobile},#{email},#{description},#{isDisabled},#{isDeleted},UNIX_TIMESTAMP(),UNIX_TIMESTAMP())  "
			)
	int insert2(Map<String, Object> params);//这里看来必须使用一个不同的名字，不然会报错
	

	@Update("UPDATE User SET `userId`=#{userId},`nickName`=#{nickName},`mobile`=#{mobile},`email`=#{email},`description`=#{description},`isDisabled`=#{isDisabled},`isDeleted`=#{isDeleted}, `updateTime`=UNIX_TIMESTAMP() WHERE `userId`=#{userId}")
	int update(UserInfo user);
	
	@Delete("DELETE FROM User WHERE userId = #{userId}")
	int delete(@Param("userId") long userId);
	
	
	@Delete("UPDATE User SET `isDeleted`=1 WHERE userId = #{userId}")
	int deleteLogic(@Param("userId") long userId);


}
