package g1.oauthlogin.oauth;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.ConcurrentHashMap;


/*
参考 InMemoryAuthorizationCodeServices 和 P3AuthorizationCodeServices 的研究结果，
这里应该实现为使用时效性的缓存，如guava，以防内存不断被意外占用。
而InMemoryAuthorizationCodeServices没有这个清理措施，很有可能不能释放的内存越来越多。
当然，还得再研究跟踪源代码以决定。TODO ......
 */
public class MyInMemoryAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore = new ConcurrentHashMap<String, OAuth2Authentication>();

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        this.authorizationCodeStore.put(code, authentication);
    }

    @Override
    public OAuth2Authentication remove(String code) {
        OAuth2Authentication auth = this.authorizationCodeStore.remove(code);
        return auth;
    }

}

