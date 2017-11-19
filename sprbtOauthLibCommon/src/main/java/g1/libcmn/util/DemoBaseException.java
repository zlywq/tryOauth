package g1.libcmn.util;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import util.Util1;

public class DemoBaseException extends RuntimeException {

	private static final long serialVersionUID = 8867604961223183459L;
	
	private final static Logger logger = LoggerFactory.getLogger(DemoBaseException.class);
	

	
	
	private int code;
//	private String extMsg;
	private Object[] envVars;
	
	public DemoBaseException(String s) {
		super(s);		
	}	
	public DemoBaseException(Throwable t) {
		super(t);
	}
	public DemoBaseException(String s, Throwable t){
		super(s, t);
	}
	
	public DemoBaseException(int code, String s) {
		super(s);	
		setCode(code);
	}
//	public HuiHuoException(int code, String s, String extMsg) {
//		super(s);	
//		setCode(code);
//		setExtMsg(extMsg);
//	}
	public DemoBaseException(int code, Throwable t){
		super(t);
		setCode(code);
	}
	public DemoBaseException(int code, String s, Throwable t){
		super(s, t);
		setCode(code);
	}
//	public HuiHuoException(int code, String s, String extMsg, Throwable t){
//		super(s, t);
//		setCode(code);
//		setExtMsg(extMsg);
//	}
	
	
	public int getCode(){
		return code;		
	}
	public void setCode(int code){
		this.code = code;
	}
	
//	public String getExtMsg(){
//		return extMsg;		
//	}
//	public void setExtMsg(String extMsg){
//		this.extMsg = extMsg;
//	}
	
	public Object[] getEnvVars(){
		return envVars;		
	}
	public void setEnvVars(Object[] envVars){
		this.envVars = envVars;
	}
	
	/*
	 * 尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	 * 
	 * 在抛HuiHuoException的时候记log，这样便于集中控制，从而其他地方没必要记了，或者从简。
	 * 不过这里注意只针对HuiHuoException的类型，其他类型的Exception还得在其他特定的地方控管。
	 * 开发者很关心的以error级别记log。
	 * 普通Exception以debug级别记log，但还是需要记一下，免得api调用者那边收到err但没有反馈误事。
	 */
	static void logErr2(DemoBaseException he){
		Object[] envVars = he.getEnvVars();
		try{
			if (ErrCode.isDevCare(he.getCode())){
				logger.error("errEnvVars="+UtilMsg.getObjectArrayMsg(envVars),he);
			}else{
				//logger.debug("errEnvVars="+Util1.getObjectArrayMsg(envVars),he);//打印堆栈太多了，普通的business的Exception就不用打印这个了。
				logger.debug("errEnvVars="+UtilMsg.getObjectArrayMsg(envVars)+"\nerrMsg="+he.getMessage());
			}
		}catch(Exception e){
			try{
				logger.error("err when logging=",e);
			}catch(Exception e2){	
			}
		}
	}
	
	/*
	 * 尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	 * 
	 * 工具函数。与logErr互补。
	 * 当是HuiHuoException时，不必在此记录log，因为抛出时已经在logErr记录log了，在这里记会导致重复。
	 * 而其他类型的Exception才需要记log，以error级别。
	 */
	public static void logAnyErr2(Exception e, Object... envVars){
		if (e instanceof DemoBaseException){
			//no need log here, because having logged when throwing
//			HuiHuoException he = (HuiHuoException)e;
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
	
	//尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	public static void throwEx2(String s, Object... envVars){
		DemoBaseException he = new DemoBaseException(s);
		he.setEnvVars(envVars);
		logErr2(he);
		throw he;
	}
	//尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	public static void throwEx2(Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(t);
		he.setEnvVars(envVars);
		logErr2(he);
		throw he;
	}
	
	//尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	public static void throwEx2(int code, String s, Object... envVars){
		DemoBaseException he = new DemoBaseException(code, s);
		he.setEnvVars(envVars);
		logErr2(he);
		throw he;
	}
	//尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	public static void throwEx2(int code, Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(code, t);
		he.setEnvVars(envVars);
		logErr2(he);
		throw he;
	}
	//尽量由UtilService中的同名函数代替，因为那里对log增加了一些feature。
	public static void throwEx2(int code, String s, Throwable t, Object... envVars){
		DemoBaseException he = new DemoBaseException(code,s,t);
		he.setEnvVars(envVars);
		logErr2(he);
		throw he;
	}

}
