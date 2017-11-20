package g1.oauthlogin.config;

import g1.oauthlogin.oauth.*;
import g1.oauthlogin.sprsec.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.*;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;
/*
//REFER D:\zlyt\demoBig\spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java
经过试验，有些表在使用jwt之后是用不着的 。参考dbSchema.txt里面的说明。
而使用jwt相比不使用jwt，有一定的优势，参考 Oauth2Web1Application.java 里面的说明。
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${myflag.ifUseJwt}")
    boolean myflag_ifUseJwt;
    @Value("${myflag.ifJwtSimple}")
    boolean myflag_ifJwtSimple;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    //@Qualifier("authDataSource")
    DataSource dataSource;

    private void t1(){
        TokenKeyEndpoint a;
        //TokenEndpoint b;
        CheckTokenEndpoint c;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //暂且不要，以后使用配置选择要不要
        endpoints.accessTokenConverter(accessTokenConverter()); //如果没有这条语句，会导致Authentication Failed: Could not obtain user details from token

        //如果要支持grant_type=password，需要设置authenticationManager，并且这个bean需要设置userDetailsService。
        //但看来这个authenticationManager已经在别处生成配置好了，这里只需要简单使用即可。

        endpoints.setClientDetailsService(clientDetailsService());
        endpoints.tokenStore(tokenStore());



        endpoints.userApprovalHandler(userApprovalHandler());
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenServices( defaultTokenServices());

        endpoints.authorizationCodeServices(authorizationCodeServices());
        endpoints.approvalStore(approvalStore());

        endpoints.userDetailsService(myUserDetailsService);


    }

    /*
注意，
    虽然spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java 里面是private方法，
    但是这里必须是bean。不然其他地方如果有引用，则没有办法自动注入，而导致隐含的错误。
        比如 TokenKeyEndpoint 就引用了JwtAccessTokenConverter。而且还要求bean的返回值是JwtAccessTokenConverter类型，而不能是AccessTokenConverter，否则也不能自动注入。!!!!!!!!!!!...................

     */
    //如果没有这个JwtAccessTokenConverter的bean，会导致使用jwt的web app端对 /oauth/token_key 的访问处理出错，从而导致web1启动时访问这个url出错而无法启动。
    @ConditionalOnProperty(value = {"myflag.ifUseJwt"},matchIfMissing = false)
    @Bean  //ok
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = null;
        if (myflag_ifJwtSimple) {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter(); //ok
            converter.setSigningKey("123");
            accessTokenConverter = converter;
        }else{
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter(); //ok
            KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("serverKeystore.jks"), "123456".toCharArray())
                    .getKeyPair("alias1", "123456".toCharArray());
            converter.setKeyPair(keyPair);
            accessTokenConverter = converter;
        }
        return accessTokenConverter;
    }
    @ConditionalOnExpression("${myflag.ifUseJwt}==false")
    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        return converter;
    }
    private AccessTokenConverter accessTokenConverter() {
        AccessTokenConverter accessTokenConverter = null;
        if (myflag_ifUseJwt){
            accessTokenConverter = jwtAccessTokenConverter();
        }else{
            accessTokenConverter = defaultAccessTokenConverter();
        }
        if (accessTokenConverter == null) {
            throw new RuntimeException("err for accessTokenConverter be null");
        }
        return accessTokenConverter;
    }
    //ok
//    @Autowired
//    AccessTokenConverter m_accessTokenConverter;
//    private AccessTokenConverter accessTokenConverter() {
//        return m_accessTokenConverter;
//    }

    @Bean
    public TokenStore tokenStore(){
        TokenStore tokenStore = null;
        if (myflag_ifUseJwt){
            JwtTokenStore store = new JwtTokenStore((JwtAccessTokenConverter)accessTokenConverter()); //ok when all jwt
            tokenStore = store;
        }else{
            ////store= new CustomJdbcTokenStore(dataSource);//暂且不用了...
            MyInMemoryTokenStore store = new MyInMemoryTokenStore();
            tokenStore = store;
        }
        return tokenStore;
    }


    @Bean
    public ClientDetailsService clientDetailsService(){
        ClientDetailsService clientDetailsService = null;
        if (clientDetailsService == null) {
            ClientDetailsService cds = new CustomJdbcClientDetailsService(dataSource);
            clientDetailsService = cds;
        }
        return clientDetailsService;
    }



    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        AuthorizationCodeServices authorizationCodeServices = null;
        if (authorizationCodeServices == null) {
            //authorizationCodeServices = new P3AuthorizationCodeServices(dataSource);
            authorizationCodeServices = new MyInMemoryAuthorizationCodeServices(); //官方源代码中也是使用的InMemoryAuthorizationCodeServices
        }
        return authorizationCodeServices;
    }



    //这里是参考官方代码的实现。本文件很多地方都是参考的官方代码。
    @Bean
    public ApprovalStore approvalStore(){
        ApprovalStore approvalStore = null;
        if (isApprovalStoreDisabled()){
            approvalStore = null;
        }else{
            approvalStore = new MyJdbcApprovalStore(dataSource);
//        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
//        tokenApprovalStore.setTokenStore(tokenStore());
//        approvalStore = tokenApprovalStore;
        }
        return approvalStore;
    }
    private boolean isApprovalStoreDisabled() {
        return (tokenStore() instanceof JwtTokenStore);
    }
    // in spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java
//    private ApprovalStore approvalStore() {
//        if (approvalStore == null && tokenStore() != null && !isApprovalStoreDisabled()) {
//            TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
//            tokenApprovalStore.setTokenStore(tokenStore());
//            this.approvalStore = tokenApprovalStore;
//        }
//        return this.approvalStore;
//    }
//    private boolean isApprovalStoreDisabled() {
//        return approvalStoreDisabled || (tokenStore() instanceof JwtTokenStore);
//    }

//    //似乎在auth server没啥用，别人的例子中也没有用到。官方的代码里也奇怪。可能是在web1 app中使用。
//    @Bean
//    public MyJdbcClientTokenServices jdbcClientTokenServices(){
//        return new MyJdbcClientTokenServices(dataSource);
//    }


    @Bean
    public OAuth2RequestFactory oAuth2RequestFactory(){
        OAuth2RequestFactory requestFactory = null;
        if (requestFactory == null) {
            DefaultOAuth2RequestFactory f = new DefaultOAuth2RequestFactory(clientDetailsService());
            requestFactory = f;
        }
        return requestFactory;
    }
    // REFER userApprovalHandler() in D:\zlyt\demoBig\spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java
    @Bean
    public UserApprovalHandler userApprovalHandler(){
        UserApprovalHandler userApprovalHandler = null;
        if (userApprovalHandler == null) {
            if (approvalStore() != null) {
                ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
                handler.setApprovalStore(approvalStore());
                handler.setRequestFactory(oAuth2RequestFactory());
                handler.setClientDetailsService(clientDetailsService());
                userApprovalHandler = handler;
            }
            else if (tokenStore() != null) {
                P3UserApprovalHandler handler = new P3UserApprovalHandler();//ok
                handler.setTokenStore(tokenStore());
                handler.setClientDetailsService(clientDetailsService());
                handler.setRequestFactory(oAuth2RequestFactory());
                userApprovalHandler = userApprovalHandler;
            }
            else {
                throw new IllegalStateException("Either a TokenStore or an ApprovalStore must be provided");
            }
        }
        return userApprovalHandler;
    }

    /* ..................TODO ...............
Description:
Method springSecurityFilterChain in org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration required a single bean, but 3 were found:
	- consumerTokenServices: defined by method 'consumerTokenServices' in class path resource [org/springframework/security/oauth2/config/annotation/web/configuration/AuthorizationServerEndpointsConfiguration.class]
	- defaultAuthorizationServerTokenServices: defined by method 'defaultAuthorizationServerTokenServices' in class path resource [org/springframework/security/oauth2/config/annotation/web/configuration/AuthorizationServerEndpointsConfiguration.class]
	- defaultTokenServices: defined by method 'defaultTokenServices' in class path resource [g1/oauthlogin/config/AuthorizationServerConfig.class]

Action:
Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
     */
    //refer: spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java
    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices ts = new DefaultTokenServices();
        ts.setTokenStore(tokenStore());
        ts.setClientDetailsService(clientDetailsService());
        ts.setSupportRefreshToken(true);
        ts.setReuseRefreshToken(false);
        ts.setTokenEnhancer(tokenEnhancer());//...............
        addUserDetailsService(ts, myUserDetailsService);
        return ts;
    }
    private TokenEnhancer tokenEnhancer() {
        TokenEnhancer tokenEnhancer = null;
        if (accessTokenConverter() instanceof JwtAccessTokenConverter) {
            tokenEnhancer = (TokenEnhancer) accessTokenConverter();
        }
        return tokenEnhancer;
    }
    //refer: spring-security-oauthGit\spring-security-oauth2\src\main\java\org\springframework\security\oauth2\config\annotation\web\configurers\AuthorizationServerEndpointsConfigurer.java
    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider> asList(provider)));
        }
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	//DataSource dataSource;
    	//clients.jdbc(dataSource);
    	
    	clients.withClientDetails(clientDetailsService());//这里的autoApprove在对应表的列上，注意意义是AutoApproveScopes。

//        clients.inMemory().withClient("ssoclient").secret("ssosecret")
//                .autoApprove(true)//注意这个如果为true，则/oauth/confirm_access页面不会显示
//                .authorizedGrantTypes("authorization_code", "refresh_token", "password").scopes("openid");
//                //.scopes("openid","read","write");
//                //.authorizedGrantTypes("password","authorization_code", "refresh_token","implicit","client_credentials");//.scopes("openid");
        
        
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
        	.allowFormAuthenticationForClients()
        	//.sslOnly();//...
        ;
        //security.passwordEncoder()
//        security.realm()
//        security.allowFormAuthenticationForClients()
//        security.authenticationEntryPoint()
//        security.accessDeniedHandler()

        //TokenEndpoint te;
    }

    
    

}
