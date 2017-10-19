package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.entity.view.TeacherPersonData;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    /**
     * 老师发布项目
     *
     * @param projectInfo
     * @param request
     * @return 推荐人选列表
     */
    @PostMapping("/addProject")
    public ResultData publishProject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            teacherService.issueProject(projectInfo, username);
            List<ProjectRecommend> projectRecommends = teacherService.queryStudentForReCommod(projectInfo.getProjectNeed(), username);
            return new ResultData(true, projectRecommends);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 老师添加个人详细信息
     *
     * @param teacherDetail
     * @param request
     * @return
     */
    @PostMapping("/addPersonal")
    public ResultData addTeacherPersonal(@RequestBody TeacherDetail teacherDetail, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            teacherService.teacherAddPersonal(teacherDetail, username);
            return new ResultData(true, UserEnum.ADD_PERSONAL_SUCCESS);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 老师修改自己的信息
     *
     * @param teacherDetail
     * @param request
     * @return
     */
    @PostMapping("/updateInfo")
    public ResultData updateInfo(@RequestBody TeacherDetail teacherDetail, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            teacherService.updateInfo(teacherDetail, username);
            return new ResultData(true, UserEnum.UPDATE_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 老师查看发布过的项目
     *
     * @param request
     * @return
     */
    @GetMapping("/allProject")
    public ResultData queryAllProject(HttpServletRequest request) {
        try {
            List<String> list = teacherService.queryAllProject(getUsrName.AllProjects(request));
            return new ResultData(true, list);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 老师查看个人信息
     *
     * @param request
     * @return
     */
    @GetMapping("/personalData")
    public ResultData queryForData(HttpServletRequest request) {

        try {
            TeacherPersonData teacherPersonData = teacherService.queryForData(getUsrName.AllProjects(request));
            return new ResultData(true, teacherPersonData);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 查看发布项目的老师的信息
     * @param username
     * @return
     */
    @GetMapping("/otherTeacherData")
    public ResultData queryForData(@RequestParam String username) {

        try {
            TeacherPersonData teacherPersonData = teacherService.queryForData(username);
            return new ResultData(true, teacherPersonData);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("uploadImage")
    public ResultData uploadImage(HttpServletRequest request, @RequestPart("image") MultipartFile image) throws IOException {
        try {
            String path=teacherService.updateTeacherImage(request, image);
            return new ResultData(true,path);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }
}
