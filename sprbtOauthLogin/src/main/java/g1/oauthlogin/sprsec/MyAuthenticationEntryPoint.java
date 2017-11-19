package g1.oauthlogin.sprsec;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import g1.libcmn.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public MyAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	

	public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
		logger.debug(""+this.getClass().getSimpleName()+"."+Util1.getMethodName()+" enter"+" RequestURI="+request.getRequestURI());
		/*
看来只在未登录且需要登录时被调用
		 */
//		UtilMsg.getHttpServletRequestMsg(request);
//		/*
//param asf=sdfdf
//ContextPath= /app1
//RequestURL= http://localhost:8080/app1/home2/common.json
//RequestURI= /app1/home2/common.json
//QueryString= asf=sdfdf
//		 */
		if (request.getRequestURI().endsWith(".json")){
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","no-cache");
			PrintWriter pw = response.getWriter();
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put(Const.Key_success, false);
			hm.put(Const.Key_errMsg, "请登录");
			hm.put(Const.Key_errCode, ErrCode.Bus_Common);
			String jsonStr = JSONHelperSf.map2json(hm);//... check chinese character
			pw.write(jsonStr);
			return;
		}else{
			super.commence(request, response, authException);
		}
	}


	

}
