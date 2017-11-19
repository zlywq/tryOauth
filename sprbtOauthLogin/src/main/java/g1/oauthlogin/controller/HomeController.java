package g1.oauthlogin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	
	

    @RequestMapping("/")
    public String home(Locale locale, Model model, Principal puser){
    	
    	Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("srvTimeStr", formattedDate );
		
		if (puser != null){
			model.addAttribute("username", puser.getName());
		}
    	
        return "index";
    }
	

    /*
模仿https://github.com/spring-cloud-samples/authserver里面的东西，但未能成功显示页面
     */
    @RequestMapping("/oauth/confirm_access")
    public String confirm_access(){
        return "confirm_access";
    }
	
	@RequestMapping("/throwerr0")
	public int zeroException(){
		return 100/0;
	}
}
