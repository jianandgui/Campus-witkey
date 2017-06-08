package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import weike.dao.ProjectDao;
import weike.dao.StudentDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.StudentDetail;
import weike.entity.view.ProjectRecommend;
import weike.service.StudentService;
import weike.util.ReduceRepeate;

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

    @Autowired
    private ReduceRepeate reduceRepeate;



    //学生发布项目
    @Override
    public int issueProject(ProjectInfo projectInfo) {

        return projectDao.addProject(projectInfo);

    }




    @Override
    public int addPersonal(StudentDetail studentDetail) {
        int rate=studentDetail.getSkills().toArray().length;
        if(rate==1||rate==0){
            studentDetail.setLevel("D");
        }
        if(rate==2){
            studentDetail.setLevel("C");
        }
        if(rate==3){
            studentDetail.setLevel("B");
        }
        if(rate>3){
            studentDetail.setLevel("A");
        }

        return studentDao.studentAddPersonal(studentDetail);


    }


    //重新抽离出一个方法

    @Override
    public List<ProjectRecommend> queryForReCommod(List<String> skills) {

        return reduceRepeate.reduceStudentRepeate(skills);

    }
}
