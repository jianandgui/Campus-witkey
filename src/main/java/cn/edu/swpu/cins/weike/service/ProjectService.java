package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.exception.StudentException;
import cn.edu.swpu.cins.weike.exception.TeacherException;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface ProjectService {


    List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException;

    ProjectDetail showProject(String projectName) throws ProjectException;

    List<ProjectView> queryByKeyWords(String keyWords) throws ProjectException;

    List<IndexVO>  queryForIndex() throws ProjectException;

    ProApplyInfo queryProApplyInfoByName(String projectName) throws ProjectException;

}
