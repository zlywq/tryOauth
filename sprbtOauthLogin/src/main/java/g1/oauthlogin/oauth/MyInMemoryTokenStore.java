package g1.oauthlogin.oauth;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.Collection;

/*
如果使用jwt，这个则用不着了。
 */
public class MyInMemoryTokenStore extends InMemoryTokenStore {



    final Logger logger             = LoggerFactory.getLogger(getClass());

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientId="+clientId);
        return super.findTokensByClientId(clientId);
    }
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientId="+clientId+" userName="+userName);
        return super.findTokensByClientIdAndUserName(clientId,userName);
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter authentication="+authentication);
        return super.getAccessToken(authentication);
    }


    public OAuth2AccessToken readAccessToken(String tokenValue) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter tokenValue="+tokenValue);
        return super.readAccessToken(tokenValue);
    }
    public void removeAccessToken(String tokenValue) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter tokenValue="+tokenValue);
        super.removeAccessToken(tokenValue);
    }
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token+" authentication="+authentication);
        super.storeAccessToken(token,authentication);
    }


    public OAuth2RefreshToken readRefreshToken(String token) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token);
        return super.readRefreshToken(token);
    }
    public void removeRefreshToken(String token) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter token="+token);
        super.removeRefreshToken(token);
    }
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter refreshToken="+refreshToken+" authentication="+authentication);
        super.storeRefreshToken(refreshToken,authentication);
    }


}
