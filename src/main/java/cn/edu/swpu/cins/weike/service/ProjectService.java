package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.exception.ProjectException;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface ProjectService {


    //显示所有项目详情(只显示三个字段)
    List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException;


    //显示一个项目的详情
    ProjectDetail showProject(String projectName) throws ProjectException;


    //根据关键词搜索
    List<ProjectView> queryByKeyWords(String keyWords) throws ProjectException;

    //首页显示八个项目

    List<ProjectView> queryForIndex() throws ProjectException;

    //显示项目申请详情
    ProApplyInfo queryProApplyInfoByName(String projectName);

}
