package weike.service;

import weike.entity.persistence.ProjectInfo;
import weike.entity.view.ProjectView;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface ProjectService {




    //显示所有项目详情(只显示三个字段)
    public List<ProjectView> showProjectAll(int offset, int limit);



    //显示一个项目的详情
    public ProjectInfo showProject(String projectName);



}
