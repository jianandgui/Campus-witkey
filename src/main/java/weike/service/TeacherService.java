package weike.service;

import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.view.ProjectRecommend;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface TeacherService {



    public int teacherAddPersonal(TeacherDetail teacherDetail);


    //老师发布项目
    public int issueProject(ProjectInfo projectInfo);

    //TODO
    //老师发送邮件给学生

    //发布项目进行推荐
    public List<ProjectRecommend> queryStudentForReCommod(List<String> skills);


}
