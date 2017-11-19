package g1.oauthlogin.controller;


import g1.libcmn.domain.BbsPost;
import g1.libcmn.util.Const;
import g1.libcmn.util.UtilMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("res1")
public class Res1Controller {


    private final Logger logger = LoggerFactory.getLogger(getClass());




    @RequestMapping(value = "/me")
    @ResponseBody
    public Map<String,Object> getSelfUser(Principal principal ) {
        HashMap<String,Object> dtMap = new HashMap<>();
        try{
            dtMap.put("name", principal.getName());
            dtMap.put("principalStr", principal.toString());
            dtMap.put(Const.Key_success, true);
        }catch(Exception e){
            UtilMsg.retriveErrMsgAndCodeToMap_withLog(e, dtMap);
        }
        return dtMap;
    }






}
