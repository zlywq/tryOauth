package g1.web1.cfg;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import g1.libweb.sec.BaseSecurityConfigForSso;
import g1.libweb.sec.SecuritySettings;


@Configuration
@EnableOAuth2Sso
@EnableConfigurationProperties(SecuritySettings.class)
public class SecurityConfigForSso extends BaseSecurityConfigForSso{

}
