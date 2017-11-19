package g1.oauthres.controller;


import g1.libcmn.util.Const;
import g1.libcmn.util.UtilMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("res1")
public class Res1Controller {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/me")
    @ResponseBody
    public Map<String,Object> getMe(Principal principal) {
        Date dt = new Date();
        HashMap<String,Object> dtMap = new HashMap<>();
        try{
            dtMap.put("dt", dt.toString());
            dtMap.put("principalStr", principal.toString());
            dtMap.put(Const.Key_success, true);
        }catch(Exception e){
            UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, dtMap);
        }
        return dtMap;
    }



    /*
    根据fiddler抓取的http通讯可以看到，每次通过access_token来访问这个url，即使这里没有用到Principal的信息，本server也会去访问user-info-uri。可能除了抓取信息，还有验证的作用...~~~
     */
    @RequestMapping(value = "/m1")
    @ResponseBody
    public Map<String,Object> getM1() {
        Date dt = new Date();
        HashMap<String,Object> dtMap = new HashMap<>();
        try{
            dtMap.put("dt", dt.toString());
            dtMap.put("tm", dt.getTime());
            dtMap.put(Const.Key_success, true);
        }catch(Exception e){
            UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, dtMap);
        }
        return dtMap;
    }






}
