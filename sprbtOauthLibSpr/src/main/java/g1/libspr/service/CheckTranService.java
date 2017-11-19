package g1.libspr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import g1.libcmn.domain.BbsPost;
import g1.libcmn.domain.UserInfo;
import g1.libcmn.domain.UserLogin;
import g1.libcmn.util.*;
import g1.libspr.ibatisMapper.*;

import java.util.Map;

@Service
public class CheckTranService {

	

	@Autowired
	IdGeneratorLikeSnowflake idGeneratorLikeSnowflake;
	

		
	@Autowired
	private BbsPostService bbsPostService;
	
	
	@Autowired
	UserInfoService userInfoService;
	
	
	@Autowired
	UtilService utilService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public void checkTranPost(){
		try{
			bbsPostService.checkTran1noTran();
		}catch(MyBaseException ex){
			Map<String,Object> mpData = (Map<String,Object>)ex.getData();
			long id1 = (Long)mpData.get("id1");
			long id2 = (Long)mpData.get("id2");
			BbsPost post1 = bbsPostService.getById(id1);
			BbsPost post2 = bbsPostService.getById(id2);
			if (post1 == null || post2 == null){
				throw new RuntimeException("checkTran1noTran, insert 2 should succeed but not");
			}
		}
		
		try{
			bbsPostService.checkTran1haveTran();
		}catch(MyBaseException ex){
			Map<String,Object> mpData = (Map<String,Object>)ex.getData();
			long id1 = (Long)mpData.get("id1");
			long id2 = (Long)mpData.get("id2");
			BbsPost post1 = bbsPostService.getById(id1);
			BbsPost post2 = bbsPostService.getById(id2);
			if (post1 != null || post2 != null){
				throw new RuntimeException("checkTran1haveTran, insert 2 should fail but not");
			}
		}
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void checkTran_registerUser(){
		try{
			userInfoService.checkTran1noTran();
		}catch(MyBaseException ex){
			Map<String,Object> mpData = (Map<String,Object>)ex.getData();
			long id = (Long)mpData.get("id");

			UserInfo userInfo1 = userInfoService.getById(id);
			UserLogin userLogin1 = userInfoService.getUserLoginByUserId(id);
			
			if (userInfo1 == null || userLogin1 == null){
				throw new RuntimeException("checkTran1noTran, insert 2 should succeed but not");
			}
		}
		
		try{
			userInfoService.checkTran1haveTran();
		}catch(MyBaseException ex){
			Map<String,Object> mpData = (Map<String,Object>)ex.getData();
			long id = (Long)mpData.get("id");

			UserInfo userInfo1 = userInfoService.getById(id);
			UserLogin userLogin1 = userInfoService.getUserLoginByUserId(id);
			
			if (userInfo1 != null || userLogin1 != null){
				throw new RuntimeException("checkTran1haveTran, insert 2 should fail but not");
			}
		}
		
	}
	
	
}
