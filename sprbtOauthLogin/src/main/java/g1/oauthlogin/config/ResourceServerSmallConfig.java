package g1.oauthlogin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


@Configuration
@EnableResourceServer
public class ResourceServerSmallConfig extends ResourceServerConfigurerAdapter {
    //数据库中要使用这个...不然会遇到类似 "Invalid token does not contain resource id (acme-resource)" 的错误信息在log中
    private static final String resourceId = "acme-resource";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
//        resources.stateless(true);

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/res1/**").authorizeRequests().anyRequest().authenticated();
    }







}
