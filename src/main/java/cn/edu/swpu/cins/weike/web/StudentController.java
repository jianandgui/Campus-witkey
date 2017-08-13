package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.entity.view.PersonData;
import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
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
import java.util.stream.Collectors;


/**
 * Created by muyi on 17-4-7.
 */
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/WeiKe/student")
public class StudentController {

    @Value("${jwt.header}")
    String tokenHeader;
    @Value("${jwt.tokenHead}")
    String tokenHead;

    StudentService studentService;
    StudentDao studentDao;
    ProjectService projectService;
    JwtTokenUtil jwtTokenUtil;
    ProjectDao projectDao;
    GetUsrName getUsrName;
    MailService mailService;



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

    /**
     * 学生发布项目
     * @param projectInfo
     * @param request
     * @return 推荐人选列表
     */
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
                        return new ResultData(false, ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg()); }
                    if(studentService.queryForReCommod(projectInfo.getProjectNeed(),username).isEmpty()){
                        return new ResultData(true, ProjectEnum.NO_SUITBLE_PERSON.getMsg()); }
                    return new ResultData(true,studentService.queryForReCommod(projectInfo.getProjectNeed(),username)); }
                return new ResultData(false, ProjectEnum.REPEATE_PROJECT.getMsg()); } else {
                return new ResultData(false, UserEnum.ADD_PERSONNAL.getMsg());
            } } catch (Exception e) {
            return new ResultData(false, e.getMessage()); }
    }


    /**
     * 学生添加个人信息
     * @param studentDetail
     * @param request
     * @return
     */
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

    /**
     * 学生修改自己信息
     * @param studentDetail
     * @param request
     * @return
     */
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

    /**
     * 学生查看发布过的项目
     * @param request
     * @return
     */
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

    /**
     * 学生查看个人信息
     * @param request
     * @return
     */
    @GetMapping("/personalData")
    public ResultData queryForData(HttpServletRequest request){

        try{
            PersonData personData=studentService.queryForData(getUsrName.AllProjects(request));
            return new ResultData(true,personData);
        }catch (Exception e){
            return new ResultData(false,e.getMessage());
        }
    }

    /**
     * 学生查看项目申请详细信息
     * @param projectName
     * @return
     */
    @GetMapping("/projetName")
    public ResultData queryApplyInfo(@RequestParam String projectName){
        return new ResultData(true,projectService.queryProApplyInfoByName(projectName));
    }
}
