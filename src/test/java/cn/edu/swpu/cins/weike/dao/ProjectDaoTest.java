package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by muyi on 17-7-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectDaoTest {

    @Autowired
    private ProjectDao projectDao;



    @Test
    public void addProject() throws Exception {
        List<String> list=new ArrayList<String>();
        list.add("Spring");
        ProjectInfo projectInfo=new ProjectInfo();
        projectInfo.setProjectName("test55555");
        projectInfo.setEmail("test");
        projectInfo.setNumNeed(8);
        projectInfo.setProjectConnector("test");
        projectInfo.setProjectEnd(132131);
        projectInfo.setProjectKind("tset");
        projectInfo.setProjectStart(1231321);
        projectInfo.setProjectProfile("asas");
        projectInfo.setProjectNeed(list);
        projectInfo.setQq(123131);

        Assert.assertEquals(1,projectDao.addProject(projectInfo));
    }

    @Test
    public void queryAll() throws Exception {
        System.out.println(projectDao.queryAll(0,10));
    }

    @Test
    public void queryProjectDetail() throws Exception {

    }

    @Test
    public void deleteByName() throws Exception {

    }

    @Test
    public void queryByKeywords() throws Exception {

    }

    @Test
    public void queryForIndex() throws Exception {

    }

}