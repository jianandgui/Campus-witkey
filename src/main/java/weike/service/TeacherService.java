package weike.service;

import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ProjectRecommod;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface TeacherService {



    public int teacherAddPersonal(TeacherDetail teacherDetail);


    //老师发布项目
    public int issueProject(ProjectInfoForSkills projectInfoForSkills);

    //TODO
    //老师发送邮件给学生

    //发布项目进行推荐
    public List<ProjectRecommod> queryStudentForReCommod(List<String> skills);


}
