package g1.libcmn.util;

public class MyBaseException extends RuntimeException {


	private static final long serialVersionUID = -9034970021313666959L;
	
	private Object data;
	

	public Object getData(){
		return data;
	}
	public void setData(Object data){
		this.data=data;
	}
}
