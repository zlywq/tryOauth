package g1.oauthlogin.oauth;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;


/**
 * 扩展 默认的 ClientDetailsService, 增加逻辑删除判断( archived = 0)
 * SQL中添加  <i>archived = 0</i> 条件
 * <p/>
 * 增加缓存支持
 *
 * @author Shengzhao Li
 */
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {

    final Logger logger             = LoggerFactory.getLogger(getClass());

    private static final String SELECT_CLIENT_DETAILS_SQL = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, " +
            "web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove " +
            "from oauth_client_details where client_id = ? and archived = 0 ";


    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
        setSelectClientDetailsSql(SELECT_CLIENT_DETAILS_SQL);//...
    }


    @Override
    //@Cacheable(value = CLIENT_DETAILS_CACHE, key = "#clientId")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientId="+clientId);
        return super.loadClientByClientId(clientId);
    }


    @Override
    //@CacheEvict(value = CLIENT_DETAILS_CACHE, key = "#clientDetails.getClientId()")
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientDetails="+clientDetails);
        super.updateClientDetails(clientDetails);
    }

    @Override
    //@CacheEvict(value = CLIENT_DETAILS_CACHE, key = "#clientId")
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientId="+clientId+" secret="+secret);
        super.updateClientSecret(clientId, secret);
    }

    @Override
    //@CacheEvict(value = CLIENT_DETAILS_CACHE, key = "#clientId")
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter clientId="+clientId);
        super.removeClientDetails(clientId);
    }
}