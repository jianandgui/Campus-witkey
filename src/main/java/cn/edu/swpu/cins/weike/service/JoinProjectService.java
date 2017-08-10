package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.JoinMessage;

import java.security.Principal;

public interface JoinProjectService {



    int acceptJoin(JoinMessage joinMessage, Principal principal);
    int refuseJoin(JoinMessage joinMessage, Principal principal);
}
