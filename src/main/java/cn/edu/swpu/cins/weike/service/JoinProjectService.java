package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.JoinMessage;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public interface JoinProjectService {



    int acceptJoin(JoinMessage joinMessage, HttpServletRequest request);
    void refuseJoin(JoinMessage joinMessage, HttpServletRequest request);
}
