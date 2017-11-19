package g1.oauthlogin.oauth;


import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;

/**
 * 处理OAuth 中用户授权确认的逻辑
 * 这在grant_type 为 authorization_code, implicit 会使用到
 *
 * @author Shengzhao Li
 */
public class P3UserApprovalHandler extends TokenStoreUserApprovalHandler {

    final Logger logger             = LoggerFactory.getLogger(getClass());


    /**
     * 这儿扩展了默认的逻辑, 若 OauthClientDetails 中的 trusted 字段为true, 将会自动跳过 授权流程
     *
     * @param authorizationRequest AuthorizationRequest
     * @param userAuthentication   Authentication
     * @return True is approved
     */
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter authorizationRequest="+authorizationRequest+" userAuthentication="+userAuthentication);

        return super.isApproved(authorizationRequest, userAuthentication);

    }

}
