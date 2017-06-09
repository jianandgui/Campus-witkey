package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.exception.ProjectException;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface ProjectService {


    //显示所有项目详情(只显示三个字段)
    public List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException;


    //显示一个项目的详情
    public ProjectDetail showProject(String projectName) throws ProjectException;


    //根据关键词搜索
    public List<ProjectView> queryByKeyWords(String keyWords) throws ProjectException;

}
