package g1.libspr.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import g1.libcmn.util.*;

import java.util.HashMap;


/*
 * 参考了Twitter Snowflake http://blog.csdn.net/sunmenggmail/article/details/8461941
 * 以及第三方改写成java的Snowflake https://github.com/Predictor/javasnowflake  https://github.com/wangym/snowflake-java
 */
@Scope("singleton")
@Service
public class IdGeneratorLikeSnowflake {
	
//	@Autowired
//	Global global;
//	
	@Autowired
	UtilService utilService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	//把datacenterIdBits缩减2位，把timestampBits增加2位，这样 =2^43/(3600*1000*24*365) =~ 278year，够用了，也不用去靠twepoch来增加几十年了
	
//	private static final long hhepoch = 1300000000000L; //13 Mar 2011 07:06:40 GMT
	private static final long hhepoch = 1439526000000L;//14 Aug 2015 04:20:00 GMT
	//之所以采用这个值hhepoch，是省一点刻度下来，以后可以往上升。比如某一段id觉得用过了，可以废弃掉这一段，此时把hhepoch减少一定数值（对应若干天或若干月）即可。
	//之所以担心id用过了，是由于微信支付那边只有正式环境，测试时用过一个id就不能在下次测试或正式环境中使用这个id了。
	private static final long twepoch = hhepoch; //0L; //1288834974657L; // 4 Nov 2010 01:42:54 GMT
	
	
    private static final long datacenterIdBits = 8L;//10L;
    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    public static final long UpLimitDatacenterId = maxDatacenterId + 1;
    private static final long timestampBits = 43L; //41L;
    private static final long sequenceBits = 12L;

    private static final long datacenterIdShift = sequenceBits; //64L-datacenterIdBits;
    private static final long timestampLeftShift = sequenceBits + datacenterIdBits; // 64L-datacenterIdBits-timestampBits;
    private static final long sequenceMax = 4096;

    
//    private long datacenterId = -1;    
//    private volatile long sequence = 0L;    
    private HashMap<String, Long> hmSequence = new HashMap<String, Long>();
    //private volatile long lastTimestamp = -1L;
    private HashMap<String, Long> hmLastTimestamp = new HashMap<String, Long>();

   
    private long getSequence(String tableKey){
    	long sequence = 0;
    	Long objSequence = hmSequence.get(tableKey);
    	if (objSequence != null){
    		sequence = objSequence;
    	}
    	return sequence;
    }
    private void setSequence(String tableKey, long sequence){
    	hmSequence.put(tableKey, sequence);
    }

    private long getLastTimestamp(String tableKey){
    	long lastTimestamp = 0;
    	Long objLastTimestamp = hmLastTimestamp.get(tableKey);
    	if (objLastTimestamp != null){
    		lastTimestamp = objLastTimestamp;
    	}
    	return lastTimestamp;
    }
    private void setLastTimestamp(String tableKey, long lastTimestamp){
    	hmLastTimestamp.put(tableKey, lastTimestamp);
    }
    
    private long tilNextMillis(long lastTimestamp){
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    private long getDatacenterId(){
    	return 111;//TODO
    }
    
//    public synchronized long generateId(Object domainPojo){
//    	return generateId(domainPojo.getClass().getSimpleName());
//    }
    
    public synchronized long generateId(@SuppressWarnings("rawtypes") Class objClass){
    	return generateId(objClass.getSimpleName());
    }
    
    public synchronized long generateId(String tableKey)  {
        long timestamp = System.currentTimeMillis();
        long lastTimestamp = getLastTimestamp(tableKey);
        if(timestamp<lastTimestamp){
            utilService.throwEx(ErrCode.Dev_Common,"Clock moved backwards.  Refusing to generate id for "+ (
                    lastTimestamp - timestamp) +" milliseconds.",tableKey);
        }
        
        long sequence = getSequence(tableKey);
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) % sequenceMax;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        setSequence(tableKey,sequence);
        
        lastTimestamp = timestamp;
        setLastTimestamp(tableKey,lastTimestamp);
        long datacenterId = getDatacenterId();
        long id = ((timestamp - twepoch) << timestampLeftShift) |
                  (datacenterId << datacenterIdShift) |
                  sequence;
//        utilService.log(logger, "in generateId, tableKey="+tableKey+", sequence="+sequence+", lastTimestamp="+lastTimestamp);
        return id;
    }

    
}
