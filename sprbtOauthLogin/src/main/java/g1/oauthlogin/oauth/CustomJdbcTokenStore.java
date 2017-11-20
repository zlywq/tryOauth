package g1.oauthlogin.oauth;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;



/**
 经过试验，有些表在使用jwt之后是用不着的，比如这里的 oauth_access_token ，即JdbcTokenStore可以用不着 。参考dbSchema.txt里面的说明。
 */
public class CustomJdbcTokenStore extends JdbcTokenStore {


    final Logger logger             = LoggerFactory.getLogger(getClass());

    public CustomJdbcTokenStore(DataSource dataSource) {
        super(dataSource);
    }


    public OAuth2AccessToken readAccessToken(String tokenValue) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter tokenValue="+tokenValue);
        return super.readAccessToken(tokenValue);
    }

    public void removeAccessToken(String tokenValue) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter tokenValue="+tokenValue);
        super.removeAccessToken(tokenValue);
    }


    public OAuth2RefreshToken readRefreshToken(String token) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token);
        return super.readRefreshToken(token);
    }

    public void removeRefreshToken(String token) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token);
        super.removeRefreshToken(token);
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token+" authentication="+authentication);
        super.storeAccessToken(token,authentication);
    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter refreshToken="+refreshToken+" authentication="+authentication);
        super.storeRefreshToken(refreshToken,authentication);
    }









}
