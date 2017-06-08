package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weike.dao.ProjectDao;
import weike.dao.StudentDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.view.ProjectRecommend;
import weike.service.TeacherService;
import weike.util.ReduceRepeate;

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
    @Autowired
    private ReduceRepeate reduceRepeate;
    @Override
    public int teacherAddPersonal(TeacherDetail teacherDetail) {
        return teacherDao.teacherAddPersonal(teacherDetail);
    }

    //老师发布项目
    @Override
    public int issueProject(ProjectInfo projectInfo) {
        return projectDao.addProject(projectInfo);
    }

    @Override
    public List<ProjectRecommend> queryStudentForReCommod(List<String> skills) {
        return reduceRepeate.reduceStudentRepeate(skills);
    }



}
