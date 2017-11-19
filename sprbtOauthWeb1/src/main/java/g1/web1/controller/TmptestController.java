package g1.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import g1.libcmn.util.Const;
import g1.libcmn.util.UtilMsg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/tmptest")
public class TmptestController {
	
	@RequestMapping(value = "/getsome_json")
	@ResponseBody
	public Map<String,Object> getsome() {
		HashMap<String,Object> dtMap = new HashMap<>();
		try{
			
			dtMap.put("dtnow", new Date());
			dtMap.put(Const.Key_success, true);
		}catch(Exception e){
			UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, dtMap);
		}
		return dtMap;
	}

}
