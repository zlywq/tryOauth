package g1.oauthres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import g1.libcmn.domain.UserInfo;
import g1.libcmn.util.Util1;
import g1.libspr.service.UserInfoService;

import java.security.Principal;

@RestController
public class UserController {
	
	@Autowired
	UserInfoService userInfoService;
	
	@RequestMapping("/user")
    public UserInfo user(Principal puser) {
		long userIdSession = Util1.getUserIdInSso(puser);
		UserInfo mapData = userInfoService.getById(userIdSession);
		return mapData;
    }
	
	
	
	
	
	
	
	
	
}
