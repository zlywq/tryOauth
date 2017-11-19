package g1.oauthlogin.sprsec;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import org.apache.log4j.Logger;


/**
 * 一个自定义的service用来和数据库进行操作. 即以后我们要通过数据库保存权限.则需要我们继承UserDetailsService
 * 
 * 
 * 
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserInfoService userInfoService;//TODO 改到auth db或datasource
	@Autowired
	UtilService utilService;

	public UserDetails loadUserByUsername(String userUniqueToken) throws UsernameNotFoundException {
		return loadUserByUsername(userUniqueToken,false);
	}

	/*
	 * isCreating是针对读写分离环境下，刚写就马上读，一般读不到最新数据的问题。解决办法是读主库。
	 */
	public UserDetails loadUserByUsername(String userUniqueToken, boolean isCreating) throws UsernameNotFoundException {
		utilService.log(logger, ""+this.getClass().getSimpleName()+"."+Util1.getMethodName()+" enter userUniqueToken="+userUniqueToken+", isCreating="+isCreating);
	
		UserInfo userInfo = null;
		if (isCreating){
			//
			userInfo = userInfoService.getUserByUniqueTokenInTran(userUniqueToken);
		}else{
			userInfo = userInfoService.getUserByUniqueToken(userUniqueToken);
		}
		
		if (userInfo == null){
			throw new UsernameNotFoundException("找不到用户信息");
		}
//		if (userInfo.getIsDisabled()==1){
//			throw new UsernameNotFoundException("用户被禁用");
//		}
		UserLogin userLogin = null;
		if (isCreating){
			userLogin = userInfoService.getUserLoginByUserIdInTran(userInfo.getUserId());
		}else{
			userLogin = userInfoService.getUserLoginByUserId(userInfo.getUserId());
		}
		
		if (userLogin == null){
			throw new UsernameNotFoundException("找不到用户登录信息");
		}
		
		UserDetails user = null;
		Collection<GrantedAuthority> authorities = getAuthorities(userInfo);
		user = new User(userInfo.getUserId()+"", userLogin.getPassword(), true, true, true, true,authorities);
		return user;
	}


	public Collection<GrantedAuthority> getAuthorities(UserInfo userInfo) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
		// 所有的用户默认拥有ROLE_USER权限
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		//SpringBoot四大神器之Actuator https://segmentfault.com/a/1190000004318360?_ea=568366 
		//management.context-path=/actuator
		authList.add(new SimpleGrantedAuthority("ACTUATOR"));//for ACTUATOR
		
//		if (xxx) {
//			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//		}
		return authList;
	}
	
	

//	public static class SecurityUser extends UserInfo implements UserDetails
//	{
//	    private static final long serialVersionUID = 1L;
//	    public SecurityUser(UserInfo user) {
//	    	Util1.copyPojoFieldsSimple(user, this);
//	    }
//	    @Override
//	    public Collection<? extends GrantedAuthority> getAuthorities() {......
//	        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//	        List<Role> roles = this.getRoles();
//	        if(roles != null)
//	        {
//	            for (Role role : roles) {
//	                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
//	                authorities.add(authority);
//	            }
//	        }
//	        return authorities;
//	    }
//	    @Override
//	    public String getPassword() {
//	        return super.getPassword();...TODO
//	    }
//	    @Override
//	    public String getUsername() {
//	        return super.getNickName();
//	    }
//	    @Override
//	    public boolean isAccountNonExpired() {
//	        return true;
//	    }
//	    @Override
//	    public boolean isAccountNonLocked() {
//	        return true;
//	    }
//	    @Override
//	    public boolean isCredentialsNonExpired() {
//	        return true;
//	    }
//	    @Override
//	    public boolean isEnabled() {
//	        return true;
//	    }
//	}

	
	
	
	
	
	
	
	
}
