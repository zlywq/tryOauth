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
跟踪了一下，
在auth server的login界面提交后，login成功后，
 会先重定向到/oauth/authorize，这是在AuthorizationEndpoint里面处理，此时会生成code，并调用这里的store()。
 然后，成功后会重定向到web1 server的login地址，附上生成的code。
 然后，web1 server使用这个code去访问/oauth/token，以post方式，此时由auth server的TokenEndpoint.postAccessToken处理。
 在TokenEndpoint.postAccessToken里面，是要消费掉这个code并生成access_token和refresh_token等的，消费的时候会调用这里的remove().
 注意，这个remove是先取回再删除。
 按上面的理解的逻辑，感觉可以放在内存或缓存里面了，这样速度快一些。但是，如果放在内存，需要研究如果由于特殊情况生成后未消费，是不是就无法释放内存。......
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
