package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.JoinMessage;
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.exception.WeiKeException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public interface JoinProjectService {



    int acceptJoin(JoinMessage joinMessage, HttpServletRequest request) throws Exception;

    void refuseJoin(JoinMessage joinMessage, HttpServletRequest request) throws Exception;
}
