package g1.libspr.service;

import org.springframework.stereotype.Service;
import g1.libcmn.util.*;

import java.util.Date;

/*
这个bean专为Themeleaf提供自定义函数
 */
@Service
public class UtilBean {
	
	public Date convertDateFromIntSeconds(int secondsFromBegin){
		return Util1.convertDateFromIntSeconds(secondsFromBegin);
	}

}
