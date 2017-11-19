package g1.web1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;

import java.security.Principal;

//import org.bson.Document;
//import g1.libspr.sprsec.*;


@Controller
@RequestMapping("logreg")
public class LogRegController {
	

	@Autowired
	UtilService utilService;

	
	@Autowired
	UserInfoService userInfoService;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	

	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info(Model model, Principal puser) {
		//logger.debug("Received request to show common page");
		
		String userName = "";
		if (puser != null){
			userName = puser.getName();	
		}
		
		String userNickName = "";
		if (!Util1.isStringEmpty(userName)){
			UserInfo userInfo = userInfoService.getUserByUniqueToken(userName);
			userNickName = userInfo.getNickName();
		}
		
		model.addAttribute("userName", userName );
		model.addAttribute("userNickName", userNickName );
				
		return "logreg/info";
	}
	
	
	
	
//	/*
//	 * 由于login受spring security控制，看来不支持以.json的url返回json数据
//	 */
//	//@RequestMapping(value = "/login2", method = RequestMethod.GET)
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public String getLogin(
//			@RequestParam(value = "error", required = false) boolean error,
//			ModelMap model) {
//		if (error == true) {
//			model.put("error","You have entered an invalid username or password!");
//		} else {
//			model.put("error", "");
//		}
//		return "logreg/loginpage";
//	}
//	@RequestMapping(value = "/denied", method = RequestMethod.GET)
//	public String getDenied(ModelMap model) {
//		model.put(Const.Key_errMsg, "访问被拒绝");
//		//logger.debug("Received request to show denied page");
//		return "logreg/deniedpage";
//	}
	

	
//	@RequestMapping(value = "/reg", method = RequestMethod.GET)
//	public String regGet(HttpServletRequest request, ModelMap model) {
//		return "logreg/regpage";
//	}
//	/*
//	 * http://localhost:8080/smmpWeb/logreg/reg
//	 * http://localhost:8080/smmpWeb/logreg/reg.json?userNickName=ua1&password=aaa&pwd2=aaa&mobile=12345&msgToken=123
//	 * http://localhost:8080/smmpWeb/logreg/reg.json?userNickName=ua1&password=aaa&pwd2=aaa&mobile=12345&msgToken=123&isDebug=true
//	 */
//	@RequestMapping(value = "/reg", method = RequestMethod.POST)//几乎所有的地方都应该使用POST，因为GET时参数在url中是明文看得到不安全
//	public String regPost(HttpServletRequest request, UserReg userReg, ModelMap model) {
//		try{
//			userInfoService.checkForRegister(userReg);
//
//			
//			UserInfo userInfo = null;
//			userInfo = userInfoService.registerUser(userReg);
//			model.put("userReg",null);//默认情况下参数userReg会在model中一起返回，其中含有密码，不应返回，所以这里显式设置为空。
//			model.put("user",userInfo);
//			
//			MyUsernamePasswordAuthenticationToken token = new MyUsernamePasswordAuthenticationToken(
//					userInfo.getUserId()+"", userReg.getPassword());
//		    // generate session if one doesn't exist
//		    request.getSession();
//		    token.setDetails(new WebAuthenticationDetails(request));
//		    utilService.log(logger, "regPost, before authenticationManager.authenticate, token="+token);
//		    Authentication authenticatedUser = authenticationManager.authenticate(token);
//		    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
//		    
//		    model.put(Const.Key_success, true);	
//		}catch(Exception e){
//			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
//		}
//	    return "logreg/regpage";
//	}//TODO
	
	
	
//	
//	
//	/*
//	 * http://localhost:8080/smmpWeb/logreg/resetPwdByUser.json?userId=xxxxx&password=aaa&pwd2=aaa&oldPassword=111
//	 * http://localhost:8080/smmpWeb/logreg/logout
//	 * http://localhost:8080/smmpWeb/logreg/login
//	 */
//	@RequestMapping(value = "/resetPwdByUser", method = RequestMethod.POST)
//	public String resetPwdByUser(HttpServletRequest request, ModelMap model, UserUpdatePwd objUpdPwd) {
//		try{
//			long userId = Util1.getUserIdInSession();
//			if (objUpdPwd.getUserId() == 0){
//				objUpdPwd.setUserId(userId);
//			}else{
//				objUpdPwd.setUserId(userId);
//			}
//			userInfoService.updateUserPwdByUserId(objUpdPwd,false);
////			deleteUserLoginFail(objUpdPwd.getUserId());
//			model.put("userUpdatePwd", null);//默认情况下会把参数objUpdPwd也通过model返回，其中含有密码，不能返回，所以这里显式设置为空。
//			model.put(Const.Key_success, true);	
//		}catch(Exception e){
//			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
//		}
//		return "empty";
//	}
//	
	
	
	
	
	
	
	

}
