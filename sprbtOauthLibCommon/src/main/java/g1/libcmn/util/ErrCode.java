package g1.libcmn.util;

public class ErrCode {

	public static final int System_Lower_Include = 100000;
	public static final int System_Upper_Exclude = 109999;
	public static final int System_Common = 100000;
	
	
	public static final int Dev_Common = 200000;

	
	
	/*
	 * Bus_Common的错误可以直接把错误信息显示给用户。
	 * Dev_Common的错误直接把错误信息显示给用户可能不太好，可能需要作信息转换，另外需要通过某些方式通知到开发者。
	 * 
	 */
	
	public static final int Bus_Lower_Include = 300000;
	public static final int Bus_Upper_Exclude = 400000;
	public static final int Bus_Common = 300000;
	public static final int Bus_DevCommon = 300001;
	public static final int Bus_ParamErr = 300010;

	
	public static boolean isBusiness(int code){
		boolean y = (code >= Bus_Lower_Include) && (code < Bus_Upper_Exclude);
		return y;
	}
	
	public static boolean isDevCare(int code){
		boolean y = ( !isBusiness(code) || code == Bus_DevCommon )  ;
		return y;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
