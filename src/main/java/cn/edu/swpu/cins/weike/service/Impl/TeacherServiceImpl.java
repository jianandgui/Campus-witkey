package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.TeacherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.service.TeacherService;
import cn.edu.swpu.cins.weike.util.ReduceRepeate;

import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    private ProjectDao projectDao;
    private StudentDao studentDao;
    private ReduceRepeate reduceRepeate;

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao, ProjectDao projectDao, StudentDao studentDao, ReduceRepeate reduceRepeate) {
        this.teacherDao = teacherDao;
        this.projectDao = projectDao;
        this.studentDao = studentDao;
        this.reduceRepeate = reduceRepeate;
    }

    @Override
    public int teacherAddPersonal(TeacherDetail teacherDetail) throws TeacherException {
        try {
            return teacherDao.teacherAddPersonal(teacherDetail);
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    //老师发布项目
    @Override
    public int issueProject(ProjectInfo projectInfo) throws TeacherException {
        try {
            return projectDao.addProject(projectInfo);
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public List<ProjectRecommend> queryStudentForReCommod(List<String> skills) throws TeacherException {
        try {
            return reduceRepeate.reduceStudentRepeate(skills);
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public int updateInfo(TeacherDetail teacherDetail, String username) throws TeacherException{
        try{
            teacherDetail.setUsername(username);
            return teacherDao.updateInfo(teacherDetail)>0?1:0;
        }catch (Exception e){
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }

    }

    @Override
    public List<String> queryAllProject(String projectConnector) throws TeacherException{
        try{
            return teacherDao.queryAllProject(projectConnector);
        }catch (Exception e){
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }

    }
}
