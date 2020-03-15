package com.zcx.demo.shiroweb.shiro.realm;

import com.zcx.demo.shiroweb.mapper.UserMapper;
import com.zcx.demo.shiroweb.vo.User;
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
import org.apache.shiro.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;


public class CustomRealm extends AuthorizingRealm {

    @Resource
    UserMapper userMapper;

//    Map<String, String> userMap = new HashedMap();
//    Set<String> roles = new HashSet<String>();
//    Set<String> permissions = new HashSet<String>();
//
//    {
//        userMap.put("zcx", "7a19d6ae40fc700a0db6ba99782aeef9");
//
//        roles.add("admin");
//        roles.add("user");
//
//        permissions.add("user:update");
//        permissions.add("user:delete");
//        super.setName("customRealm");
//
//    }

    /**
     * 授权的方法
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        super.setName("customRealm");
        String username = (String) principalCollection.getPrimaryPrincipal();

        List<String> roles = getRoleByName(username);
        List<String> permissions = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(new HashSet<>(roles));
        simpleAuthorizationInfo.setStringPermissions(new HashSet<>(permissions));
        return simpleAuthorizationInfo;
    }

    private List<String> getPermissionsByRoles(List<String> roles) {
        return userMapper.selectPermissionByRoles(roles);
    }

    private List<String> getRoleByName(String username) {
        System.out.println("从数据库读取数据:" + username);
        List<String> roles = userMapper.selectRoleByName(username);
        return roles;

    }

    /**
     * 身份验证方法
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = getPasswordByName(username);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(), "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return simpleAuthenticationInfo;
    }

    private User getPasswordByName(String username) {
        List<User> users = userMapper.selectUserByName(username);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "zcx");
        System.out.println(md5Hash.toHex());

    }
}
