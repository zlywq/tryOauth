package g1.libcmn.domain;

import java.util.List;
import java.util.Map;

public class RestActResultTBbsPostList extends RestActResultSimple {

	List<BbsPost> dataObj;
	Map<String,Object> dataMap;
	
	

	public List<BbsPost> getDataObj(){
		return dataObj;
	}
	public void setDataObj(List<BbsPost> dataObj){
		this.dataObj=dataObj;
	}

	public Map<String,Object> getDataMap(){
		return dataMap;
	}
	public void setDataMap(Map<String,Object> dataMap){
		this.dataMap=dataMap;
	}
	
	
	
	

}










