package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.PersonData;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.exception.StudentException;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import cn.edu.swpu.cins.weike.util.SensitiveWordsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.service.StudentService;
import cn.edu.swpu.cins.weike.util.ReduceRepeat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;
    private ProjectDao projectDao;
    @Value("${event.service.pageCount}")
    private int pageCount;
    private ReduceRepeat reduceRepeat;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private SensitiveWordsFilter sensitiveWordsFilter;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, ProjectDao projectDao, ReduceRepeat reduceRepeat) {
        this.studentDao = studentDao;
        this.projectDao = projectDao;
        this.reduceRepeat = reduceRepeat;
    }

    //学生发布项目
    @Override
    public int issueProject(ProjectInfo projectInfo, String username) throws StudentException {

        try {
            projectInfo.setProjectName(sensitiveWordsFilter.Filter(projectInfo.getProjectName()));
            StudentInfo studentinfo = studentDao.selectStudent(username);
            StudentDetail studentDetail = studentDao.queryForStudentPhone(username);
            if (projectDao.queryProjectDetail(projectInfo.getProjectName()) == null) {
                projectInfo.setProjectConnector(username);
                projectInfo.setEmail(studentinfo.getEmail());
                projectInfo.setQq(studentDetail.getQq());
                int num = projectDao.addProject(projectInfo);
                if (num != 1) {
                    throw new StudentException(ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg());
                }
                return 1;
            }
            throw new StudentException(ProjectEnum.REPEATE_PROJECT.getMsg());

        } catch (Exception e) {
            throw new StudentException("数据库学生添加异常");
        }
    }

    @Override
    public int addPersonal(StudentDetail studentDetail, String username) throws StudentException {
        int rate = studentDetail.getSkills().toArray().length;
        if (rate == 2 || rate == 0 ||rate ==1) {
            studentDetail.setLevel("D");
        }
        if (rate == 3) {
            studentDetail.setLevel("C");
        }
        if (rate == 4) {
            studentDetail.setLevel("B");
        }
        if (rate > 5) {
            studentDetail.setLevel("A");
        }
        try {
            if (studentDao.queryForStudentPhone(username) == null) {
                studentDetail.setUsername(username);
                int num = studentDao.studentAddPersonal(studentDetail);
                if (num == 1) {
                    return 1;
                }
                throw new StudentException(UserEnum.ADD_PERSONAL_FAILD.getMsg());
            } else
                throw new StudentException(UserEnum.REPEATE_ADD.getMsg());
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    //重新抽离出一个方法
    @Override
    public List<ProjectRecommend> queryForReCommod(List<String> skills, String username) throws StudentException {
        try {
            List<ProjectRecommend> projectRecommends = reduceRepeat.reduceStudentRepeat(skills, username);
            if (projectRecommends.isEmpty()) {
                throw new StudentException(ProjectEnum.NO_PROJECTS.getMsg());
            }
            return projectRecommends;
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public int updateInfo(StudentDetail studentDetail, String username) throws StudentException {
        studentDetail.setUsername(username);
        try {
            int num = studentDao.updateInfo(studentDetail);
            if (num != 1) {
                throw new StudentException(UserEnum.UPDATE_FAILD.getMsg());
            }
            return num;
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public List<String> queryAllProject(String projectConnector) throws StudentException {
        try {
            List<String> list = studentDao.queryAllProject(projectConnector);
            if (list.isEmpty()) {
                throw new StudentException(UserEnum.NO_PROJECTS.getMsg());
            }
            return list;
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public PersonData queryForData(String username) throws StudentException {
        try {
            PersonData personData = studentDao.queryPerson(username);
            personData.setEmail(studentDao.selectStudent(username).getEmail());
            personData.setRole("Student");
            return personData;
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public List<String> queryProFollower(String projectName) throws StudentException {
        String projectFollowerKey = RedisKey.getBizProFollower(projectName);
        try {
            return jedisAdapter.smenber(projectFollowerKey).stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }
}
