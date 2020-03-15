package com.zcx.demo.shiroweb.vo;

import lombok.Data;

@Data
public class User {
    private String username;
    private String password;
    private boolean rememberMe;
    private String salt;
}
