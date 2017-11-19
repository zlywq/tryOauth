package g1.web1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.bson.Document;



/*

 */

@Controller
@RequestMapping("/post")
public class PostController {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	BbsPostService postService;
	@Autowired
	CheckTranService checkTranService;

	
	@Autowired
	UtilService utilService;
	
	@Autowired
	UtilBean utilBean;
	
	
	
	/*
	 * http://localhost:8080/smmpWeb/post/getById.json?postId=123
	 */
	@RequestMapping(value = "/getById")
	public String getPost(HttpServletRequest request, ModelMap model, long postId) {
		try{
			//webContext.setVariable("utilBean", utilBean);
			//request.getServletContext().setAttribute
//			request.getSession().setAttribute("utilBean", utilBean);//err: DefaultSerializer requires a Serializable payload but received an object of type [g1.service.UtilBean]
//			SpringContextUtils a;

//			int i = request.getSession().getMaxInactiveInterval();
//			logger.debug("request.getSession().getMaxInactiveInterval()="+i);
			
			BbsPost post = postService.getById(postId);
			model.put("post", post);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "post/post";
	}
	
	//@RequestMapping(value = "/getById_json", produces="application/json")//Error resolving template "post/getById_json", template might not exist or might not be accessible by any of the configured Template Resolvers
	@RequestMapping(value = "/getById_json")
	@ResponseBody
	public Map<String,Object> getPost(long postId ) {
		HashMap<String,Object> dtMap = new HashMap<>();
		try{
			
			
			
			BbsPost post = postService.getById(postId);
			dtMap.put("post", post);
			dtMap.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, dtMap);
		}
		return dtMap;
	}
	
	
	
	
	/*
	 * http://localhost:8080/smmpWeb/post/getPosts.json
	 */
	@RequestMapping(value = "/getPosts")
	public String getPosts(ModelMap model) {
		try{
			List<BbsPost> postList = postService.getPosts();
			model.put("postList", postList);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "post/postList";
	}
	@RequestMapping(value = "/getOnlyDeleted")
	public String getOnlyDeleted(ModelMap model) {
		try{
			List<BbsPost> postList = postService.getOnlyDeleted();
			model.put("postList", postList);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "post/postList";
	}
	
	
	
	@RequestMapping(value = "/checkTran")
	public String checkTran(ModelMap model) {
		try{
			checkTranService.checkTranPost();
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
			return "error";
		}
		return "empty";
	}
	
	/*
	 * http://localhost:8080/smmpWeb/post/insert.json?title=aa&content=aaa
	 */
	@RequestMapping(value = "/insert")
	public String insert(ModelMap model, BbsPost post, Principal puser) {
		try{
			long userIdInSession = Util1.getUserIdInSso(puser);
			post.setUserId(userIdInSession);
			
			postService.insert(post);
			model.put("postId", post.getPostId());
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "empty";
	}
	
	
	/*
	 * http://localhost:8080/smmpWeb/post/update.json?postId=12&title=aa&content=aaa
	 */
	@RequestMapping(value = "/update")
	public String update(ModelMap model, BbsPost post, Principal puser) {
		try{
			long userIdInSession = Util1.getUserIdInSso(puser);
			
			postService.update(post,userIdInSession);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "empty";
	}
	
	
	/*
	 * http://localhost:8080/smmpWeb/post/delete.json?postId=12
	 */
	@RequestMapping(value = "/delete")
	public String delete(ModelMap model, long postId, Principal puser) {
		try{
			long userIdInSession = Util1.getUserIdInSso(puser);
			postService.delete(postId, userIdInSession);
			model.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, model);
		}
		return "empty";
	}
	
	
}
