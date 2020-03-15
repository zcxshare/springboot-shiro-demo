package com.zcx.test;

import com.alibaba.druid.pool.DruidDataSource;
import jdk.nashorn.internal.parser.Token;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class ShiroTest {
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("zcx","123456","hhh");
    }
    @Test
    public void shriodemo(){

        // securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken("zcx","123456");

        System.out.println(subject.isAuthenticated());
        subject.login(token);
        System.out.println(subject.isAuthenticated());
//        subject.logout();
//        System.out.println(subject.isAuthenticated());
        subject.checkRole("hhh");


    }

    @Test
    public void shiroIniRealm(){
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken("zcx","123456");
        subject.login(token);
        subject.checkRole("admin");
        subject.checkPermission("user:update");
        System.out.println(subject.isAuthenticated());
    }
    @Test
    public void shiroJdbcRealm(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://47.105.60.135:3306/platform-shop");
        druidDataSource.setUsername("zcx");
        druidDataSource.setPassword("lovezcx");


        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setPermissionsLookupEnabled(true);
        PrincipalCollection prin = new SimplePrincipalCollection();
        jdbcRealm.onLogout(prin);
        jdbcRealm.setDataSource(druidDataSource);
        jdbcRealm.setAuthenticationQuery("select password from sys_user where username = ?");
        jdbcRealm.setUserRolesQuery("select (select role_name from sys_role where role_id = sr.role_id) from sys_user_role sr where user_id = " +
                "(select user_id from sys_user where username = ?)");
        jdbcRealm.setPermissionsQuery("select sm.perms from sys_menu sm" +
                " join sys_role_menu srm on srm.menu_id = sm.menu_id" +
                " join sys_role sr on sr.role_id = srm.role_id" +
                " where sr.role_name = ?");

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        AuthenticationToken token = new UsernamePasswordToken("zcx","123456");
        subject.login(token);
        subject.checkRole("admin");
        subject.checkPermission("user:update");
        System.out.println(subject.isAuthenticated());
    }
    @Test
    public void shiroCustomRealm(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);

        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        AuthenticationToken token = new UsernamePasswordToken("zcx","123456");
        subject.login(token);
//        subject.checkRole("admin");
//        subject.checkPermission("user:update");
//        subject.checkPermission("user:select");
        System.out.println(subject.isAuthenticated());
    }
}
