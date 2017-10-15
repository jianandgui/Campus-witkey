package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.exception.ProjectException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface ProjectService {


    List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException;

    IndexVO showProject(String projectName) throws ProjectException;

    List<IndexVO> queryByKeyWords(String keyWords) throws ProjectException;

    List<IndexVO>  queryForIndex(int offset);

    ProApplyInfo queryProApplyInfoByName(String projectName) throws ProjectException;

    int deleteProByProName(String projectName, HttpServletRequest request);

    int updateProByName(String projectName,ProjectInfo projectInfo, HttpServletRequest request);



}
