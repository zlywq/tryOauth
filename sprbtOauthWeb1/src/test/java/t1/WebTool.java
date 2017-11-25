package t1;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebTool {


    private final Logger logger = LoggerFactory.getLogger(getClass());


//    @LocalServerPort
//    private int port;

    @Value("${server.port}")
    private int port;

//    @Autowired
//    private TestRestTemplate restTemplate;
    private static String DEFAULT_HOST = "localhost";

    private String hostName = DEFAULT_HOST;


    public static void httpHeaders_add(HttpHeaders httpHeaders, String headerName, List<String> headerValues, boolean noXSRF_TOKEN){
        if (headerValues != null){
            for (String headerValue:headerValues) {
                boolean needAddHeader = false;
                if (headerValue.contains("JSESSIONID")){
                    String jsession = getJSessionInCookieStr(headerValue);
                    httpHeaders.add(headerName,jsession);
                }else if (headerValue.contains("SESSIONID")){
                    String session = getSessionInCookieStr(headerValue);
                    httpHeaders.add(headerName,session);
                }else if (headerValue.contains("XSRF-TOKEN")){
                    if (noXSRF_TOKEN){
                        //needAddHeader = false;
                    }else{
                        String xsrf = getXSRF_TOKENInCookieStr(headerValue);
                        if (!StringUtils.isEmpty(xsrf)){
                            httpHeaders.add(headerName,xsrf);
                        }
                    }
                }else{
                    //needAddHeader = false;
                }

            }
        }
    }

    public static String getCsrfInHtmlForm(String input){
        Matcher csrfMatcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*").matcher(input);
        boolean csrfIsMatch = csrfMatcher.matches();
        String csrf = null;
        if (csrfIsMatch){
            csrf = csrfMatcher.group(1);
        }
        return csrf;
    }

    public static String getJSessionInCookieStr(String input){
        Matcher matcher = Pattern.compile("(JSESSIONID=(\\w+));.*").matcher(input);
        boolean isMatch = matcher.matches();
        String val = null;
        if (isMatch){
            val = matcher.group(1);
        }
        return val;
    }
    public static String getSessionInCookieStr(String input){
        Matcher matcher = Pattern.compile("(SESSIONID=(\\w+));.*").matcher(input);
        boolean isMatch = matcher.matches();
        String val = null;
        if (isMatch){
            val = matcher.group(1);
        }
        return val;
    }
    public static String getXSRF_TOKENInCookieStr(String input){
        Matcher matcher = Pattern.compile("(XSRF-TOKEN=([\\w\\-]+));.*").matcher(input);
        boolean isMatch = matcher.matches();
        String val = null;
        if (isMatch){
            val = matcher.group(1);
        }
        return val;
        /*
存在这种情况
Set-Cookie: XSRF-TOKEN=;Max-Age=0;path=/
Set-Cookie: XSRF-TOKEN=bff7afd6-7f90-49a1-bef3-8951842d7984;path=/
即
XSRF-TOKEN=;Max-Age=0;path=/
XSRF-TOKEN=bff7afd6-7f90-49a1-bef3-8951842d7984;path=/
         */
    }









    RestTemplate client = null;
    public RestTemplate getRestTemplate() {

        if (this.client == null){
            client = new RestTemplate();
            client.setRequestFactory(new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    super.prepareConnection(connection, httpMethod);
                    connection.setInstanceFollowRedirects(false);
                }
            });
            client.setErrorHandler(new ResponseErrorHandler() {
                // Pass errors through in response entity for status code analysis
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    return false;
                }

                public void handleError(ClientHttpResponse response) throws IOException {
                }
            });
        }
        return client;
    }








    public String getBaseUrl() {
        return "http://" + hostName + ":" + port;
    }

    public String getUrl(String path) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+"port="+port);
        if (path.startsWith("http:")) {
            return path;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "http://" + hostName + ":" + port + path;
    }

    public ResponseEntity<String> postForString(String path, MultiValueMap<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
        return postForString(path,formData,headers);
    }
    public ResponseEntity<String> postForString(String path, MultiValueMap<String, String> formData,HttpHeaders headers) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" path="+path);
        RestTemplate restTemplate = getRestTemplate();
        return restTemplate.exchange(getUrl(path), HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, String>>(formData,headers), String.class);
    }

    public ResponseEntity<String> getForString(String path, final HttpHeaders headers) {
        RestTemplate restTemplate = getRestTemplate();
        return restTemplate.exchange(getUrl(path), HttpMethod.GET, new HttpEntity<Void>((Void) null, headers), String.class);
    }
    public ResponseEntity<String> getForString(String path) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" path="+path);
        return getForString(path, new HttpHeaders());
    }











}
