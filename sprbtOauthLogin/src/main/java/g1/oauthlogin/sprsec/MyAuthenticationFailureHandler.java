package g1.oauthlogin.sprsec;


import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import g1.libcmn.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
		logger.debug(""+this.getClass().getSimpleName()+"."+Util1.getMethodName()+" enter"+" RequestURI="+request.getRequestURI());
//		UtilMsg.getHttpServletRequestMsg(request);
//		/*
//ContextPath= /app1
//RequestURL= http://localhost:8080/app1/login
//RequestURI= /app1/login
//QueryString= null
//
//如果未登录时，随意访问某网址，是进入不到这里来的
//		 */
		
//		logger.debug("onAuthenticationFailure err.", exception);
		
		String needJson = request.getParameter("needJson");
		if ("true".equalsIgnoreCase(needJson)){
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","no-cache");
			PrintWriter pw = response.getWriter();
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put(Const.Key_success, false);
			String msg = "";
			String exMsg = exception.getMessage();
			if (!Util1.isStringEmpty(exMsg) && exMsg.contains("Bad credentials") ){
				//UsernameNotFoundException may be changed to BadCredentialsException
				msg = "登录失败，认证信息错误或用户不存在";
			}else{
				msg = "登录失败("+exMsg+")";
			}
			hm.put(Const.Key_errMsg, msg);
			hm.put(Const.Key_errCode, ErrCode.Bus_Common);
			String jsonStr = JSONHelperSf.map2json(hm);//... check chinese character--在aliyun上目前没有问题
			pw.write(jsonStr);
			return;
		}else{
			super.onAuthenticationFailure(request, response, exception);
		}
    }  
}
