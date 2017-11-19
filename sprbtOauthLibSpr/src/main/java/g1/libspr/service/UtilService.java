package g1.libspr.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import g1.libcmn.util.*;



@Service
public class UtilService {
	

	public static final int CommonConfig_keyValue_LogLevel_debug = 1;
	public static final int CommonConfig_keyValue_LogLevel_info = 100;
	public static final int CommonConfig_keyValue_LogLevel_warn = 1000;
	public static final int CommonConfig_keyValue_LogLevel_error = 10000;
	
//	@Autowired
//	GetValueFromPropertiesFile getValueFromPropertiesFile;
	

	

	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	
	
	public void log(Logger logger, String msg){
		log(logger, msg, null);
	}
	public void log(Logger logger, String msg, Throwable ex){
//		int logLevel = TODO
		
	}
	public void log(Logger logger, String msg, Throwable ex, int logLevel ){
		if (logLevel >= CommonConfig_keyValue_LogLevel_error){
			if (ex == null){
				logger.error(msg);
			}else{
				logger.error(msg, ex);
			}
		}else if (logLevel >= CommonConfig_keyValue_LogLevel_warn){
			if (ex == null){
				logger.warn(msg);
			}else{
				logger.warn(msg, ex);
			}
		}else if (logLevel >= CommonConfig_keyValue_LogLevel_info){
			if (ex == null){
				logger.info(msg);
			}else{
				logger.info(msg, ex);
			}
		}else if (logLevel >= CommonConfig_keyValue_LogLevel_debug){
			if (ex == null){
				logger.debug(msg);
			}else{
				logger.debug(msg, ex);
			}
		}else{
			//NOT do log
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	 * 
	 * 在抛DemoBaseException的时候记log，这样便于集中控制，从而其他地方没必要记了，或者从简。
	 * 不过这里注意只针对DemoBaseException的类型，其他类型的Exception还得在其他特定的地方控管。
	 * 开发者很关心的以error级别记log。
	 * 普通Exception以debug级别记log，但还是需要记一下，免得api调用者那边收到err但没有反馈误事。
	 */
	void logErr(DemoBaseException he){
		Object[] envVars = he.getEnvVars();
		try{
			if (ErrCode.isDevCare(he.getCode())){
				logger.error("errEnvVars="+UtilMsg.getObjectArrayMsg(envVars),he);
			}else{
				//logger.debug("errEnvVars="+Util1.getObjectArrayMsg(envVars),he);//打印堆栈太多了，普通的business的Exception就不用打印这个了。
				log(logger, "errEnvVars="+UtilMsg.getObjectArrayMsg(envVars)+"\nerrMsg="+he.getMessage());
			}
		}catch(Exception e){
			try{
				logger.error("err when logging=",e);
			}catch(Exception e2){	
			}
		}
	}
	/*
	 * 尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	 * 工具函数。与logErr互补。
	 * 当是DemoBaseException时，不必在此记录log，因为抛出时已经在logErr记录log了，在这里记会导致重复。
	 * 而其他类型的Exception才需要记log，以error级别。
	 */
	public void logAnyErr(Exception e, Object... envVars){
		if (e instanceof DemoBaseException){
			//no need log here, because having logged when throwing
//			DemoBaseException he = (DemoBaseException)e;
//			logErr(he, envVars);
		}else{
			try{
				logger.error("errEnvVars="+UtilMsg.getObjectArrayMsg(envVars),e);
			}catch(Exception e2){
				try{
					logger.error("err when logging=",e2);
				}catch(Exception e3){	
				}
			}
		}
	}
	
	//尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	public void throwEx(String s, Object... envVars){
		DemoBaseException he = new DemoBaseException(s);
		he.setEnvVars(envVars);
		logErr(he);
		throw he;
	}
	//尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	public void throwEx(Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(t);
		he.setEnvVars(envVars);
		logErr(he);
		throw he;
	}
	//尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	public void throwEx(int code, String s, Object... envVars){
		DemoBaseException he = new DemoBaseException(code, s);
		he.setEnvVars(envVars);
		logErr(he);
		throw he;
	}
	//尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	public void throwEx(int code, Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(code, t);
		he.setEnvVars(envVars);
		logErr(he);
		throw he;
	}
	//尽量替代 DemoBaseException 的同名函数，除了某些static的地方替代不了以外。
	public void throwEx(int code, String s, Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(code,s,t);
		he.setEnvVars(envVars);
		logErr(he);
		throw he;
	}
	
	
}
