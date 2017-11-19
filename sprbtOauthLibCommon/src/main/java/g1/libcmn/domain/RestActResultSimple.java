package g1.libcmn.domain;



public class RestActResultSimple {
	boolean success;
	int errCode;
	String errMsg;
	String errMsgDetail;
	

	public boolean getSuccess(){
		return success;
	}
	public void setSuccess(boolean success){
		this.success=success;
	}
	
	public int getErrCode(){
		return errCode;
	}
	public void setErrCode(int errCode){
		this.errCode=errCode;
	}


	public String getErrMsg(){
		return errMsg;
	}
	public void setErrMsg(String errMsg){
		this.errMsg=errMsg;
	}
	
	

	public String getErrMsgDetail(){
		return errMsgDetail;
	}
	public void setErrMsgDetail(String errMsgDetail){
		this.errMsgDetail=errMsgDetail;
	}

	
	

}










