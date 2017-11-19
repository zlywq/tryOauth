package g1.libweb.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
本配置或者本模块用于sso的web客户端
 */

//TODO USE CHILD class 
//@Configuration
//@EnableOAuth2Sso
//@EnableConfigurationProperties(SecuritySettings.class)
public abstract class BaseSecurityConfigForSso extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    protected SecuritySettings settings;

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
        http
        	.antMatcher("/**").authorizeRequests()
        	.antMatchers(settings.getPermitall().split(",")).permitAll()
        	.anyRequest().authenticated()
        	.and().csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher())
                .csrfTokenRepository(csrfTokenRepository())
            .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)

            .logout().logoutUrl(settings.getLogouturl()).permitAll().logoutSuccessUrl(settings.getLogoutsuccssurl())

            .and().exceptionHandling()
            	//.accessDeniedPage(settings.getDeniedpage())//这个accessDeniedPage并不一定是被deny，有时候出错了本应该进入error页面，却进入到这里而看不到错误信息
            ;
        
        // @formatter:on
    }

//这个customFilter()不知遵循什么规则自动加入到spring security里面去了，暂且不要，以后再考虑......
//    @Bean
//    public CustomFilterSecurityInterceptor customFilter() throws Exception{
//        CustomFilterSecurityInterceptor customFilter = new CustomFilterSecurityInterceptor();
//        customFilter.setSecurityMetadataSource(securityMetadataSource());
//        customFilter.setAccessDecisionManager(accessDecisionManager());
//        customFilter.setAuthenticationManager(authenticationManager);
//        return customFilter;
//    }
//    @Bean
//    public CustomAccessDecisionManager accessDecisionManager() {
//        return new CustomAccessDecisionManager();
//    }
//    @Bean
//    public CustomSecurityMetadataSource securityMetadataSource() {
//        return new CustomSecurityMetadataSource(settings.getUrlroles());
//    }


    protected CsrfSecurityRequestMatcher csrfSecurityRequestMatcher(){
        CsrfSecurityRequestMatcher csrfSecurityRequestMatcher = new CsrfSecurityRequestMatcher();
        List<String> list = new ArrayList<String>();
        list.add("/rest/");
        csrfSecurityRequestMatcher.setExecludeUrls(list);
        return csrfSecurityRequestMatcher;
    }

    protected Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = new Cookie("XSRF-TOKEN",csrf.getToken());
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    protected CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
    
    
    
    
    
    
    
    
    
}
