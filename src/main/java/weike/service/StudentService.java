package weike.service;

import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.StudentDetail;
import weike.entity.view.ProjectRecommend;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface StudentService {





    //学生发布项目
    // 判断学生是否填写了个人信息  否则不能发布项目
    public int issueProject(ProjectInfo projectInfo);






    //学生添加个人信息

    public int addPersonal(StudentDetail studentDetail);



    //推荐格式的学生参加项目
    public List<ProjectRecommend> queryForReCommod(List<String> skills);


}
