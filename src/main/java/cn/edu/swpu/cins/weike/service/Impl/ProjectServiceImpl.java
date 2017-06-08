package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;

import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Value("${event.service.pageCount}")
    private int pageCount;


    //显示所有项目(三个字段)
    @Override
    public List<ProjectView> showProjectAll(int offset, int limit) {
        return  projectDao.queryAll(offset * pageCount, pageCount);
    }

    //查询一个项目的详细信息
    @Override
    public ProjectDetail showProject(String projectName) {
        return projectDao.queryProjectDetail(projectName);
    }

    @Override
    public List<ProjectView> queryByKeyWords(String keyWords) {
        return projectDao.queryByKeywords(keyWords);
    }


}
