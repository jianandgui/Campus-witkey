package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.entity.view.TeacherPersonData;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.exception.TeacherException;
import cn.edu.swpu.cins.weike.util.CheckProjectInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.service.TeacherService;
import cn.edu.swpu.cins.weike.util.ReduceRepeat;

import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    private ProjectDao projectDao;
    private StudentDao studentDao;
    private ReduceRepeat reduceRepeat;
    private CheckProjectInfo checkProjectInfo;



    @Override
    public int teacherAddPersonal(TeacherDetail teacherDetail, String username) throws TeacherException {
        try {
            if (teacherDao.queryForPhone(username) != null) {
                throw new TeacherException(UserEnum.REPEATE_ADD.getMsg());
            }
            teacherDetail.setUsername(username);
            int num = teacherDao.teacherAddPersonal(teacherDetail);
            if (num != 1) {
                throw new TeacherException(UserEnum.ADD_PERSONAL_FAILD.getMsg());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    //老师发布项目
    @Override
    public int issueProject(ProjectInfo projectInfo, String username) throws TeacherException {
        try {
            checkProjectInfo.checkProjectInfo(projectInfo);
            TeacherInfo teacherinfo = teacherDao.queryByName(username);
            TeacherDetail teacherDetail = teacherDao.queryForPhone(username);
            if (projectDao.queryProjectDetail(projectInfo.getProjectName()) != null) {
                throw new TeacherException(ProjectEnum.REPEATE_PROJECT.getMsg());
            }
            projectInfo.setProjectConnector(username);
            projectInfo.setEmail(teacherinfo.getEmail());
            projectInfo.setQq(teacherDetail.getQq());
            if (projectDao.addProject(projectInfo) != 1) {
                throw new TeacherException(ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ProjectRecommend> queryStudentForReCommod(List<String> skills, String username) throws TeacherException {
        try {
            return reduceRepeat.reduceStudentRepeat(skills, username);
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public int updateInfo(TeacherDetail teacherDetail, String username) throws TeacherException {
        try {
            teacherDetail.setUsername(username);
            int num = teacherDao.updateInfo(teacherDetail);
            if (num != 1) {
                throw new TeacherException(UserEnum.UPDATE_FAILD.getMsg());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<String> queryAllProject(String projectConnector) throws TeacherException {
        try {
            List<String> list = teacherDao.queryAllProject(projectConnector);
            if (list.isEmpty()) {
                throw new TeacherException(UserEnum.NO_PROJECTS.getMsg());
            }
            return list;
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    public TeacherPersonData queryForData(String username) throws TeacherException {
        try {
            TeacherPersonData teacherPersonData = teacherDao.queryForData(username);
            TeacherInfo teacherInfo = teacherDao.queryByName(username);
            if (teacherInfo != null) {
                teacherPersonData.setEmail(teacherInfo.getEmail());
                teacherPersonData.setRole("ROLE_TEACHER");
            }
            return teacherPersonData;
        } catch (Exception e) {
            throw new TeacherException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }
}
