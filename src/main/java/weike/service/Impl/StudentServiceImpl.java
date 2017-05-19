package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import weike.dao.ProjectDao;
import weike.dao.StudentDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.StudentDetail;
import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ProjectRecommod;
import weike.entity.view.StudentDetailForLevel;
import weike.service.StudentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private ProjectDao projectDao;

    @Value("${event.service.pageCount}")
    private int pageCount;




    //学生发布项目
    @Override
    public int issueProject(ProjectInfoForSkills projectInfoForSkills) {


        ProjectInfo projectInfo =ProjectInfoForSkillsToProjectInfo(projectInfoForSkills);
        return projectDao.addProject(projectInfo);

    }




    //专门用来做DTO和Persistence的转换
    public ProjectInfo ProjectInfoForSkillsToProjectInfo(ProjectInfoForSkills projectInfoForSkills){

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
    public int addPersonal(StudentDetailForLevel studentDetailForLevel) {

        StudentDetail studentDetail;

        studentDetail=StudentDetailForLevelToStudentDetail(studentDetailForLevel);

        return studentDao.studentAddPersonal(studentDetail);


    }



    //进行两个类的转化（不得已的办法后续有好方法再采用）
    public StudentDetail StudentDetailForLevelToStudentDetail(StudentDetailForLevel studentDetailForLevel){

        int rate=studentDetailForLevel.getSkills().toArray().length;
        if(rate==1||rate==0){
            studentDetailForLevel.setLevel("D");
        }
        if(rate==2){
            studentDetailForLevel.setLevel("C");
        }
        if(rate==3){
            studentDetailForLevel.setLevel("B");
        }
        if(rate>3){

            studentDetailForLevel.setLevel("A");
        }
        //orika
        StudentDetail studentDetail=new StudentDetail();
        studentDetail.setLevel(studentDetailForLevel.getLevel());
        studentDetail.setUsername(studentDetailForLevel.getUsername());
        studentDetail.setQq(studentDetailForLevel.getQq());
        studentDetail.setSex(studentDetailForLevel.getSex());
        studentDetail.setEduBackgroud(studentDetailForLevel.getEduBackgroud());
        studentDetail.setUniversity(studentDetailForLevel.getUniversity());
        studentDetail.setImage(studentDetailForLevel.getImage());
        studentDetail.setMajorAndGrade(studentDetailForLevel.getMajorAndGrade());
        studentDetail.setEntryUniversity(studentDetailForLevel.getEntryUniversity());
        studentDetail.setLeaveUniversity(studentDetailForLevel.getLeaveUniversity());
        studentDetail.setSkills(studentDetailForLevel.getSkills().toString());
        studentDetail.setExperience(studentDetailForLevel.getExperience());
        studentDetail.setSelfFeel(studentDetailForLevel.getSelfFeel());

        return studentDetail;

    }

    @Override
    public List<ProjectRecommod> queryForReCommod(List<String> skills) {

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
