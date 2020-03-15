package com.zcx.demo.shiroweb.utils;

import com.zcx.demo.shiroweb.vo.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class EncryptHelp {
    private RandomNumberGenerator numberGenerator = new SecureRandomNumberGenerator();
    public static final String PASSWORD_ALGORITHM_NAME = "md5";
    public static final int PASSWORD_HASH_ITERATIONS = 2;

    /**
     * 注册时方便使用
    */
    public void encryptPassword(User user) {
        user.setSalt(numberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(PASSWORD_ALGORITHM_NAME, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()), PASSWORD_HASH_ITERATIONS).toHex();
        user.setPassword(newPassword);
    }
}
