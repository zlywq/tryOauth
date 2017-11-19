package g1.libcmn.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.bson.types.ObjectId;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.w3c.dom.Document;


public class Util1 {
	
	private final static Logger logger = LoggerFactory.getLogger(Util1.class);

	
	public static boolean isStringEmpty(String s){
		if (s == null || s.length()==0){
			return true;
		}
		return false;
	}
	public static boolean isListEmpty(@SuppressWarnings("rawtypes") List lst){
		if (lst == null || lst.size()==0){
			return true;
		}
		return false;
	}
	public static String toStr(String s){
		if (isStringEmpty(s)){//由于null的string会被转为一个"null"的字符串
			return "";
		}
		return s;
	}
	public static String concat(String s1,String s2){		
		return toStr(s1)+toStr(s2);
	}
	public static boolean isIn(String s1, List<String> strList){
		if (strList == null || strList.size()==0)
			return false;
		String[] strAry = strList.toArray(new String[strList.size()]);
		return isIn(s1,strAry);
	}
	public static boolean isIn(String s1, String[] strAry){
		boolean b = false;
		if (isStringEmpty(s1)){
			DemoBaseException.throwEx2(ErrCode.Dev_Common, "s1 required", s1 , strAry);
		}
		if (strAry == null)
			return b;
//		HashSet<String> strSet = new HashSet<String>(strAry);
		for(int i=0; i<strAry.length; i++){
			String s2 = strAry[i];
			if (s1.equals(s2)){
				b = true;
				break;
			}
		}
		return b;
	}
	public static boolean isIn(int int1, int[] intAry){
		boolean b = false;
		if (intAry == null)
			return b;
		for(int i=0; i<intAry.length; i++){
			int i2 = intAry[i];
			if (int1 == i2){
				b = true;
				break;
			}
		}
		return b;
	}
	
	// 验证手机号  
	static final Pattern PatternMobile = Pattern.compile("[0-9]{11}$"); 
	public static boolean checkIsMobile(String mobileStr){		
        Matcher m = null;  
        boolean b = false;
        m = PatternMobile.matcher(mobileStr);
        b = m.matches();   
        return b;  
	}
	
	/*
	 * 注意时区是当前时区，不是UTC
	 */
	public static Date getTodayBegin(){
		String dtFormat = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(dtFormat);
		Date dtNow = new Date();
		String todayStr = sdf.format(dtNow);
		try {
			Date dtTodayBegin = sdf.parse(todayStr);
			return dtTodayBegin;
		} catch (ParseException e) {
			DemoBaseException.logAnyErr2(e);
		}
		return null;
	}
	
	/*
	 * 注意时区是当前时区，不是UTC
	 */
	public static int getTodayBeginTimeInSecond(){
		Date dtTodayBegin = getTodayBegin();
		int todayBeginTime = (int)(dtTodayBegin.getTime() / 1000);
		return todayBeginTime;
	}
	/*
	 * 注意时区是当前时区，不是UTC
	 */
	public static int getBeforeTodayBeginTimeInSecond(int dayCount){
		int todayBeginTime = getTodayBeginTimeInSecond();
		int beforeTodayBeginTime = todayBeginTime - dayCount * 60*60*24;
		return beforeTodayBeginTime;		
	}
	
//	public static int getBeginDayTimeInSecondBeforeSeveralMonth(){
//		return getBeforeTodayBeginTimeInSecond(Const.LimitDaysForHistory_Normal);
//	}
	
	private final static int VerifyCode_Len = 6;
	private final static int VerifyCode_UpLimit = (int)Math.round(Math.pow(10, VerifyCode_Len));
	private final static String VerifyCode_StringFormat = "%0"+ VerifyCode_Len +"d";
	public static String generateVerifyCode(){		
		Random rdm = new Random();
		int ir = rdm.nextInt(VerifyCode_UpLimit);
		String sr = String.format(VerifyCode_StringFormat, ir);
		return sr;
	}
	
	

	/*
	 * 只处理一层map，即map中的value全是简单值
	 */
	public static org.dom4j.Document convertXmlDocDom4jFromMapSimple(Map map){
		if (map == null)
			return null;
		org.dom4j.Document xmlDoc = DocumentHelper.createDocument();  
		org.dom4j.Element nodeElement = xmlDoc.addElement("xml");
		
		Iterator it=map.keySet().iterator();    
		while(it.hasNext()){    
		     String key = it.next().toString();    
		     Object val = map.get(key);    
		     org.dom4j.Element keyElement = nodeElement.addElement(key); 
	         keyElement.addCDATA(String.valueOf(val));
		}
	    return xmlDoc;
	}
	public static org.dom4j.Element convertXmlElementDom4jFromMapSimple(Map map){
		if (map == null)
			return null;
		org.dom4j.Document xmlDoc = DocumentHelper.createDocument();  
		org.dom4j.Element rootElement = xmlDoc.addElement("xml");
		
		Iterator it=map.keySet().iterator();    
		while(it.hasNext()){    
		     String key = it.next().toString();    
		     Object val = map.get(key);    
		     org.dom4j.Element keyElement = rootElement.addElement(key); 
	         keyElement.addCDATA(String.valueOf(val));
		}
	    return rootElement;
	}
	
	public static String convertXmlStrFromXmlDocDom4j(org.dom4j.Document xmlDoc) {		
		if (xmlDoc == null)
			return null;
        String s = "";  
        try {  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            org.dom4j.io.OutputFormat format = new org.dom4j.io.OutputFormat("  ", true, Const.Charset_Encoding_UTF_8);  
            org.dom4j.io.XMLWriter writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(xmlDoc);  
            s = out.toString(Const.Charset_Encoding_UTF_8);  
            return s;  
        } catch (Exception e) {  
        	DemoBaseException.throwEx2(ErrCode.Dev_Common,e, xmlDoc);  
        }
        return null;
    }
	public static String convertXmlStrFromXmlElementDom4j(org.dom4j.Element xmlElement) {		
		if (xmlElement == null)
			return null;
        String s = "";  
        try {  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            org.dom4j.io.OutputFormat format = new org.dom4j.io.OutputFormat("  ", true, Const.Charset_Encoding_UTF_8);  
            org.dom4j.io.XMLWriter writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(xmlElement);  
            s = out.toString(Const.Charset_Encoding_UTF_8);  
            return s;  
        } catch (Exception e) {  
        	DemoBaseException.throwEx2(ErrCode.Dev_Common,e, xmlElement);  
        }
        return null;
    }
	public static String convertXmlStrFromMapSimple_ByDocument(Map map){
		return convertXmlStrFromXmlDocDom4j(convertXmlDocDom4jFromMapSimple(map));
	}
	public static String convertXmlStrFromMapSimple_ByElement(Map map){
		return convertXmlStrFromXmlElementDom4j(convertXmlElementDom4jFromMapSimple(map));
	}
	public static Map<String,String> convertMapFromXmlStrByDom4j(String xmlStr) {  
        try {  
        	Map<String,String> map = new HashMap<String,String>();  
            org.dom4j.Document document = DocumentHelper.parseText(xmlStr);  
            org.dom4j.Element nodeElement = document.getRootElement();  
            List node = nodeElement.elements();  
            for (Iterator it = node.iterator(); it.hasNext();) {  
            	org.dom4j.Element elm = (org.dom4j.Element) it.next();
            	String key  = elm.getName();
            	String val = elm.getText(); //看来既能处理 <fld>val</fld> 又能处理  <fld><![CDATA[val]]></fld> 
                map.put(key, val);  
            }  
            return map;  
        } catch (Exception e) {  
            DemoBaseException.throwEx2(ErrCode.Dev_Common,e ,xmlStr);  
        }
        return null;
    }  
	
	/*
	 * 取巧做法。假定private的字段名是按照规范来的。
	 */
	static public Map<String,Object> convertMapFromPojo(Object pojo){
		
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = pojo.getClass().getDeclaredFields();//看来都是取到了字段属性，而没有属性方法。在命名上还得遵照规范了，这需要引起注意......
        for (Field field : fields) {
            Object obj;
            try {
            	String fieldName = field.getName();
            	field.setAccessible(true);//需要调用此语句，才能避免 IllegalAccessException: Class AA can not access a member of class BB with modifiers "private"
//            	logger.debug("convertMapFromPojo fieldName="+fieldName);
            	obj = field.get(pojo);                
                if(obj!=null){
                    map.put(fieldName, obj);
                }
            } catch (IllegalArgumentException e) {
            	logger.debug("convertMapFromPojo IllegalArgumentException:"+e.getMessage());
            	
            } catch (IllegalAccessException e) {
            	logger.debug("convertMapFromPojo IllegalAccessException:"+e.getMessage());
                
            }catch (Exception e) {
                DemoBaseException.throwEx2(ErrCode.Dev_Common,e, pojo);
            } 
        }
        return map;
    }
	
	public static void convertPojoFromMapSimple(Map<String, Object> dataFrom, Object pojoTo) {  
		if(dataFrom == null || pojoTo == null)  
            return;  
        
		Field[] fields = pojoTo.getClass().getDeclaredFields();//看来都是取到了字段属性，而没有属性方法。在命名上还得遵照规范了，这需要引起注意......
        for (Field field : fields) {
            try {
            	String fieldName = field.getName();
            	if ( dataFrom.containsKey(fieldName) ){
            		Object val = dataFrom.get(fieldName);
            		field.setAccessible(true);//需要调用此语句，才能避免 IllegalAccessException: Class AA can not access a member of class BB with modifiers "private"
//                	logger.debug("convertMapFromPojo fieldName="+fieldName);
                	field.set(pojoTo, val);
            	}
            } catch (IllegalArgumentException e) {
            	logger.debug("convertMapFromPojo IllegalArgumentException:"+e.getMessage());
            	
            } catch (IllegalAccessException e) {
            	logger.debug("convertMapFromPojo IllegalAccessException:"+e.getMessage());
                
            }catch (Exception e) {
                DemoBaseException.throwEx2(ErrCode.Dev_Common,e,dataFrom, pojoTo);
            } 
        }
    }
	
	public static void copyPojoFieldsSimple(Object pojoFrom,Object pojoTo){
		if(pojoFrom == null || pojoTo == null)  
            return;  
		Map<String,Object> mpFrom = convertMapFromPojo(pojoFrom);
		convertPojoFromMapSimple(mpFrom,pojoTo);
	}
	

    public static Map<String,String> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  getStringStream(xmlString);
        org.w3c.dom.Document document = builder.parse(is);
        
        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, String> map = new HashMap<String, String>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;
    }
    public static InputStream getStringStream(String sInputString) throws UnsupportedEncodingException {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("UTF-8"));
        }
        return tInputStringStream;
    }
    
    
    public static String getStringFromInputStream(InputStream is){
    	
    	byte[] bytes = new byte[1024 * 16];  //假设html的post的数据少于16KB
        
        int nRead = 1;  
        int nTotalRead = 0;  
        while (nRead > 0) {  
            try {
				nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
				if (nRead > 0)  
	                nTotalRead = nTotalRead + nRead;  
			} catch (IOException e) {
				DemoBaseException.throwEx2(ErrCode.Dev_Common,e);
			}
        }  
        String str=null;
		try {
			str = new String(bytes, 0, nTotalRead, Const.Charset_Encoding_UTF_8);
		} catch (UnsupportedEncodingException e) {
			DemoBaseException.throwEx2(ErrCode.Dev_Common,e);
		}
        return str;
    }

//	public Map<String,String> decodeXml(String content) {
//
//		try {
//			Map<String, String> xml = new HashMap<String, String>();
//			XmlPullParser parser = Xml.newPullParser();
//			parser.setInput(new StringReader(content));
//			int event = parser.getEventType();
//			while (event != XmlPullParser.END_DOCUMENT) {
//
//				String nodeName=parser.getName();
//				switch (event) {
//					case XmlPullParser.START_DOCUMENT:
//
//						break;
//					case XmlPullParser.START_TAG:
//
//						if("xml".equals(nodeName)==false){
//							//实例化student对象
//							xml.put(nodeName,parser.nextText());
//						}
//						break;
//					case XmlPullParser.END_TAG:
//						break;
//				}
//				event = parser.next();
//			}
//
//			return xml;
//		} catch (Exception e) {
//			Log.e("orion",e.toString());
//		}
//		return null;
//
//	}
    
    /*
     * 不太准确。一次试验获取到本机的虚拟机的ip。
     */
    static public String getSelfIp(){
    	String selfip= "127.0.0.1";        
    	try {
    		InetAddress addr = InetAddress.getLocalHost();    		
    		selfip=addr.getHostAddress()+"";
    	} catch (UnknownHostException e) {
    		DemoBaseException.logAnyErr2(e);
    	}
    	return selfip;
    }
    static public byte[] getLocalMac(){
    	InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			DemoBaseException.logAnyErr2(e);
			return null;
		}
		byte[] mac = null;
    	try {
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException e) {
			DemoBaseException.logAnyErr2(e);
			return null;
		}
    	return mac;
    }
    static public long getLocalMacAddressAsLong(){
    	byte[] bytes = getLocalMac();
    	return convertToLongFromBytesSimple(bytes);
    }
    static public long convertToLongFromBytesSimple(byte[] bytes){
    	long l = 0;
    	if (bytes == null)
    		return l;
    	int len = bytes.length;
    	if (len > 8){
    		len = 8;
    	}
    	for(int i=0; i<len; i++){
    		byte bt = bytes[i];
    		l = l | (bt << (i*8));
    	}
    	return l;
    }
    
    static public final int LimitLen_statusReason  =200;
    static public String limitStringLength_statusReason(String s){
    	return limitStringLength(s,LimitLen_statusReason);
    }
	
    static public String limitStringLength(String s,int limitlen){
    	if (Util1.isStringEmpty(s))
    		return s;
    	int byteLen = getStringByteLength(s);
    	if (byteLen <= limitlen){
    		return s;
    	}
    	
    	int limitLen2 = s.length();
    	if (limitLen2 > limitlen){
    		limitLen2 = limitlen;
    	}
    	
    	String s1 = s.substring(0, limitLen2);
    	byteLen = getStringByteLength(s1);
    	if (byteLen <= limitlen){
    		return s1;
    	}
    	if (limitlen == 1)
    		return "";    	
    	do{
    		s1 = s1.substring(0, s1.length()/2);
    		byteLen = getStringByteLength(s1);
    	}while(byteLen > limitlen);
    	return s1;    	
    }
    static public int getStringByteLength(String s){
    	int byteLen = s.length();
    	try {
			byte[] bytes = s.getBytes(Const.Charset_Encoding_UTF_8);
			byteLen = bytes.length;
		} catch (UnsupportedEncodingException e) {
			DemoBaseException.logAnyErr2(e);
		}
    	return byteLen;
    }
	
	
	

	static public long getLongIdFromGeneratedWxNO(String wxNO){
		int idxFirstDigit = -1;
		for(int i=0; i<wxNO.length(); i++){
			char c = wxNO.charAt(i);
			if (Character.isDigit(c)){
				idxFirstDigit = i;
				break;
			}
		}
		if (idxFirstDigit == -1){
			DemoBaseException.throwEx2(ErrCode.Dev_Common,"not valid wxNO:"+wxNO);
		}
		String sNumber = wxNO.substring(idxFirstDigit);
		long v = Long.parseLong(sNumber);
		return v;
	}
	
	static public void threadSleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			DemoBaseException.logAnyErr2(e);
		}
	}
	
	static public Map<String, Object> getDataFromMapMessage(MapMessage mapmsg){
		Map<String, Object> mapData = new HashMap<String, Object>();
		Enumeration enumer;
		try {
			enumer = mapmsg.getMapNames();
		} catch (JMSException e) {
			DemoBaseException.throwEx2(ErrCode.Dev_Common,e, mapmsg);
			return null;
		}
		while (enumer.hasMoreElements()) {
			Object objKey = enumer.nextElement();
			String sKey = (String)objKey;
			Object val;
			try {
				val = mapmsg.getObject(sKey);
			} catch (JMSException e) {
				DemoBaseException.throwEx2(ErrCode.Dev_Common,e, mapmsg,sKey);
				return null;
			}
			mapData.put(sKey, val);			
		}
		return mapData;
	}
	
	//使用企业邮箱
	static String m_fromMail = "support@hhexp.cn";
	static String m_acntPwd = "Huihuo/1234";//TODO let it in db config
	static String m_hostMailServer = "smtp.mxhichina.com";
	static int m_hostMailServerPort = 465;
	static public void sendEmail_ByApacheLib_ByAuth(String subject, String textContent, String mailto){
		try {
//			String mailto = Const.MailOfDev;

			HtmlEmail email = new HtmlEmail();
			email.setHostName(m_hostMailServer);
			email.setSmtpPort(m_hostMailServerPort);
			email.setSSLOnConnect(true);
			
			email.setAuthentication(m_fromMail, m_acntPwd);
			email.setCharset("GBK");
			
			email.setFrom(m_fromMail, m_fromMail);
			email.addTo(mailto, mailto);
			

//			String subject = "提醒";
//			String contentHtml = "<html><body>测试</body></html>";
			String contentHtml = "<html><body>"+textContent+"</body></html>";
			email.setSubject(subject);
			email.setHtmlMsg(contentHtml);

			String sendRet = email.send();
//			logger.debug("sendEmail_ByApacheLib_ByAuth sendRet="+sendRet);
		} catch (EmailException e) {
			DemoBaseException.throwEx2(ErrCode.Dev_Common,e ,subject,textContent,mailto);
		} 
	}
	
	static public void sendEmail_ByApacheLib_ByAuth_notThrowErr(String subject, String textContent, String mailto){
		try {
			sendEmail_ByApacheLib_ByAuth(subject, textContent, mailto);
		} catch (Exception e) {
			DemoBaseException.logAnyErr2(e, subject, textContent, mailto);
		}
	}
	
	static public String getStackTrace(){
		return getExceptionStackTrace(new Exception());
	}
	
	static public String getExceptionStackTrace(Throwable t){
		//如果有Caused by的inner exception，能够看到Caused by的inner exception的消息和部分stack，但是有省略，类似"... 35 more"。
		//待查为何要省略，难道特定内存不足。使用 StringWriter sw = new StringWriter(1000000); 没有多打印一些
		//    不过如果有多级inner exception。目前试了2级的(加上最外层一共是3个exception)，也都能打印出来，难道是智能去掉重复。
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw,true);
		t.printStackTrace(pw);
		String s = sw.toString();
		return s;
	}
	
	
	static public String getMethodName() {  
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
        StackTraceElement e = stacktrace[2];  
        String methodName = e.getMethodName();  
        return methodName;  
    }
	public static String getCurrentClassName(){
//        String s0 = Thread.currentThread().getStackTrace()[0].getClassName();
//        String s1 = Thread.currentThread().getStackTrace()[1].getClassName();
		String s2 = Thread.currentThread().getStackTrace()[2].getClassName();
//        System.out.println("s0="+s0+" s1="+s1+" s2="+s2);
		//s0=java.lang.Thread s1=g1.tool.Tool s2=g1.TRocketmq1Application
		return s2;
	}
	public static String getCurrentMethodName(){
//        String s0 = Thread.currentThread().getStackTrace()[0].getMethodName();
//        String s1 = Thread.currentThread().getStackTrace()[1].getMethodName();
		String s2 = Thread.currentThread().getStackTrace()[2].getMethodName();
//        System.out.println("s0="+s0+" s1="+s1+" s2="+s2);
		//s0=getStackTrace s1=getCurrentMethodName s2=run
		return s2;
	}

//	static public boolean isInProdMode(){//this config move to db and file
//		return Const.ProductMode_prod.equals(Const.ProductMode);
//	}
	
	static public void assertMy(boolean b,String errMsg){
		if (!b){
			DemoBaseException.throwEx2(ErrCode.Dev_Common,errMsg);
		}
	}
	
	
	static public Date convertDateFromIntSeconds(int secondsFromBegin){
		return new Date(secondsFromBegin * 1000L);
	}
	
	static public ArrayList<Long> convertLongListFromNumberList(@SuppressWarnings("rawtypes") List nList){
		if (nList == null || nList.size()==0)
			return null;
		ArrayList<Long> lList = new ArrayList<Long>(nList.size());
		for(int i=0; i<nList.size(); i++){
			Object val = nList.get(i);
			long lval = 0;
			if (val instanceof Long){
				lval = (Long)val;
			}else if(val instanceof Integer){
				lval = (Integer)val;				
			}else if(val instanceof String){
				lval = Long.parseLong(val.toString());
			}else{
				lval = Long.parseLong(val+"");
			}
			lList.add(lval);
		}
		return lList;
	}
	
	static public double[] convertDoubleArrayFromNumberList(List dblList){
		if (dblList == null)
			return null;
		double[] dblAry = new double[dblList.size()];
		for(int i=0; i<dblList.size(); i++){
			try{
				dblAry[i] = (Double)(dblList.get(i));
			}catch(Exception e){
				dblAry[i] = Double.parseDouble( dblList.get(i)+"" );
			}
		}
		return dblAry;
	}
	
	//在sso的情况下不适用了
	protected static long getUserIdInSession(){
		return getUserIdInSession(true);
	}
	//在sso的情况下不适用了
	protected static long getUserIdInSession(boolean throwErrWhenNotGet){
		UserDetails userDetails = null;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null){
			Authentication authentication = securityContext.getAuthentication();
			if (authentication != null){
				Object principal = authentication.getPrincipal();
				if (principal instanceof UserDetails){
					userDetails = (UserDetails) principal;
				}else{
					logger.debug("getUserIdInSession principal="+principal);
				}
			}
		}
		
		long userId = 0;
		if (userDetails != null){
			try {
				userId = Long.parseLong(userDetails.getUsername());
			} catch (Exception e) {
				logger.warn("getUserIdInSession parseLong(userDetails.getUsername() err, Username="+userDetails.getUsername());
			}
		}
		if (userId == 0){
			if (throwErrWhenNotGet){
				DemoBaseException.throwEx2(ErrCode.Dev_Common,"NOT get userId in session");
			}
		}
		return userId;
	}
	public static long getUserIdInSso(Principal puser){
		return getUserIdInSso(puser,true);
	}
	public static long getUserIdInSso(Principal puser,boolean throwErrWhenNotGet){
		long userId = 0;
		if (puser != null){
			try {
				userId = Long.parseLong(puser.getName());
			} catch (Exception e) {
				logger.warn("getUserIdInSso parseLong(userDetails.getName() err, puser.getName()="+puser.getName());
			}
		}
		if (userId == 0){
			if (throwErrWhenNotGet){
				DemoBaseException.throwEx2(ErrCode.Dev_Common,"NOT get userId in sso");
			}
		}
		return userId;
	}
	
	
	
	static public List<String> convertStringListFromMongoObjectIdList(List<ObjectId> mgOidList){
		if (mgOidList == null)
			return null;
		List<String> strList = new ArrayList<String>();
		for(int i=0; i<mgOidList.size(); i++){
			ObjectId oid = mgOidList.get(i);
			String str = oid.toHexString();
			strList.add(str);
		}
		return strList;
	}
	static public Map<String, String> convertMapStrStrFromMapStrObj(Map<String,Object> mpStrObj){
		if (mpStrObj == null)
			return null;
		Map<String, String> mpStrStr = new HashMap<String, String>();
		for (String key : mpStrObj.keySet()) {
			Object val = mpStrObj.get(key);
			if (val instanceof String){
				String sVal = (String)val;
				mpStrStr.put(key, sVal);
			}else{
				mpStrStr.put(key, val+"");
			}
		}
		return mpStrStr;
	}
	
	
	static public void addToLongListInMap(Map<String,List<Long>> map, String key, long val){
		List<Long> lst = map.get(key);
		if (lst == null){
			lst = new ArrayList<Long>();
			map.put(key, lst);
		}
		lst.add(val);
	}
	
	public static String convertBytesToBase64Str(byte[] bytes) {
		if (bytes == null) return null;
//		String base64Str = (new sun.misc.BASE64Encoder()).encode( bytes );
//		return base64Str;
//		Base64 base64=new Base64();
		String base64Str = Base64.encodeBase64String(bytes);
        return base64Str;
	}
	public static byte[] convertBytesFromBase64Str(String base64Str) {
		if (base64Str == null) return null;
//		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
//		try { 
//			byte[] bytes = decoder.decodeBuffer(base64Str); 
//			return bytes;
//		} catch (Exception e) { 
//			DemoBaseException.throwEx2(e, base64Str);
//		}
		byte[] bytes = Base64.decodeBase64(base64Str);
        return bytes;
	}
	public static String convertToBase64Str(String s) { 
		if (s == null) return null;
		try { 
			String base64Str = convertBytesToBase64Str( s.getBytes(Const.Charset_Encoding_UTF_8) );
			return base64Str;
		} catch (Exception e) { 
			DemoBaseException.throwEx2(e ,s);
			return null;
		} 
	} 
	public static String convertFromBase64Str(String base64Str) { 
		if (base64Str == null) return null;
		byte[] bytes = convertBytesFromBase64Str(base64Str);
		try { 
			String s = new String(bytes, Const.Charset_Encoding_UTF_8);
			return s;
		} catch (Exception e) { 
			DemoBaseException.throwEx2(e, base64Str);
			return null;
		} 
	}
	
	
	
	/*
	 * the format of serverAddresses is : "host1:123;host2:234;;"
	 */
	static public List<com.mongodb.ServerAddress> convertToServerAddressList(String serverAddresses){
		if (serverAddresses == null)
			return null;
		List<com.mongodb.ServerAddress> svrAddrList = new ArrayList<com.mongodb.ServerAddress>();
		String[] serverAddressStrAry = serverAddresses.split(";");
		for(int i=0; i<serverAddressStrAry.length; i++){
			String serverAddressStr = serverAddressStrAry[i];
			if (serverAddressStr.length() > 0){
				String[] serverAddressParts = serverAddressStr.split(":");
				if (serverAddressParts.length < 2){
					DemoBaseException.throwEx2(ErrCode.Dev_Common, "serverAddressParts.length < 2",  serverAddresses,serverAddressStr);
				}
				String hostPart = serverAddressParts[0];
				String portPart = serverAddressParts[1].trim();
				int port = Integer.parseInt(portPart);
				com.mongodb.ServerAddress srvAddress = new com.mongodb.ServerAddress(hostPart, port);
				svrAddrList.add(srvAddress);
			}
		}
		System.out.println(svrAddrList+"");
		
		return svrAddrList;
	}
	
	

	static public void seeReflect(Object obj){
		if (obj == null)
			return;
		
		if (true){
			Map<String,String> mapFields = new HashMap<String, String>();
	        Field[] fields = obj.getClass().getFields();
	        for (Field field : fields) {
	            try {
	            	String fieldName = field.getName();
	            	mapFields.put(fieldName, field.toGenericString()+"\n");
	            }catch (Exception e) {
	                System.out.println("err when getFields:"+e.getMessage());
	            } 
	        }
	        System.out.println("\nFields:"+mapFields);
		}
		if (true){
			Map<String,String> mapDeclaredFields = new HashMap<String, String>();
	        Field[] declaredFields = obj.getClass().getDeclaredFields();
	        for (Field field : declaredFields) {
	            try {
	            	String fieldName = field.getName();
	            	mapDeclaredFields.put(fieldName, field.toGenericString()+"\n");
	            }catch (Exception e) {
	                System.out.println("err when getDeclaredFields:"+e.getMessage());
	            } 
	        }
	        System.out.println("\nDeclaredFields:"+mapDeclaredFields);
		}
		
        
        if (true){
        	Map<String,String> mapMethods = new HashMap<String, String>();
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                try {
                	String name = method.getName();
                	mapMethods.put(name, method.toGenericString()+"\n");
                }catch (Exception e) {
                    System.out.println("err when getMethods:"+e.getMessage());
                } 
            }
            System.out.println("\nMethods:"+mapMethods);
        }
        if (true){
        	Map<String,String> mapDeclaredMethods = new HashMap<String, String>();
            Method[] declaredMethods = obj.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                try {
                	String name = method.getName();
                	mapDeclaredMethods.put(name, method.toGenericString()+"\n");
                }catch (Exception e) {
                    System.out.println("err when getDeclaredMethods:"+e.getMessage());
                } 
            }
            System.out.println("\nDeclaredMethods:"+mapDeclaredMethods);
        }
        
        
        if (true){
        	Map<String,String> mapMethodsGetVal = new HashMap<String, String>();
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
            	String name = method.getName();
            	if (name.startsWith("get")){
            		try{
            			Object val = method.invoke(obj);
            			mapMethodsGetVal.put(name, val+"\n");
            		}catch (Exception e) {
                        System.out.println("err when getMethods:"+e.getMessage());
                        mapMethodsGetVal.put(name, "err occur"+"\n");
                    } 
            	}
            }
            System.out.println("\nmapMethodsGetVal:"+mapMethodsGetVal);
        }
        
	}
	
	static public String getProcessInfoStr(){
		String name = ManagementFactory.getRuntimeMXBean().getName();    
//		System.out.println(name);    
//		String pid = name.split("@")[0];// get pid        
//		System.out.println("Pid is:" + pid); 
		return name;
	}
	
	
}
