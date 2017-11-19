package g1.libspr.service;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import g1.libcmn.domain.UserInfo;
import g1.libcmn.domain.UserLogin;
import g1.libcmn.domain.UserReg;
import g1.libcmn.domain.UserUpdatePwd;
import g1.libcmn.util.*;
import g1.libspr.ibatisMapper.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserInfoService {
	
	@Autowired
	IdGeneratorLikeSnowflake idGeneratorLikeSnowflake;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private UserLoginMapper userLoginMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
    DataSourceTransactionManager transactionManager;
	
	
	@Autowired
	UtilService utilService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public UserInfo getUserByUniqueToken(String uniqueToken){
		UserInfo userInfo = null;
		Long userId = null;
		try{
			userId = Long.parseLong(uniqueToken);
		}catch(NumberFormatException e){
			//do nothing
		}
		if (userId!=null){
			userInfo = userInfoMapper.getById(userId);
		}
		if (userInfo != null){
			return userInfo;
		}
		userInfo = userInfoMapper.getByMobile(uniqueToken);
		return userInfo;		
	}
	@Transactional(rollbackFor=Exception.class)
	public UserInfo getUserByUniqueTokenInTran(String uniqueToken){
		return getUserByUniqueToken(uniqueToken);		
	}
	
	public UserInfo getById(long userId){
		return userInfoMapper.getById(userId);
	}
	@Transactional(rollbackFor=Exception.class)
	public UserInfo getByIdInTran(long userId){
		return userInfoMapper.getById(userId);
	}
	public Map<String, Object> getUserDataById(long userId){
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		UserInfo userInfo = userInfoMapper.getById(userId);
		mapRet.put("userInfo", userInfo);
		return mapRet;
	}
	
	
	public List<UserInfo> getByIds(List<Long> userIds, boolean withDeleted){
		if (withDeleted){
			return userInfoMapper.getByIdsWithDeleted(userIds);
		}else{
			return userInfoMapper.getByIds(userIds);
		}
	}
	
	
	
	
	
	
	
	public UserInfo getUserByMobile(String mobile){
		UserInfo userInfo = null;		
		userInfo = userInfoMapper.getByMobile(mobile);
		return userInfo;
	}
	
	
	public UserLogin getUserLoginByUserId(long userId){
		UserLogin userLogin = null;
		userLogin = userLoginMapper.getByUsername(userId+"");
		return userLogin;
	}
	@Transactional(rollbackFor=Exception.class)
	public UserLogin getUserLoginByUserIdInTran(long userId){
		return getUserLoginByUserId(userId);
	}
	
	public void checkForRegister(UserReg userReg){
		String errMsg = null;
		if ( Util1.isStringEmpty(userReg.getPassword()) || Util1.isStringEmpty(userReg.getPwd2()) 
				|| Util1.isStringEmpty(userReg.getMobile()) ){
			errMsg = "请输入密码，确认密码，手机号";
			utilService.throwEx(ErrCode.Bus_ParamErr,errMsg, userReg.getMobile(), userReg.getMsgToken());//注意密码不能写入log
		}else if (!userReg.getPassword().equals(userReg.getPwd2())){
			errMsg = "密码和确认密码不一致";
			utilService.throwEx(ErrCode.Bus_ParamErr,errMsg, userReg.getMobile(), userReg.getMsgToken());//注意密码不能写入log
		}else {	
		}
		return ;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public UserInfo registerUser(UserReg userReg) {
		return registerUser_in(userReg);
	}
	private UserInfo registerUser_in(UserReg userReg) {
		//check fields。 其中手机验证码的验证比较费时，放到事务中不好，改为放到上层函数去做。
		checkForRegister(userReg);
		
		String mobile = userReg.getMobile();
		UserInfo userInfo = userInfoMapper.getByMobileForUpdate(mobile);
		if (userInfo != null){
			utilService.throwEx(ErrCode.Bus_Common,"这个手机号已经注册过", userReg.getMobile()) ;//注意密码不能写入log
		}
		
		userInfo = userReg;
		long userId = idGeneratorLikeSnowflake.generateId(UserInfo.class);
		userInfo.setUserId(userId);
		userInfoMapper.insert(userInfo);

		String pwdHash = EncryptUtil.encrypt(userReg.getPassword());
		UserLogin userLogin = new UserLogin();
		userLogin.setUsername(userInfo.getUserId()+"");
		userLogin.setPassword(pwdHash);
		userLoginMapper.insertUserLogin(userLogin);
		
		return userInfo;
	}
	
	
	private void checkTran_registerUserAndThrow(){
		UserReg userReg = new UserReg();
		Date dtNow = new Date();
		long idsome = dtNow.getTime();
		userReg.setNickName("usr"+idsome);
		userReg.setMobile(idsome+"");
		userReg.setPassword("aaa");
		userReg.setPwd2("aaa");
		UserInfo usrInfo = registerUser_in(userReg);
		
		MyBaseException ex = new MyBaseException();
		HashMap<String,Object> data = new HashMap<>();
		data.put("id", usrInfo.getUserId());
		ex.setData(data);
		throw ex;
	}
	@Transactional(rollbackFor=Exception.class)
	public void checkTran1haveTran(){
		checkTran_registerUserAndThrow();
	}
	public void checkTran1noTran(){
		checkTran_registerUserAndThrow();
	}
	
	
	
	@Transactional(rollbackFor=Exception.class)
	public void updateUserInfo(UserInfo user){
		userInfoMapper.update(user);
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	public void updateUserPwdByUserId(UserUpdatePwd dataUpdPwd, boolean isByAdmin) {
		if (Util1.isStringEmpty(dataUpdPwd.getPassword()) || Util1.isStringEmpty(dataUpdPwd.getPwd2()) 
				|| dataUpdPwd.getUserId()<=0)
		{
			utilService.throwEx(ErrCode.Bus_ParamErr,"请填入必要字段" ,dataUpdPwd.getUserId(),isByAdmin);//注意密码不能写入log
		}
		
		if (!dataUpdPwd.getPassword().equals(dataUpdPwd.getPwd2())){
			utilService.throwEx(ErrCode.Bus_Common,"两次密码输入不一致" ,dataUpdPwd.getUserId(),isByAdmin);//注意密码不能写入log
		}
		
		UserInfo userInfo = userInfoMapper.getById(dataUpdPwd.getUserId());
		if (userInfo == null){
			utilService.throwEx(ErrCode.Bus_Common,"用户代号有错" ,dataUpdPwd.getUserId(),isByAdmin) ;//注意密码不能写入log
		}
		
		if (!isByAdmin){
			UserLogin userLogin = userLoginMapper.getByUsername(userInfo.getUserId()+"");
			if (!EncryptUtil.matches(dataUpdPwd.getOldPassword(), userLogin.getPassword())) {
				utilService.throwEx(ErrCode.Bus_Common,"原密码输入错误" ,dataUpdPwd.getUserId(),isByAdmin) ;  //注意密码不能写入log
			}
		}		
		
		String pwdHash = EncryptUtil.encrypt(dataUpdPwd.getPassword());
		UserLogin userLoginU = new UserLogin();
		userLoginU.setUsername(dataUpdPwd.getUserId()+"");
		userLoginU.setPassword(pwdHash);
		userLoginMapper.updateUserLoginPassword(userLoginU);
		
		return ;
	}
	
	
	
	
	
	public List<UserInfo> getUserInfosPaged(long lowerUserId, int pageSize){
		return userInfoMapper.getUserInfosPaged(lowerUserId, pageSize);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
