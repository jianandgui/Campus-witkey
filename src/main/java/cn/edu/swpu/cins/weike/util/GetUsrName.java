package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by muyi on 17-6-14.
 */
@Component
public class GetUsrName {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public String AllProjects(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username=jwtTokenUtil.getUsernameFromToken(authToken);
        if (username.isEmpty() || username.equals("") || username.equals(" ")) {
            throw new AuthException(ExceptionEnum.ILLEAGEAL_OPERATION.getMsg());
        }
        return username;
    }
}
