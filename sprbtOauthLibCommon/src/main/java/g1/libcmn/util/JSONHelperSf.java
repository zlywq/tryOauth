package g1.libcmn.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**  
 * JSON和JAVA的POJO的相互转换  
 *   
 * JSONHelper.java  
 */ 
public final class JSONHelperSf {   
   
    // 将数组转换成JSON   
    public static String array2json(Object object) {   
        JSONArray jsonArray = JSONArray.fromObject(object);   
        return jsonArray.toString();   
    }   
   
    // 将JSON转换成数组,其中valueClz为数组中存放的对象的Class   
    public static Object json2Array(String json, Class valueClz) {   
        JSONArray jsonArray = JSONArray.fromObject(json);   
        return JSONArray.toArray(jsonArray, valueClz);   
    }   
   
    // 将Collection转换成JSON   
    public static String collection2json(Object object) {   
        JSONArray jsonArray = JSONArray.fromObject(object);   
        return jsonArray.toString();   
    }   
   
    // 将JSON转换成Collection,其中collectionClz为Collection具体子类的Class,   
    // valueClz为Collection中存放的对象的Class   
    public static Collection json2Collection(String json, Class collectionClz,   
            Class valueClz) {   
        JSONArray jsonArray = JSONArray.fromObject(json);   
        return JSONArray.toCollection(jsonArray, valueClz);   
    }   
   
    // 将Map转换成JSON   
    public static String map2json(Object object) {   
        JSONObject jsonObject = JSONObject.fromObject(object);   
        return jsonObject.toString();   
    }   
   
//    // 将JSON转换成Map,其中valueClz为Map中value的Class,keyArray为Map的key   
//    public static Map json2Map(Object[] keyArray, String json, Class valueClz) {   
//        JSONObject jsonObject = JSONObject.fromObject(json);   
//        Map classMap = new HashMap();   
//   
//        for (int i = 0; i < keyArray.length; i++) {   
//            classMap.put(keyArray[i], valueClz);   
//        }   
//   
//        return (Map) JSONObject.toBean(jsonObject, Map.class, classMap);   
//    }   
   public static Map<String,Object> jsonToMapSimple(String jsonStr){
	   HashMap<String, Object> map = new HashMap<String, Object>();  
	   JSONObject json = JSONObject.fromObject(jsonStr);
       for(Object k : json.keySet()){
           Object v = json.get(k);
           map.put(k.toString(), v);
       }
	   return map;
   }
    
    
    // 将POJO转换成JSON   
    public static String bean2json(Object object) {   
        JSONObject jsonObject = JSONObject.fromObject(object);   
        return jsonObject.toString();   
    }   
   
    // 将JSON转换成POJO,其中beanClz为POJO的Class   
    public static Object json2Object(String json, Class beanClz) {   
        return JSONObject.toBean(JSONObject.fromObject(json), beanClz);   
    }   
   
    // 将String转换成JSON   
    public static String string2json(String key, String value) {   
        JSONObject object = new JSONObject();   
        object.put(key, value);   
        return object.toString();   
    }   
   
    // 将JSON转换成String   
    public static String json2String(String json, String key) {   
        JSONObject jsonObject = JSONObject.fromObject(json);   
        return jsonObject.get(key).toString();   
    }   
   
} 