package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.StudentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by muyi on 17-4-7.
 */
@RestController
@RequestMapping("/WeiKe/student")
public class StudentController {
    private StudentService studentService;
    private StudentDao studentDao;
    private ProjectService projectService;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private JwtTokenUtil jwtTokenUtil;
    private ProjectDao projectDao;
    private GetUsrName getUsrName;
    private MailService mailService;

    @Autowired
    public StudentController(StudentService studentService, StudentDao studentDao, ProjectService projectService, JwtTokenUtil jwtTokenUtil, ProjectDao projectDao, GetUsrName getUsrName, MailService mailService) {
        this.studentService = studentService;
        this.studentDao = studentDao;
        this.projectService = projectService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.projectDao = projectDao;
        this.getUsrName = getUsrName;
        this.mailService = mailService;
    }

    //学生发布项目(增加推荐人选功能)
    @PostMapping("/addProject")
    public ResultData publishProject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request) {

        try {
            String username = getUsrName.AllProjects(request);
            StudentInfo studentinfo = studentDao.selectStudent(username);
            StudentDetail studentDetail = studentDao.queryForStudentPhone(username);
            if (studentDetail != null) {
                if (projectDao.queryProjectDetail(projectInfo.getProjectName()) == null) {
                    projectInfo.setProjectConnector(username);
                    projectInfo.setEmail(studentinfo.getEmail());
                    projectInfo.setQq(studentDetail.getQq());
                    int num = studentService.issueProject(projectInfo);
                    if (num != 1) {
                        return new ResultData(false, ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg());
                    }
                    if(studentService.queryForReCommod(projectInfo.getProjectNeed()).isEmpty()){
                        return new ResultData(true, ProjectEnum.NO_SUITBLE_PERSON.getMsg());
                    }
                    return new ResultData(true,studentService.queryForReCommod(projectInfo.getProjectNeed()));
                }
                return new ResultData(false, ProjectEnum.REPEATE_PROJECT.getMsg());
            } else {
                return new ResultData(false, UserEnum.ADD_PERSONNAL.getMsg());
            }
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }




    @PostMapping("/addPersonalDeail")
    public ResultData addPersonalDetail(@RequestBody StudentDetail studentDetail, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            if (studentDao.queryForStudentPhone(username) == null) {
                studentDetail.setUsername(username);
                int num = studentService.addPersonal(studentDetail);
                if (num == 1) {
                    return new ResultData(true, UserEnum.ADD_PERSONAL_SUCCESS.getMsg());}
                return new ResultData(false, UserEnum.ADD_PERSONAL_FAILD.getMsg());
            } else
                return new ResultData(false, UserEnum.REPEATE_ADD.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/updateInfo")
    public ResultData updateInfo(@RequestBody StudentDetail studentDetail, HttpServletRequest request) {

        try {
            String username = getUsrName.AllProjects(request);
            int num = studentService.updateInfo(studentDetail, username);
            if (num == 1) {
                return new ResultData(true, UserEnum.UPDATE_SUCCESS.getMsg());
            } else
                return new ResultData(false, UserEnum.UPDATE_FAILD.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    //查看发布过的项目
    @GetMapping("/allProject")
    public ResultData queryAllProject(HttpServletRequest request) {
        try {
            List<String> list = studentService.queryAllProject(getUsrName.AllProjects(request));
            if (list.isEmpty()) {
                return new ResultData(false, UserEnum.NO_PROJECTS.getMsg());
            }
            return new ResultData(true, list);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

}
