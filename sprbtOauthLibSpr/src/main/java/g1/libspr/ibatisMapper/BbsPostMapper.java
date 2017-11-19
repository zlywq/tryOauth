package g1.libspr.ibatisMapper;


import org.apache.ibatis.annotations.*;
import g1.libcmn.domain.BbsPost;

import java.util.List;
import java.util.Map;


public interface BbsPostMapper {
	@Select("SELECT  *  FROM BbsPost WHERE postId = #{postId} AND isDeleted=0")
	BbsPost getById(@Param("postId") long postId);
	@Select("SELECT  *  FROM BbsPost WHERE postId = #{postId}")
	BbsPost getByIdWithDeleted(@Param("postId") long postId);
	
	@Select("SELECT  *  FROM BbsPost WHERE postId = #{postId} FOR UPDATE")
	BbsPost getByIdForUpdate(@Param("postId") long postId);
	

	
	@Select("SELECT  *  FROM BbsPost WHERE userId = #{userId} AND isDeleted=0 ORDER BY createTime DESC")
	List<BbsPost> getByUserId(@Param("userId") long userId);
	
	List<BbsPost> getOnlyDeleted();
	
	@Select("SELECT  *  FROM BbsPost WHERE isDeleted=0 ORDER BY createTime DESC")
	List<BbsPost> getPosts();
	
	
	@Select("SELECT  *  FROM BbsPost WHERE postId < #{upperPostIdExclude} AND isDeleted=0 ORDER BY postId DESC LIMIT #{pageSize} ")
	List<BbsPost> getPostsPaged(@Param("upperPostIdExclude") long upperPostIdExclude, @Param("pageSize") int pageSize);


		

	@Insert("INSERT INTO BbsPost (`postId`,`userId`,`groupId`,`title`,`content`,`isDeleted`,`createTime`,`updateTime`)"
			+" VALUES (#{postId},#{userId},#{groupId},#{title},#{content},#{isDeleted},UNIX_TIMESTAMP(),UNIX_TIMESTAMP())"
			)
	int insert(BbsPost post);
	@Insert("INSERT INTO BbsPost (`postId`,`userId`,`groupId`,,`title`,`content`,`isDeleted`,`createTime`,`updateTime`)"
			+" VALUES (#{postId},#{userId},#{groupId},#{title},#{content},#{isDeleted},UNIX_TIMESTAMP(),UNIX_TIMESTAMP())"
			)
	int insert2(Map<String, Object> params);//这里看来必须使用一个不同的名字，不然会报错
	

	@Update("UPDATE BbsPost SET `title`=#{title},`content`=#{content},`isDeleted`=#{isDeleted}, `updateTime`=UNIX_TIMESTAMP() WHERE `postId`=#{postId}")
	int update(BbsPost post);
	
	@Delete("DELETE FROM BbsPost WHERE postId = #{postId}")
	int delete(@Param("postId") long postId);
	
	
	@Delete("UPDATE BbsPost SET `isDeleted`=1 WHERE postId = #{postId}")
	int deleteLogic(@Param("postId") long postId);


}
