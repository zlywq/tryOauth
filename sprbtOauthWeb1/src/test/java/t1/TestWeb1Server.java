package t1;


import com.alibaba.fastjson.JSON;
import g1.ClassInTopPackage;
import g1.libcmn.domain.BbsPost;
import g1.libcmn.util.Const;
import g1.libcmn.util.Util1;
import g1.libcmn.util.UtilMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

Unable to start EmbeddedWebApplicationContext due to missing EmbeddedServletContainerFactory bean.
    参考 第一次启动springboot的辛酸历程 http://blog.csdn.net/zhang168/article/details/51423905

注意这里需要auth server先启动起来

发现header中的XSRF-TOKEN也很必要。另外，好像只有post之后才改变。

 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)//
public class TestWeb1Server {

    @SpringBootApplication(scanBasePackageClasses = {TestWeb1Server.class,ClassInTopPackage.class},exclude = {MongoAutoConfiguration.class})
    public static class Configuration1{

    }

//    @Value("${server.port}")
//    private int port;

//    @LocalServerPort
//    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Value("${myOauthServer.hostUrlBeforeProxy}")
    private String myOauthServer_hostUrlBeforeProxy;
    @Value("${myOauthResServer.hostUrl}")
    String myOauthResServer_hostUrl;

    @Autowired
    WebTool webTool;


    /*
    这里需要auth server预先启动。res server用得很少，但也要预先启动。这里会启动web server。
    这里模仿网页上从访问web server首页开始所必须的http请求，直到登录成功再成功访问这个首页，然后去访问res server取点数据。
    这里发现header中的XSRF-TOKEN也很必要。另外，好像只有post之后才改变。
    另外，在初次访问auth server时，不知为何不生成。后来跟踪代码，发现与Accept有关。（也许之前accept的主要是json格式，被当成rest方式了?...）。
    还有，fiddler抓取的http交互log是一个很好的帮手。参见sprbtOauthWeb1/src/test/some/TestWeb1Server_test1.saz 。

    这里试验的是
    insert into oauth_client_details
(client_id, resource_ids, client_secret, scope, authorized_grant_types,
web_server_redirect_uri,authorities, access_token_validity,refresh_token_validity, additional_information,
autoapprove, create_time, archived, trusted)
values
('client1','client1-resource', 'client1secret', 'read,write,openid','authorization_code,refresh_token,password,implicit',
null,'ROLE_CLIENT',null, null,null,  '',now(), 0, 0)
     */
    @Test
    public void test1(){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter");
        String path1Main = "/";
        ResponseEntity<String> responseEntityMain1 = webTool.getForString(path1Main);
        logger.info("responseEntityMain1="+responseEntityMain1);
//<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[JSESSIONID=A5D11EA1AAC43B9AE075F9FD5348DF62;path=/;HttpOnly, XSRF-TOKEN=4261e845-558a-4983-8f95-7feb8a46e05e;path=/], Location=[http://localhost:10082/logreg/login], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
        assert (responseEntityMain1.getStatusCode().value()==302);
        URI uriLogin = responseEntityMain1.getHeaders().getLocation();
        String sUrlLogin = uriLogin.toString();
        assert ( uriLogin.getPath().equals("/logreg/login") );
        List<String> cookiesWeb1b = responseEntityMain1.getHeaders().get(HttpHeaders.SET_COOKIE);

        HttpHeaders headersWeb2a = new HttpHeaders();
        WebTool.httpHeaders_add(headersWeb2a,HttpHeaders.COOKIE,cookiesWeb1b,false);
        ResponseEntity<String> responseEntityLogin = webTool.getForString(sUrlLogin,headersWeb2a);
        logger.info("responseEntityLogin="+responseEntityLogin);
/*
<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[XSRF-TOKEN=4261e845-558a-4983-8f95-7feb8a46e05e;path=/], Location=[http://win7base:10080/oauth/authorize?client_id=client1&redirect_uri=http://localhost:10082/logreg/login&response_type=code&state=fLi1xU], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
 */
        assert (responseEntityLogin.getStatusCode().value()==302);
        URI uriOauthAuthorize = responseEntityLogin.getHeaders().getLocation();
        String sUrlOauthAuthorize = uriOauthAuthorize.toString();
        assert ( sUrlOauthAuthorize.contains(myOauthServer_hostUrlBeforeProxy) );
        List<String> cookiesWeb2b = responseEntityLogin.getHeaders().get(HttpHeaders.SET_COOKIE);

        HttpHeaders headersAuth3a = new HttpHeaders();
        headersAuth3a.add(HttpHeaders.ACCEPT,"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");//在浏览器访问会生成SESSIONID，但是这里java访问就不生成，不知为何（也许accept的是json格式..?）。后来跟踪代码，发现与Accept有关。
//        WebTool.httpHeaders_add(headersAuth3a,HttpHeaders.COOKIE,cookiesWeb2b,false);
        WebTool.httpHeaders_add(headersAuth3a,HttpHeaders.COOKIE,cookiesWeb1b,true);
        ResponseEntity<String> responseEntityOauthAuthorize = webTool.getForString(sUrlOauthAuthorize, headersAuth3a);
        logger.info("responseEntityOauthAuthorize="+responseEntityOauthAuthorize);
//<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[XSRF-TOKEN=cff126cb-4082-4b87-82bf-350021b36b76;path=/, SESSIONID=FD5C7085081E0EDEA3FD0C635EDFA7ED;path=/;HttpOnly], Location=[http://win7base:10080/logreg/login], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
        assert (responseEntityOauthAuthorize.getStatusCode().value()==302);
        URI uriOauthLogin = responseEntityOauthAuthorize.getHeaders().getLocation();
        String sUrlOauthLogin = uriOauthLogin.toString();
        assert ( uriOauthLogin.getPath().equals("/logreg/login") );
        List<String> cookiesAuth3b = responseEntityOauthAuthorize.getHeaders().get(HttpHeaders.SET_COOKIE);


        HttpHeaders headersAuth4a = new HttpHeaders();
        WebTool.httpHeaders_add(headersAuth4a,HttpHeaders.COOKIE,cookiesAuth3b,false);
        WebTool.httpHeaders_add(headersAuth4a,HttpHeaders.COOKIE,cookiesWeb1b,true);
        ResponseEntity<String> responseEntityOauthLogin = webTool.getForString(sUrlOauthLogin,headersAuth4a);
        logger.info("responseEntityOauthLogin="+responseEntityOauthLogin);
/*<200 OK,<!DOCTYPE html>
..
</html>,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Content-Type=[text/html;charset=UTF-8], Content-Language=[zh-CN], Transfer-Encoding=[chunked], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
*/
        assert (responseEntityOauthLogin.getStatusCode().value()==200);
        List<String> cookiesAuth4b = responseEntityOauthLogin.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders headersAuth5a = new HttpHeaders();
        WebTool.httpHeaders_add(headersAuth5a,HttpHeaders.COOKIE,cookiesAuth4b,false);
        WebTool.httpHeaders_add(headersAuth5a,HttpHeaders.COOKIE,cookiesAuth3b,false);//XSRF-TOKEN也很重要
        Matcher csrfMatcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*").matcher(responseEntityOauthLogin.getBody());
        boolean csrfIsMatch = csrfMatcher.matches();
        assert (csrfIsMatch);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("username", "111");
        form.add("password", "aaa");
        form.add("_csrf", csrfMatcher.group(1));
        ResponseEntity<String> responseEntityOauthLoginPost = webTool.postForString(sUrlOauthLogin,form,headersAuth5a);
        logger.info("responseEntityOauthLoginPost="+responseEntityOauthLoginPost);
//<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[SESSIONID=7C291E75BA16AF5E28015EC86EB721A4;path=/;HttpOnly, XSRF-TOKEN=;Max-Age=0;path=/, XSRF-TOKEN=174797aa-154a-446a-86f2-ed52c95541a9;path=/], Location=[http://win7base:10080/oauth/authorize?client_id=client1&redirect_uri=http://localhost:10082/logreg/login&response_type=code&state=fLi1xU], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
        assert (responseEntityOauthLoginPost.getStatusCode().value()==302);
        //String cookieAuth5b = responseEntityOauthLoginPost.getHeaders().getFirst("Set-Cookie");
        List<String> cookiesAuth5b = responseEntityOauthLoginPost.getHeaders().get(HttpHeaders.SET_COOKIE);
        URI uri5b = responseEntityOauthLoginPost.getHeaders().getLocation();
        String sUrl5b = uri5b.toString();
        assert ( uri5b.getPath().equals("/oauth/authorize") );

        HttpHeaders headersAuth6a = new HttpHeaders();
        WebTool.httpHeaders_add(headersAuth6a,HttpHeaders.COOKIE,cookiesAuth5b,false);
        String sUrlAuth6 = sUrl5b;
        ResponseEntity<String> responseEntityAuth6 = webTool.getForString(sUrlAuth6,headersAuth6a);
        logger.info("responseEntityAuth6="+responseEntityAuth6);
        assert (responseEntityAuth6.getStatusCode().value()==200);
/*
<200 OK,<html>
..
		<form id="confirmationForm" name="confirmationForm" action="../oauth/authorize" method="post">
			<input name="user_oauth_approval" value="true" type="hidden" />
			<input type="hidden" id="csrf_token" name="_csrf" value="3997b17c-5bb1-42d7-a221-69eb59992688" />
			<input type="submit" value="Approve" />
		</form>
		<form id="denyForm" name="confirmationForm" action="../oauth/authorize" method="post">
			<input name="user_oauth_approval" value="false" type="hidden" />
			<input type="hidden" id="csrf_token" name="_csrf" value="3997b17c-5bb1-42d7-a221-69eb59992688" />
			<input type="submit" value="Deny" />
		</form>
..
</html>,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Content-Type=[text/html;charset=UTF-8], Content-Language=[zh-CN], Transfer-Encoding=[chunked], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
 */
        List<String> cookiesAuth6b = responseEntityAuth6.getHeaders().get(HttpHeaders.SET_COOKIE);
        String csrf6b = WebTool.getCsrfInHtmlForm(responseEntityAuth6.getBody());
        assert(!StringUtils.isEmpty(csrf6b));


        MultiValueMap<String, String> form7 = new LinkedMultiValueMap<String, String>();
        form7.add("user_oauth_approval", "true");
        //form7.add("submit", "Approve");
        form7.add("_csrf", csrf6b);
        HttpHeaders headersAuth7a = new HttpHeaders();
        WebTool.httpHeaders_add(headersAuth7a,HttpHeaders.COOKIE,cookiesAuth6b,false);
        WebTool.httpHeaders_add(headersAuth7a,HttpHeaders.COOKIE,cookiesAuth5b,false);
        //WebTool.httpHeaders_add(headersAuth7a,HttpHeaders.COOKIE,cookiesAuth3b,true);
        //WebTool.httpHeaders_add(headersAuth7a,HttpHeaders.COOKIE,cookiesWeb1b,true);
        //headersAuth7a.add(HttpHeaders.COOKIE,"XSRF-TOKEN="+csrf6b);//is from cookiesAuth5b
        int pos7 = sUrlAuth6.indexOf("?");
        String sUrlAuth7 = sUrlAuth6.substring(0,pos7);
        ResponseEntity<String> responseEntityOauthApprovePost = webTool.postForString(sUrlAuth7,form7,headersAuth7a);
        logger.info("responseEntityOauthApprovePost="+responseEntityOauthApprovePost);
        assert (responseEntityOauthApprovePost.getStatusCode().value()==302);
        //<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Location=[http://localhost:10082/logreg/login?code=h8LdPs&state=fLi1xU], Content-Language=[zh-CN], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:44 GMT]}>
        URI uri7blogin = responseEntityOauthApprovePost.getHeaders().getLocation();
        String sUrl7blogin = uri7blogin.toString();
        assert ( uri7blogin.getPath().equals("/logreg/login") );

        HttpHeaders headersAuth8a = new HttpHeaders();
        WebTool.httpHeaders_add(headersAuth8a,HttpHeaders.COOKIE,cookiesWeb1b,false);
        ResponseEntity<String> responseEntityWebLogin8 = webTool.getForString(sUrl7blogin, headersAuth8a);
        logger.info("responseEntityWebLogin8="+responseEntityWebLogin8);
//<302 Found,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[XSRF-TOKEN=4261e845-558a-4983-8f95-7feb8a46e05e;path=/, JSESSIONID=73084409568409A28FAD8EB966C68B7E;path=/;HttpOnly], Location=[http://localhost:10082/], Content-Length=[0], Date=[Sat, 25 Nov 2017 13:41:45 GMT]}>
        assert (responseEntityWebLogin8.getStatusCode().value()==302);
        URI uri8bWebHome = responseEntityWebLogin8.getHeaders().getLocation();
        String sUrl8bWebHome = uri8bWebHome.toString();
        assert ( uri8bWebHome.getPath().equals(path1Main) );
        List<String> cookies8bWebHome = responseEntityWebLogin8.getHeaders().get(HttpHeaders.SET_COOKIE);


        HttpHeaders headers9aWeb = new HttpHeaders();
        WebTool.httpHeaders_add(headers9aWeb,HttpHeaders.COOKIE,cookies8bWebHome,false);
        ResponseEntity<String> responseEntity9Web = webTool.getForString(sUrl8bWebHome, headers9aWeb);
        logger.info("responseEntity9Web="+responseEntity9Web);
/*
<200 OK,......,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate, no-store], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[XSRF-TOKEN=b1df26ce-58cc-4cf1-b3e9-0bcbbb8198bf;path=/], Content-Type=[application/json;charset=UTF-8], Content-Language=[zh-CN], Transfer-Encoding=[chunked], Date=[Sat, 25 Nov 2017 13:41:45 GMT]}>
 */
        assert (responseEntity9Web.getStatusCode().value()==200);

        HttpHeaders headers10aWeb = new HttpHeaders();
        WebTool.httpHeaders_add(headers10aWeb,HttpHeaders.COOKIE,cookies8bWebHome,false);
        ResponseEntity<String> responseEntity10Web = webTool.getForString("/getOauthTokens", headers10aWeb);
        logger.info("responseEntity10Web="+responseEntity10Web);
/*
<200 OK,......,{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Set-Cookie=[XSRF-TOKEN=b1df26ce-58cc-4cf1-b3e9-0bcbbb8198bf;path=/], Content-Type=[application/json;charset=UTF-8], Transfer-Encoding=[chunked], Date=[Sat, 25 Nov 2017 13:41:45 GMT]}>

 */
        assert (responseEntity10Web.getStatusCode().value()==200);
        String strBody10b = responseEntity10Web.getBody();
        assert (strBody10b.contains("accessToken"));
        HashMap data10b =  JSON.parseObject(strBody10b,HashMap.class);
        String accessToken10 = (String) data10b.get("accessToken");
        assert (!StringUtils.isEmpty(accessToken10));

        String url20Res = myOauthResServer_hostUrl + "/res1/m1"+ "?access_token="+accessToken10;
        ResponseEntity<String> responseEntity20Res = webTool.getForString(url20Res);
        logger.info("responseEntity20Res="+responseEntity20Res);
/*
<200 OK,{"dt":"Sat Nov 25 21:41:47 CST 2017","success":true,"tm":1511617307316},{X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY], Content-Type=[application/json;charset=UTF-8], Transfer-Encoding=[chunked], Date=[Sat, 25 Nov 2017 13:41:47 GMT]}>
 */
        assert (responseEntity20Res.getStatusCode().value()==200);

    }


    /*
    这里是采用grant_type=password的方式。
     */
    @Test
    public void test2password() {
        logger.info("" + Util1.getCurrentClassName() + "." + Util1.getCurrentMethodName() + " enter");

        MultiValueMap<String, String> form1 = new LinkedMultiValueMap<String, String>();
        form1.add("grant_type", "password");
        form1.add("username", "111");
        form1.add("password", "aaa");

        HttpHeaders headersAuth1a = new HttpHeaders();
        String basicAuth = "client1:client1secret";
        String b64BasicAuth = Base64.getEncoder().encodeToString(basicAuth.getBytes());
        logger.info("" + Util1.getCurrentClassName() + "." + Util1.getCurrentMethodName() + " b64BasicAuth="+b64BasicAuth);
                //.encodeToString(basicAuth.getBytes("utf-8"));
        headersAuth1a.add(HttpHeaders.AUTHORIZATION,"Basic "+b64BasicAuth);

        String sUrlAuth1 = myOauthServer_hostUrlBeforeProxy+"/oauth/token";
        ResponseEntity<String> responseEntityOauthToken = webTool.postForString(sUrlAuth1,form1,headersAuth1a);
        logger.info("responseEntityOauthToken="+responseEntityOauthToken);
        assert (responseEntityOauthToken.getStatusCode().value()==200);
        String strBody1b = responseEntityOauthToken.getBody();
        HashMap data1b =  JSON.parseObject(strBody1b,HashMap.class);
        String accessToken1 = (String) data1b.get("access_token");
        assert (!StringUtils.isEmpty(accessToken1));

        String url20Res = myOauthResServer_hostUrl + "/res1/m1"+ "?access_token="+accessToken1;
        ResponseEntity<String> responseEntity20Res = webTool.getForString(url20Res);
        logger.info("responseEntity20Res="+responseEntity20Res);
        assert (responseEntity20Res.getStatusCode().value()==200);

    }







}
