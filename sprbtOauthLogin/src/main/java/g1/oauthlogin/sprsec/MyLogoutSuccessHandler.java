package g1.oauthlogin.sprsec;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
//		String needJson = request.getParameter("needJson");
//		logger.debug(""+this.getClass().getSimpleName()+"."+Util1.getMethodName()+" enter"+" RequestURI="+request.getRequestURI()+", needJson="+needJson);
//		if ("true".equalsIgnoreCase(needJson)){
//			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//			response.setCharacterEncoding("UTF-8");
//			response.setHeader("Cache-Control","no-cache");
//			PrintWriter pw;
//			try {
//				pw = response.getWriter();
//				HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put(Const.Key_success, true);
//				String jsonStr = JSONHelperSf.map2json(hm);
//				pw.write(jsonStr);
//			} catch (IOException e) {
//				DemoBaseException.logAnyErr2(e);
//			}			
//			return;
//		}else{
//			super.onLogoutSuccess(request, response, authentication);
//			return;
//		}
		super.onLogoutSuccess(request, response, authentication);
		return;
	}
}
