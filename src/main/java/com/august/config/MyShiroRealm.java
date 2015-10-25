package com.august.config;

import com.august.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/10/26)
 * Description:
 */
public class MyShiroRealm  extends AuthorizingRealm {
    // 用于获取用户信息及用户权限信息的业务接口
    private UserService userService;

    // 获取授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        String username = (String) principals.fromRealm(
                getName()).iterator().next();

        if( username != null ){
            // 查询用户授权信息
            Collection<String> pers=userService.queryPermissions(username);
            if( pers != null && !pers.isEmpty() ){
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                for( String each:pers )
                    info.addStringPermissions( each );

                return info;
            }
        }

        return null;
    }

    // 获取认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken ) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 通过表单接收的用户名
        String username = token.getUsername();

        if( username != null && !"".equals(username) ){
            LoginAccount account = userService.get( username );

            if( account != null ){
                return new SimpleAuthenticationInfo(
                        account.getLoginName(),account.getPassword(),getName() );
            }
        }

        return null;
    }
}
