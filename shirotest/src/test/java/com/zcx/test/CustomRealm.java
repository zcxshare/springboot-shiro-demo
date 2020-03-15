package com.zcx.test;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashedMap();
    Set<String> roles = new HashSet<String>();
    Set<String> permissions = new HashSet<String>();

    {
        userMap.put("zcx", "7a19d6ae40fc700a0db6ba99782aeef9");

        roles.add("admin");
        roles.add("user");

        permissions.add("user:update");
        permissions.add("user:delete");
        super.setName("customRealm");
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object username = principalCollection.getPrimaryPrincipal();

        Set<String> roles = getRoleByName(username);
        Set<String> permissions = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByRoles(Set<String> roles) {
        return permissions;
    }

    private Set<String> getRoleByName(Object username) {
        return roles;

    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Object username = authenticationToken.getPrincipal();
        String password = getPasswordByName(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zcx"));
        return simpleAuthenticationInfo;
    }

    private String getPasswordByName(Object username) {
        return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "zcx");
        System.out.println(md5Hash.toHex());
    }
}
