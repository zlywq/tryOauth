package g1.oauthlogin.oauth;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;



/**
 * 2016/7/23
 *
 * @author Shengzhao Li
 */
public class P3AuthorizationCodeServices extends JdbcAuthorizationCodeServices {

    final Logger logger             = LoggerFactory.getLogger(getClass());


    public P3AuthorizationCodeServices(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    //@Cacheable(value = AUTHORIZATION_CODE_CACHE, key = "#code")
    protected void store(String code, OAuth2Authentication authentication) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter code="+code+" authentication="+authentication);
        super.store(code, authentication);
    }

    @Override
    //@CacheEvict(value = AUTHORIZATION_CODE_CACHE, key = "#code")
    public OAuth2Authentication remove(String code) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter code="+code);
        return super.remove(code);
    }
}
