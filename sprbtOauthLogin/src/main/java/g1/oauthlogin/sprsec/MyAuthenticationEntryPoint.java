package g1.oauthlogin.sprsec;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import g1.libcmn.util.*;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());


//	@Value("${myserver.awareReverseProxyUrl.scheme}")
//	String myserver_awareReverseProxyUrl_scheme;
//    @Value("${myserver.awareReverseProxyUrl.serverName}")
//    String myserver_awareReverseProxyUrl_serverName;
//    @Value("${myserver.awareReverseProxyUrl.serverPort}")
//    int myserver_awareReverseProxyUrl_serverPort;



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

//	/*
//	这里是为了解决前端有nginx代理时出的一个问题。详细说明见有关nginx的说明部分。
//	 */
//	protected String buildRedirectUrlToLoginPage(HttpServletRequest request,
//												 HttpServletResponse response, AuthenticationException authException) {
//        String url ;
//	    if (StringUtils.isNotEmpty(myserver_awareReverseProxyUrl_serverName)){
//            String loginForm = determineUrlToUseForThisRequest(request, response,authException);
////            if (UrlUtils.isAbsoluteUrl(loginForm)) {
////                return loginForm;
////            }
//            RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
//            String scheme = myserver_awareReverseProxyUrl_scheme;
//            if (StringUtils.isEmpty(scheme)){
//                scheme = request.getScheme();
//            }
//            int serverPort = myserver_awareReverseProxyUrl_serverPort;
//            if (serverPort == 0){
//                //serverPort = portResolver.getServerPort(request);
//                request.getServerPort();
//            }
//
//            urlBuilder.setScheme(scheme);
//            urlBuilder.setServerName(myserver_awareReverseProxyUrl_serverName);
//            urlBuilder.setPort(serverPort);
//            urlBuilder.setContextPath(request.getContextPath());
//            urlBuilder.setPathInfo(loginForm);
//            url = urlBuilder.getUrl();
//        }else{
//	        url = super.buildRedirectUrlToLoginPage(request,response,authException);
//        }
//        return url;
//	}
	

}
