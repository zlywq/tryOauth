package g1.libweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import g1.ClassInTopPackage;
import g1.libcmn.domain.*;
import g1.libcmn.util.Util1;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice(basePackageClasses = ClassInTopPackage.class)
public class ErrorHandleControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);
//		HashMap<String , Object> data = new HashMap<>();
//		data.put("status", status.value());
//		data.put("errMsg", ex.getMessage());
//		data.put("errstack", Util1.getExceptionStackTrace(ex));
//		//Object obj = new CustomErrorType(status.value(), ex.getMessage());
//		return new ResponseEntity<>(data, status);//OK
		
		RestActResultForPage data = new RestActResultForPage();
		data.setStatus(status.value());
		data.setErrMsg(ex.getMessage());
		data.setErrstack(Util1.getExceptionStackTrace(ex));
		return new ResponseEntity<>(data, status);
		
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}
}