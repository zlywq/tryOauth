package g1.libcmn.domain;

import java.util.Map;

public class RestActResultT<T> extends RestActResultSimple {

	T dataObj;
	Map<String,Object> dataMap;
	
	

	public T getDataObj(){
		return dataObj;
	}
	public void setDataObj(T dataObj){
		this.dataObj=dataObj;
	}

	public Map<String,Object> getDataMap(){
		return dataMap;
	}
	public void setDataMap(Map<String,Object> dataMap){
		this.dataMap=dataMap;
	}
	
	
	
	

}










