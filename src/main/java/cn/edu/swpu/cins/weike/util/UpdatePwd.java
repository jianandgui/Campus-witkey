package cn.edu.swpu.cins.weike.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by muyi on 17-6-9.
 */
public class UpdatePwd {
    public static String updatePwd(String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePwd = encoder.encode(password);
        return encodePwd;
    }
}
