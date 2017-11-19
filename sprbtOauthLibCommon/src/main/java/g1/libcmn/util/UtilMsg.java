package g1.libcmn.util;

import g1.libcmn.domain.RestActResultSimple;
import org.bson.Document;


import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class UtilMsg {

	public static void printMongoDucumentList(List<Document> docMglist){	
		System.out.println("docMglist ="+getMsgMongoDucumentList(docMglist));
	}	
	public static void printMongoDucument(Document docMg){		
		System.out.println("docMg ="+getMsgMongoDucument(docMg));		
	}	
	public static String getMsgMongoDucumentList(List<Document> docMglist){
		StringBuffer sb = new StringBuffer();
		if (docMglist == null){
			sb.append("null list");			
		}else if (docMglist.size()==0){
			sb.append("empty list");		
		}else{
			sb.append("list data is below, size="+docMglist.size()+"\n");
			for(int i=0; i<docMglist.size(); i++){
				Document docMg = docMglist.get(i);
				String msg = getMsgMongoDucument(docMg);
				sb.append(msg);
				sb.append("\n");				
			}
		}
		return sb.toString();
	}	
	public static String getMsgMongoDucument(Document docMg){
		String msg = "null";
		if (docMg != null){
			msg = docMg.toJson();
		}
		return msg;		
	}
	
	public static String getPojoObjectMsg(Object pojo){
		String msg = "null";
		if (pojo != null){
			String pojoJsonStr = JSONHelperSf.bean2json(pojo);
			msg = pojoJsonStr;
		}
		return msg;
	}
	public static String getPojoListMsg(List pojoList){
		StringBuffer sb = new StringBuffer();
		if (pojoList == null){
			sb.append("null list");			
		}else if (pojoList.size()==0){
			sb.append("empty list");		
		}else{
			sb.append("list data is below, size="+pojoList.size()+"\n");
			for(int i=0; i<pojoList.size(); i++){
				Object pojo = pojoList.get(i);
				String msg = getPojoObjectMsg(pojo);
				sb.append(msg);
				sb.append("\n");				
			}
		}
		return sb.toString();
	}
	
	public static String getObjectArrayMsg(Object[] objs){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (objs != null){
			for(int i=0; i<objs.length; i++){
				Object obj = objs[i];
				sb.append(getObjectMsg(obj));
				sb.append(" ,\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String getObjectListMsg(List<Object> objList){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (objList != null){
			for(int i=0; i<objList.size(); i++){
				Object obj = objList.get(i);
				sb.append(getObjectMsg(obj));
				sb.append(" ,\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String getMapMsg(@SuppressWarnings("rawtypes") Map mp){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (mp != null){
			for (Object key : mp.keySet()) {
				Object val = mp.get(key);
				sb.append(key+"=");
				sb.append(getObjectMsg(val));
				sb.append(" ;\n");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	public static String getHttpServletRequestMsg(HttpServletRequest request){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (request != null){
			sb.append("Attributes:\n");
			Enumeration<String> enumAttrNames = request.getAttributeNames();
			while(enumAttrNames.hasMoreElements()){
				String attrName = enumAttrNames.nextElement();
				Object attrVal = request.getAttribute(attrName);
				sb.append("attr "+attrName+"="+attrVal);
				sb.append(" ;\n");
			}
			
			sb.append("Parameters:\n");
			Enumeration<String> enumParamNames = request.getParameterNames();
			while(enumParamNames.hasMoreElements()){
				String paramName = enumParamNames.nextElement();
				String paramVal = request.getParameter(paramName);
				sb.append("param "+paramName+"="+paramVal);
				sb.append(" ;\n");
			}
			
			sb.append("Some Data:\n");
			sb.append("ContextPath= "+request.getContextPath());
			sb.append("LocalAddr= "+request.getLocalAddr());
			sb.append("LocalName= "+request.getLocalName());
			sb.append("LocalPort= "+request.getLocalPort());
			
			sb.append("RequestURL= "+request.getRequestURL());
			sb.append("RequestURI= "+request.getRequestURI());
			sb.append("QueryString= "+request.getQueryString());
		}
		sb.append("}");
		return sb.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getObjectMsg(Object obj){
		StringBuffer sb = new StringBuffer();
		if (obj == null){
			sb.append("<null>");
		}else if (obj instanceof String || obj instanceof Character || obj instanceof CharSequence){
			sb.append(obj.toString());
		}else if (obj instanceof Number || obj instanceof Boolean){
//			Integer,Long,Double,Byte
			sb.append(obj.toString());
		}else if (obj instanceof Date){
			Date dt = (Date)obj;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
			sb.append(sdf.format(dt));
		}else if (obj instanceof Map){
			sb.append(getMapMsg((Map)obj));
		}else if (obj instanceof List){
			sb.append(getObjectListMsg((List)obj));
		}else if (obj instanceof Object[]){
			sb.append(getObjectArrayMsg((Object[])obj));
		}
		else if (obj instanceof javax.jms.Message){
			sb.append(obj.toString());
		}
		else{
			String pkgName = obj.getClass().getPackage().getName();
			if (!Util1.isStringEmpty(pkgName) && pkgName.contains(".domain")){
				try{
					String s1 = getPojoObjectMsg(obj); 
					sb.append(s1);
				}catch(Exception e){
					sb.append(obj.toString());
				}
			}else{
				sb.append(obj.toString());
			}
		}
		return sb.toString();
	}
	
	
	
	
	
	

	public static String getErrMsgNotEmpty(Exception e){
		String msg = e.getMessage();//当是NullPointerException时，msg是null值。
		if (Util1.isStringEmpty(msg)){
			msg = e.getClass().getName();
		}
		return msg;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void retriveErrMsgAndCodeToMap(DemoBaseException e, Map mapData){
		mapData.put(Const.Key_errMsg, getErrMsgNotEmpty(e));
		mapData.put(Const.Key_errCode, e.getCode());
		if (ErrCode.isDevCare(e.getCode())){
			StringBuffer sbMsgDetail = new StringBuffer(4096);
			sbMsgDetail.append("\nEnvVars=");
			sbMsgDetail.append(UtilMsg.getObjectArrayMsg(e.getEnvVars()));
			sbMsgDetail.append("\nStackTrace=");
			sbMsgDetail.append(Util1.getExceptionStackTrace(e));
			mapData.put(Const.Key_errMsgDetail, sbMsgDetail.toString());
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void retriveErrMsgAndCodeToMap(Exception e, Object[] envVars,Map mapData){
		if (e instanceof DemoBaseException){
			DemoBaseException he = (DemoBaseException)e;
			retriveErrMsgAndCodeToMap(he,mapData);
			return;
		}
		mapData.put(Const.Key_errMsg, getErrMsgNotEmpty(e));
		mapData.put(Const.Key_errCode, ErrCode.Dev_Common);
		StringBuffer sbMsgDetail = new StringBuffer(4096);
		sbMsgDetail.append("\nEnvVars=");
		sbMsgDetail.append(UtilMsg.getObjectArrayMsg(envVars));
		sbMsgDetail.append("\nStackTrace=");
		sbMsgDetail.append(Util1.getExceptionStackTrace(e));
		mapData.put(Const.Key_errMsgDetail, sbMsgDetail.toString());
	}
	
	public static void retriveErrMsgAndCodeToPojo(DemoBaseException e, RestActResultSimple objData){
		objData.setErrMsg(getErrMsgNotEmpty(e));
		objData.setErrCode(e.getCode());
		if (ErrCode.isDevCare(e.getCode())){
			StringBuffer sbMsgDetail = new StringBuffer(4096);
			sbMsgDetail.append("\nEnvVars=");
			sbMsgDetail.append(UtilMsg.getObjectArrayMsg(e.getEnvVars()));
			sbMsgDetail.append("\nStackTrace=");
			sbMsgDetail.append(Util1.getExceptionStackTrace(e));
			objData.setErrMsgDetail(sbMsgDetail.toString());
		}
	}
	public static void retriveErrMsgAndCodeToPojo(Exception e, Object[] envVars,RestActResultSimple objData){
		if (e instanceof DemoBaseException){
			DemoBaseException he = (DemoBaseException)e;
			retriveErrMsgAndCodeToPojo(he,objData);
			return;
		}
		objData.setErrMsg(getErrMsgNotEmpty(e));
		objData.setErrCode(ErrCode.Dev_Common);
		StringBuffer sbMsgDetail = new StringBuffer(4096);
		sbMsgDetail.append("\nEnvVars=");
		sbMsgDetail.append(UtilMsg.getObjectArrayMsg(envVars));
		sbMsgDetail.append("\nStackTrace=");
		sbMsgDetail.append(Util1.getExceptionStackTrace(e));
		objData.setErrMsgDetail(sbMsgDetail.toString());
	}
	
//	@SuppressWarnings({ "rawtypes" })
//	public static void retriveErrMsgAndCodeToMap(Exception e, Map mapData){
//		if (e instanceof DemoBaseException){
//			retriveErrMsgAndCodeToMap((DemoBaseException)e,mapData);
//		}else{
//			retriveErrMsgAndCodeToMap(e, ErrCode.Dev_Common, mapData);
//		}
//	}
	public static void retriveErrMsgAndCodeToMap_withLog(Exception e, @SuppressWarnings("rawtypes") Map mapData){
		retriveErrMsgAndCodeToMap(e, null ,mapData);
		DemoBaseException.logAnyErr2(e);
	}
	public static void retriveErrMsgAndCodeToMap_withLog(Exception e, @SuppressWarnings("rawtypes") Map mapData, Object... envVars){
		retriveErrMsgAndCodeToMap(e, envVars ,mapData);
		DemoBaseException.logAnyErr2(e, envVars);
	}
	
	
	public static void retriveErrMsgAndCodeToPojo_withLog(Exception e, RestActResultSimple objData){
		retriveErrMsgAndCodeToPojo(e, null ,objData);
		DemoBaseException.logAnyErr2(e);
	}
	public static void retriveErrMsgAndCodeToPojo_withLog(Exception e, RestActResultSimple objData, Object... envVars){
		retriveErrMsgAndCodeToPojo(e, envVars ,objData);
		DemoBaseException.logAnyErr2(e, envVars);
	}
	
	
	
	
	
}
