package g1.web1.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import g1.libcmn.domain.Model1;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Value("${myOauthServer.logoutUrl}")
	String myOauthServer_logoutUrl;

	@Value("${myOauthResServer.hostUrl}")
	String myOauthResServer_hostUrl;

	@Autowired
	OAuth2ClientContext oAuth2ClientContext;
//	@Autowired
//	OAuth2RestTemplate oAuth2RestTemplate;

    @RequestMapping("/")
    public String home(Locale locale, Model model, Principal puser){
    	//logger.debug("myOauthServer_hostUrl="+myOauthServer_hostUrl+"  ,myOauthServer_logoutUrl="+myOauthServer_logoutUrl+"  ,security_oauth2_client_accessTokenUri="+security_oauth2_client_accessTokenUri);
    	
    	Date date = new Date();
    	
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("srvTimeStr", formattedDate );

		if (puser != null){
			model.addAttribute("username", puser.getName());
		}

		OAuth2AccessToken oAuth2AccessToken = oAuth2ClientContext.getAccessToken();
		String strAccessTokenObj = JSON.toJSONString(oAuth2AccessToken);
		model.addAttribute("strAccessTokenObj", strAccessTokenObj );
		if (oAuth2AccessToken != null){
			model.addAttribute("accessToken", oAuth2AccessToken.getValue() );
		}
		model.addAttribute("myOauthResServer_hostUrl", myOauthResServer_hostUrl);

        return "home";
    }
    


    @RequestMapping(value="/deny")
    public String handleDeny(){
        return "deny";
    }

    @RequestMapping("/signout2")
    public String signout2aboutsso(Model model) {
    	
    	//model.addAttribute("myOauthServer_hostUrl", myOauthServer_hostUrl);
    	model.addAttribute("myOauthServer_logoutUrl", myOauthServer_logoutUrl);
    	
        return "signout2";
    }


	@ResponseBody
	@RequestMapping("/getm1")//自动把bean转为json返回
	public Model1 getM1() {
		Model1 demo = new Model1();
		demo.setId(1);
		demo.setName("Angel");
		return demo;
	}
	
	@RequestMapping("/throwerr0")
	public int zeroException(){
		return 100/0;
	}
}
