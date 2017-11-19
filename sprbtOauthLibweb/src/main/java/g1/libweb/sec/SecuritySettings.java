package g1.libweb.sec;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="securityconfig")
public class SecuritySettings {
	private String logouturl = "/logout";
    private String logoutsuccssurl = "/";
    private String permitall = "/tmp**,/tst/**";
    //X private String deniedpage = "/deny";
//    private String urlroles;

    
    public String getLogouturl() {
        return logouturl;
    }
    public void setLogouturl(String logouturl) {
        this.logouturl = logouturl;
    }
    
    public String getLogoutsuccssurl() {
        return logoutsuccssurl;
    }
    public void setLogoutsuccssurl(String logoutsuccssurl) {
        this.logoutsuccssurl = logoutsuccssurl;
    }

    public String getPermitall() {
        return permitall;
    }
    public void setPermitall(String permitall) {
        this.permitall = permitall;
    }

    //X
//    public String getDeniedpage() {
//        return deniedpage;
//    }
//    public void setDeniedpage(String deniedpage) {
//        this.deniedpage = deniedpage;
//    }

//    public String getUrlroles() {
//        return urlroles;
//    }
//    public void setUrlroles(String urlroles) {
//        this.urlroles = urlroles;
//    }
    
    
    
    
}
