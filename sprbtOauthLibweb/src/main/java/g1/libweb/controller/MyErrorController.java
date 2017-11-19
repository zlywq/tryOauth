package g1.libweb.controller;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/*
摘录BasicErrorController和AbstractErrorController的代码实现了一个。
之所以没继承 BasicErrorController ，是因为其构造函数需要ErrorProperties errorProperties，且没有自动注入，也不能为null值。估计得研究ErrorMvcAutoConfiguration的代码才能造一个这个出来。
另外，这里目前会用到 DefaultErrorAttributes ，是否有必要另行实现，目前暂无需求，以后再说。

TODO 注意需要在web应用的resources/templates下放一个error.html，在这里放了没用，只有参考意义。
 */
@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	

	private final ErrorAttributes errorAttributes;

	
	
	
	public MyErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}


	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));//
		response.setStatus(status.value());
//		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
//		return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
		

		//DefaultErrorAttributes DefaultErrorAttributes1;
		
		//这里取exception对象，然后取stack。不过在前面getErrorAttributes已经有"trace"的key的内容是stack，就不用再取了。
		//这是根据跟踪进入 DefaultErrorAttributes 的源码发现的。必要时可以继承 DefaultErrorAttributes 实现新类。
//		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//		Throwable ex = this.errorAttributes.getError(requestAttributes);
//		model.put("errstack", Util1.getExceptionStackTrace(ex));//注意要去掉前面的Collections.unmodifiableMap()

		ModelAndView modelAndView = new ModelAndView("error", model);
		return modelAndView;
	}

	@RequestMapping
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}
	
	

	protected HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		}
		catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}


	protected Map<String, Object> getErrorAttributes(HttpServletRequest request,
			boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return this.errorAttributes.getErrorAttributes(requestAttributes,
				includeStackTrace);
	}

	
	
	/**
	 * Determine if the stacktrace attribute should be included.
	 * @param request the source request
	 * @param produces the media type produced (or {@code MediaType.ALL})
	 * @return if the stacktrace attribute should be included
	 */
	protected boolean isIncludeStackTrace(HttpServletRequest request,
			MediaType produces) {
//		IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
//		if (include == IncludeStacktrace.ALWAYS) {
//			return true;
//		}
//		if (include == IncludeStacktrace.ON_TRACE_PARAM) {
//			return getTraceParameter(request);
//		}
//		return false;
		return true;
	}


//	@RequestMapping(value="/deny")
//    public String deny(){
//        return "deny";
//    }
	
}
