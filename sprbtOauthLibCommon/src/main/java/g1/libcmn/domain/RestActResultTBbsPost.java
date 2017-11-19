package g1.libcmn.domain;

import java.util.Map;

public class RestActResultTBbsPost extends RestActResultSimple {

	BbsPost dataObj;
	Map<String,Object> dataMap;
	
	

	public BbsPost getDataObj(){
		return dataObj;
	}
	public void setDataObj(BbsPost dataObj){
		this.dataObj=dataObj;
	}

	public Map<String,Object> getDataMap(){
		return dataMap;
	}
	public void setDataMap(Map<String,Object> dataMap){
		this.dataMap=dataMap;
	}
	
	
	
	

}










