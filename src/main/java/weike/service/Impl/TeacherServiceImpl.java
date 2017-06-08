package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weike.dao.ProjectDao;
import weike.dao.StudentDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ProjectRecommod;
import weike.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private StudentDao studentDao;


    @Override
    public int teacherAddPersonal(TeacherDetail teacherDetail) {
        return teacherDao.teacherAddPersonal(teacherDetail);
    }

    //老师发布项目
    @Override
    public int issueProject(ProjectInfoForSkills projectInfoForSkills) {
        ProjectInfo projectInfo =ProjectInfoForSkillsToProjectInfoOfTeacher(projectInfoForSkills);
        return projectDao.addProject(projectInfo);
    }

    //专门用来做DTO和Persistence的转换
    public ProjectInfo ProjectInfoForSkillsToProjectInfoOfTeacher(ProjectInfoForSkills projectInfoForSkills){

        ProjectInfo projectInfo =new ProjectInfo();
        projectInfo.setProjectName(projectInfoForSkills.getProjectName());
        projectInfo.setProjectKind(projectInfoForSkills.getProjectKind());
        projectInfo.setProjectConnector(projectInfoForSkills.getProjectConnector());
        projectInfo.setQq(projectInfoForSkills.getQq());
        projectInfo.setEmail(projectInfoForSkills.getEmail());
        projectInfo.setNumNeed(projectInfoForSkills.getNumNeed());
        projectInfo.setProjectNeed(projectInfoForSkills.getProjectNeed().toString());
        projectInfo.setProjectStart(projectInfoForSkills.getProjectStart());
        projectInfo.setProjectEnd(projectInfoForSkills.getProjectEnd());
        projectInfo.setProjectProfile(projectInfoForSkills.getProjectProfile());
        return projectInfo;
    }






    @Override
    public List<ProjectRecommod> queryStudentForReCommod(List<String> skills) {
        List<ProjectRecommod> list;
        list =new ArrayList<ProjectRecommod>();
        for (String skill: skills){
            list.addAll(studentDao.queryAllRecommod(skill));

        }
        //对List去重处理
        List<ProjectRecommod> list1=new ArrayList<ProjectRecommod>();
        int num=0;
        for(ProjectRecommod projectRecommod:list){
            String username=projectRecommod.getUsername();
            for(ProjectRecommod projectRecommod1:list1) {
                if(username.equals(projectRecommod1.getUsername())) {
                    num++;
                }
            }
            if(num==0) {
                list1.add(projectRecommod);
            }
        }
        return list1;
    }



}
