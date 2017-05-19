package weike.service;

import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ProjectRecommod;
import weike.entity.view.StudentDetailForLevel;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface StudentService {





    //学生发布项目
    // 判断学生是否填写了个人信息  否则不能发布项目
    public int issueProject(ProjectInfoForSkills projectInfoForSkills);






    //学生添加个人信息

    public int addPersonal(StudentDetailForLevel studentDetailForLevel);



    //推荐格式的学生参加项目
    public List<ProjectRecommod> queryForReCommod(List<String> skills);


}
