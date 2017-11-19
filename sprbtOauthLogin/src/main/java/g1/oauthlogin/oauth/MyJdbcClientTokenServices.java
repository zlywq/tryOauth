package g1.oauthlogin.oauth;

import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;

import javax.sql.DataSource;

//似乎没啥用，别人的例子中也没有用到。官方的代码里也奇怪。根据 Persisting Tokens in a Client (OAuth 2 Developers Guide)里面的说法，是与OAuth2RestTemplate的bean搭配使用
public class MyJdbcClientTokenServices extends JdbcClientTokenServices {

    public MyJdbcClientTokenServices(DataSource dataSource) {
        super(dataSource);
    }

}
