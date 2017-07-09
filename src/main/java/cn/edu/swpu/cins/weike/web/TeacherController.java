package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import cn.edu.swpu.cins.weike.enums.UserEnum;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-4-7.
 */
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/WeiKe/teacher")
public class TeacherController {

    private TeacherService teacherService;
    private ProjectService projectService;
    private TeacherDao teacherDao;
    private JwtTokenUtil jwtTokenUtil;
    private ProjectDao projectDao;
    private GetUsrName getUsrName;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public TeacherController(TeacherService teacherService, ProjectService projectService, TeacherDao teacherDao, JwtTokenUtil jwtTokenUtil, ProjectDao projectDao, GetUsrName getUsrName) {
        this.teacherService = teacherService;
        this.projectService = projectService;
        this.teacherDao = teacherDao;
        this.jwtTokenUtil = jwtTokenUtil;
        this.projectDao = projectDao;
        this.getUsrName = getUsrName;
    }

    //老师发布项目
    @PostMapping("/addProject")
    public ResultData publishProject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            TeacherInfo teacherinfo = teacherDao.queryByName(username);
            TeacherDetail teacherDetail = teacherDao.queryForPhone(username);
            if (projectDao.queryProjectDetail(projectInfo.getProjectName()) == null) {
                if (teacherDetail != null) {
                    projectInfo.setProjectConnector(username);
                    projectInfo.setEmail(teacherinfo.getEmail());
                    projectInfo.setQq(teacherDetail.getQq());
                    if (teacherService.issueProject(projectInfo) != 1) {
                        return new ResultData(false, ProjectEnum.PUBLISH_PROJECT_FAILD.getMsg());}
                        if(teacherService.queryStudentForReCommod(projectInfo.getProjectNeed()).isEmpty()){
                        return new ResultData(true,ProjectEnum.NO_SUITBLE_PERSON.getMsg());
                        }
                    return new ResultData(true,teacherService.queryStudentForReCommod(projectInfo.getProjectNeed()));}
                return new ResultData(false, ProjectEnum.ADD_PERSONNAL.getMsg());}
            return new ResultData(false, ProjectEnum.REPEATE_PROJECT.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }



    //老师添加个人信息
    @PostMapping("/addPersonal")
    public ResultData addTeacherPersonal(@RequestBody TeacherDetail teacherDetail, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            if (teacherDao.queryForPhone(username) == null) {
                teacherDetail.setUsername(username);
                int num = teacherDao.teacherAddPersonal(teacherDetail);
                if (num == 1) {
                    return new ResultData(true, UserEnum.ADD_PERSONAL_SUCCESS.getMsg());}
                return new ResultData(false, UserEnum.ADD_PERSONAL_FAILD.getMsg());}
            return new ResultData(false, UserEnum.REPEATE_ADD.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    //老师修改个人信息
    @PostMapping("/updateInfo")
    public ResultData updateInfo(@RequestBody TeacherDetail teacherDetail, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            int num = teacherService.updateInfo(teacherDetail, username);
            if (num == 1) {
                return new ResultData(true, UserEnum.UPDATE_SUCCESS.getMsg());}
            return new ResultData(false, UserEnum.UPDATE_FAILD.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    //查看发布过的项目
    @GetMapping("/allProject")
    public ResultData queryAllProject(HttpServletRequest request) {
        try {
            List<String> list = teacherService.queryAllProject(getUsrName.AllProjects(request));
            if (!list.isEmpty()) {
                return new ResultData(true, list);}
            return new ResultData(false, UserEnum.NO_PROJECTS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }
}
