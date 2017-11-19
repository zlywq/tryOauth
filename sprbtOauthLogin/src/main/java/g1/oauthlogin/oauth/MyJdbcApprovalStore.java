package g1.oauthlogin.oauth;

import g1.libcmn.util.Util1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class MyJdbcApprovalStore extends JdbcApprovalStore {


    final Logger logger             = LoggerFactory.getLogger(getClass());

    public MyJdbcApprovalStore(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public List<Approval> getApprovals(String userName, String clientId) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter userName="+userName+" clientId="+clientId);
        return super.getApprovals(userName,clientId);
    }


    @Override
    public boolean addApprovals(final Collection<Approval> approvals) {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter approvals="+approvals);
        return super.addApprovals(approvals);
    }


}
