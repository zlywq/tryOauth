package g1.oauthlogin.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;
import g1.oauthlogin.sprsec.MyUsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("logreg")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	

	@Autowired
	UserInfoService userInfoService;


	@Autowired
	UtilService utilService;


	@Autowired
	private AuthenticationManager authenticationManager;
	

    
	@RequestMapping(value="/login", method= RequestMethod.GET)
    public String login(Model model, Principal puser){
		
		if (puser != null){
			model.addAttribute("username", puser.getName());
		}
		
        return "logreg/loginpage";
    }

    @RequestMapping("/signout")
    public String signout(HttpServletRequest request) throws Exception{
        request.logout();
        return "logreg/tologin";
    }
    

    @RequestMapping("/logout2")
    public String logout2(HttpServletRequest request) throws Exception{
        request.logout();
        return "redirect:/";
    }
    //以前别的项目出过问题。  /logout的mapping默认是post的，get的没有，然而某些时候莫名其妙会跳到get的/logout。不管logoutSuccessUrl是设置为/logout2还是/signout。
    @RequestMapping(value="/logout", method= RequestMethod.GET)
    public String logout(HttpServletRequest request) throws Exception{
        request.logout();
        return "redirect:/";
    }
    


    @RequestMapping(value="/deny")
    public String accessDeniedPage(){
        return "logreg/deny";
    }

    
    

	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String regGet(HttpServletRequest request, ModelMap model, Principal puser) {
		String puserName = null;
		if (puser != null){
			puserName = puser.getName();
		}
		if (!Util1.isStringEmpty(puserName)){
			throw new DemoBaseException("请先注销当前用户");
		}
		
		return "logreg/regpage";
	}
	/*
	 * http://localhost:8080/smmpWeb/logreg/reg
	 * http://localhost:8080/smmpWeb/logreg/reg.json?userNickName=ua1&password=aaa&pwd2=aaa&mobile=12345&msgToken=123
	 * http://localhost:8080/smmpWeb/logreg/reg.json?userNickName=ua1&password=aaa&pwd2=aaa&mobile=12345&msgToken=123&isDebug=true
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)//几乎所有的地方都应该使用POST，因为GET时参数在url中是明文看得到不安全
	public String regPost(HttpServletRequest request, UserReg userReg, ModelMap model, Principal puser) {
		String puserName = null;
		if (puser != null){
			puserName = puser.getName();
		}
		if (!Util1.isStringEmpty(puserName)){
			throw new DemoBaseException("请先注销当前用户，否则不能注册用户");
		}
		
		try{
			userInfoService.checkForRegister(userReg);

			
			UserInfo userInfo = null;
			userInfo = userInfoService.registerUser(userReg);
			model.put("userReg",null);//默认情况下参数userReg会在model中一起返回，其中含有密码，不应返回，所以这里显式设置为空。
			model.put("user",userInfo);
			
			MyUsernamePasswordAuthenticationToken token = new MyUsernamePasswordAuthenticationToken(
					userInfo.getUserId()+"", userReg.getPassword());
		    // generate session if one doesn't exist
		    request.getSession();
		    token.setDetails(new WebAuthenticationDetails(request));
		    utilService.log(logger, "regPost, before authenticationManager.authenticate, token="+token);
		    Authentication authenticatedUser = authenticationManager.authenticate(token);
		    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		    
		    model.put(Const.Key_success, true);	
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
	    return "logreg/regpage";
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    




}
