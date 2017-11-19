package g1.libspr.service;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.ibatisMapper.*;

import java.util.HashMap;
import java.util.List;


@Service
public class BbsPostService {
	
	@Autowired
	IdGeneratorLikeSnowflake idGeneratorLikeSnowflake;
	
	@Autowired
	private BbsPostMapper postMapper;
	
		
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
    DataSourceTransactionManager transactionManager;
	
	
	@Autowired
	UtilService utilService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	public BbsPost getById(long postId){
		return postMapper.getById(postId);
	}
	@Transactional(rollbackFor=Exception.class)
	public BbsPost getByIdInTran(long postId){
		return postMapper.getById(postId);
	}
	
	
	public List<BbsPost> getByUserId(long userId){
		return postMapper.getByUserId(userId);
	}
	
	public List<BbsPost> getPosts(){
		return postMapper.getPosts();
	}

	public List<BbsPost> getOnlyDeleted(){
		return postMapper.getOnlyDeleted();
	}
	
	public void checkFields(BbsPost post,boolean isInsert){
		String errMsg = null;
		if (isInsert){
			if ( post.getUserId()==0 ){
				utilService.throwEx(ErrCode.Bus_ParamErr,"userId needed", post);
			}
		}
		
		if ( Util1.isStringEmpty(post.getTitle())  // || post.getUserId()==0 
				|| Util1.isStringEmpty(post.getContent()) 
				){
			errMsg = "请输入标题，内容等必要字段";
			utilService.throwEx(ErrCode.Bus_ParamErr,errMsg, post);
		}else {	
		}
		return ;
	}
	
	private void checkTran_insert2AndThrow(){
		BbsPost post = new BbsPost();
		long postId = idGeneratorLikeSnowflake.generateId(BbsPost.class);
		post.setPostId(postId);
		post.setUserId(1);
		post.setTitle(postId+"");
		post.setContent(postId+"");
		checkFields(post,true);
		postMapper.insert(post);
		
		BbsPost post2 = new BbsPost();
		long postId2 = idGeneratorLikeSnowflake.generateId(BbsPost.class);
		post2.setPostId(postId2);
		post2.setUserId(1);
		post2.setTitle(postId+"");
		post2.setContent(postId+"");
		postMapper.insert(post2);
		
		MyBaseException ex = new MyBaseException();
		HashMap<String,Object> data = new HashMap<>();
		data.put("id1", postId);
		data.put("id2", postId2);
		ex.setData(data);
		throw ex;
	}

	@Transactional(rollbackFor=Exception.class)
	public void checkTran1haveTran(){
		checkTran_insert2AndThrow();
	}
	public void checkTran1noTran(){
		checkTran_insert2AndThrow();
	}
	
	
	
	@Transactional(rollbackFor=Exception.class)
	public void insert(BbsPost post){
		checkFields(post,true);
		
		long postId = idGeneratorLikeSnowflake.generateId(BbsPost.class);
		post.setPostId(postId);
		postMapper.insert(post);
	}
	
	
	
	@Transactional(rollbackFor=Exception.class)
	public void update(BbsPost post, long userIdDoing){
		checkFields(post,false);
		
		if (post.getPostId()==0){
			utilService.throwEx(ErrCode.Bus_ParamErr,"postId needed", post);
		}
		
		BbsPost oldPost = postMapper.getByIdForUpdate(post.getPostId());
		if (oldPost == null){
			utilService.throwEx(ErrCode.Bus_ParamErr,"no this post", post);
		}

		
		if (oldPost.getUserId() != userIdDoing){
//			utilService.throwEx(ErrCode.Bus_Common,"only can edit self post", post);
		}
		post.setUserId(oldPost.getUserId());
		
		postMapper.update(post);
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(long postId, long userIdDoing){
		if (postId==0){
			utilService.throwEx(ErrCode.Bus_ParamErr,"postId needed");
		}
		BbsPost post = postMapper.getByIdForUpdate(postId);
		if (post == null){
			return;
		}
		if (post.getUserId() != userIdDoing){
//			utilService.throwEx(ErrCode.Bus_Common,"only self can delete post", post);
		}
		postMapper.delete(postId);
	}
	
	
	
	public List<BbsPost> getPostsPaged(long upperPostIdExclude, int pageSize){
		return postMapper.getPostsPaged(upperPostIdExclude, pageSize);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
