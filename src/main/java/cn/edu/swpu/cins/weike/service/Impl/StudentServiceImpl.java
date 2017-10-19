package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.PersonData;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.exception.StudentException;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.util.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.service.StudentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
    private GetUsrName getUsrName;

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private CheckProjectInfo checkProjectInfo;

    @Autowired
    private CheckStudentDate checkStudentDate;

    @Autowired
    private UploadImage uploadImage;

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
            checkProjectInfo.checkProjectInfo(projectInfo);
            projectInfo.setProjectName(sensitiveWordsFilter.Filter(projectInfo.getProjectName()));
            StudentInfo studentinfo = studentDao.selectStudent(username);
            StudentDetail studentDetail = studentDao.queryForStudentPhone(username);
            if (projectDao.queryProjectDetail(projectInfo.getProjectName()) != null) {
                throw new StudentException(ProjectEnum.REPEATE_PROJECT.getMsg());
            }
            projectInfo.setProjectConnector(username);
            projectInfo.setEmail(studentinfo.getEmail());
            projectInfo.setQq(studentDetail.getQq());
            int num = projectDao.addProject(projectInfo);
            if (num != 1) {
                throw new StudentException(ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg());
            }
            return 1;

        } catch (Exception e) {
            throw new StudentException(e.getMessage());
        }
    }

    public void getStudentSkillRate(StudentDetail studentDetail) {
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
    }

    @Override
    public int addPersonal(StudentDetail studentDetail, String username) throws StudentException {
             //增加头像上传功能！
             getStudentSkillRate(studentDetail);
        try {
            checkStudentDate.checkStudentInfo(studentDetail);
            if (studentDao.queryForStudentPhone(username) != null) {
                throw new StudentException(UserEnum.REPEATE_ADD.getMsg());
            }
            studentDetail.setUsername(username);
            int num = studentDao.studentAddPersonal(studentDetail);
            if (num != 1) {
                throw new StudentException(UserEnum.ADD_PERSONAL_FAILD.getMsg());
            }
            return 1;
        } catch (StudentException e) {
            throw  e;
        }
    }



    //重新抽离出一个方法
    @Override
    public List<ProjectRecommend> queryForReCommend(List<String> skills, String username) throws StudentException {
        try {
            List<ProjectRecommend> projectRecommends = reduceRepeat.reduceStudentRepeat(skills, username);
            if (projectRecommends.isEmpty()) {
                throw new StudentException(ProjectEnum.NO_PROJECTS.getMsg());
            }
            return projectRecommends;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int updateInfo(StudentDetail studentDetail, String username) throws StudentException {
        studentDetail.setUsername(username);
        try {
            checkStudentDate.checkStudentInfo(studentDetail);
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
        StudentInfo studentInfo=studentDao.selectStudent(username);
        if (studentInfo != null) {
            personData.setEmail(studentInfo.getEmail());
            personData.setRole("ROLE_STUDENT".toString());
        }
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

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public List<IndexVO> queryForRecommend(HttpServletRequest request) {
        String username = getUsrName.AllProjects(request);
        List<String> skills = studentDao.queryForStudentPhone(username).getSkills();
        if (skills == null) {
            throw new StudentException(ExceptionEnum.INNER_ERROR.getMsg());
        }
        List<ProjectInfo> projectInfoList = projectDao.selectRecommend(skills);
        List<ProjectDetail> projectDetailList = projectInfoList.stream().map(ProjectDetail::new).collect(Collectors.toList());
        List<IndexVO> indexVOList = projectService.getProjectDetail(projectDetailList);
        if (indexVOList.isEmpty()) {
            throw new StudentException(ExceptionEnum.NO_SUITBLE_PRO.getMsg());
        }
        return indexVOList;
    }

    @Override
    public String updateStudentImage(HttpServletRequest request, MultipartFile image) throws IOException {
        try {
            String username = getUsrName.AllProjects(request);
            String path=uploadImage.uploadImage(image, username);
            studentDao.updateImage(username, path);
            return path;
        } catch (Exception e) {
            throw e;
        }
    }
}
