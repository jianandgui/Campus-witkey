package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by muyi on 17-6-14.
 */
@Component
public class GetUsrName{

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public String AllProjects(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        return jwtTokenUtil.getUsernameFromToken(authToken);
    }
}
