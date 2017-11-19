package g1.web1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;

import java.security.Principal;
import java.util.Map;

//import org.bson.Document;


@Controller
@RequestMapping("/user")
public class UserController {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	UserInfoService userInfoService;
	@Autowired
	CheckTranService checkTranService;
	
	@Autowired
	UtilService utilService;
	

	
	
	/*
	 * http://localhost:8080/smmpWeb/user/info
	 */
	@RequestMapping(value = "/info")
	public String info(ModelMap model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = "";
		if (userDetails != null){
			userName = userDetails.getUsername();
		}
		String userNickName = "";
		if (!Util1.isStringEmpty(userName)){
			UserInfo userInfo = userInfoService.getUserByUniqueToken(userName);
			userNickName = userInfo.getNickName();
			model.addAttribute("userId", userInfo.getUserId());
		}
		
		model.addAttribute("userName", userName );
		model.addAttribute("userNickName", userNickName );
		return "logreg/info";
	}
	
	/*
	 * http://localhost:8080/smmpWeb/user/getUser.json?userId=100000
	 */
	@RequestMapping(value = "/getUser")
	public String getUser(ModelMap model, long userId, Principal puser) {
		try{
			long userIdSession = Util1.getUserIdInSso(puser);
			if (userId == 0){
				userId = userIdSession;
			}
			
			Map<String, Object> mapData = userInfoService.getUserDataById(userId);
			model.putAll(mapData);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "logreg/userInfo";
	}
	
	/*
	 * http://localhost:8080/smmpWeb/user/getUserByMobile.json?mobile=1112223334445
	 */
	@RequestMapping(value = "/getUserByMobile")
	public String getUserByMobile(ModelMap model, String mobile) {
		try{
			UserInfo userInfo = userInfoService.getUserByMobile(mobile);
			model.put("userInfo", userInfo);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "logreg/userInfo";
	}

	@RequestMapping(value = "/checkTran")
	public String checkTran(ModelMap model) {
		try{
			checkTranService.checkTran_registerUser();
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
			return "error";
		}
		return "empty";
	}
	
	
//	/*
//	 * http://localhost:8080/smmpWeb/user/deleteUser.json?userId=100000
//	 */
//	@RequestMapping(value = "/deleteUser")
//	public String deleteUser(ModelMap model, long userId) {
//		try{
//			long userIdInSession = Util1.getUserIdInSession();
//			if (userId == 0){
//				userId = userIdInSession;
//			}else{
//				if (!Util1.isSysUser(userIdInSession)){
//					utilService.throwEx(ErrCode.Dev_Common,"需要admin权限");
//				}
//			}
//			Map<String, Object> mapData = userInfoService.deleteUser(userId);
//			model.putAll(mapData);
////			model.put(Const.Key_success, true);
//		}catch(Exception e){
//			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
//		}
//		return "empty";
//	}
	
	
	
	
	
}
