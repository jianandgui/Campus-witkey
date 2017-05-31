package weike.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import weike.config.filter.JwtTokenUtil;
import weike.dao.ProjectDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.persistence.TeacherInfo;
import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ResultData;
import weike.service.ProjectService;
import weike.service.TeacherService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by muyi on 17-4-7.
 */
@CrossOrigin(origins = "http://182.150.37.74", maxAge = 3600)
@RestController
@RequestMapping("/WeiKe")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private ProjectDao projectDao;



    //老师发布项目
    @PostMapping("/teacher/addProject")
    public ResultData publishProject(@RequestBody ProjectInfoForSkills projectInfoForSkills, HttpServletRequest request) {

        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        TeacherInfo teacherinfo = teacherDao.queryByName(username);
        TeacherDetail teacherDetail = teacherDao.queryForPhone(username);
        if (projectDao.queryProjectDetail(projectInfoForSkills.getProjectName())==null) {

            if (teacherDetail != null) {
                projectInfoForSkills.setProjectConnector(username);
                projectInfoForSkills.setEmail(teacherinfo.getEmail());
                projectInfoForSkills.setQq(teacherDetail.getQq());
                if (teacherService.issueProject(projectInfoForSkills) != 1) {

                    return new ResultData(false, "发布项目失败");
                }
                return new ResultData(teacherService.queryStudentForReCommod(projectInfoForSkills.getProjectNeed()));
            }

            return new ResultData(false, "请填写个人信息");
        }
        return new ResultData(false,"请勿重复添加项目");
    }


    //老师向学生发邮件邀请参加项目
    //TODO
    public ResultData sendMail(){

        return null;
    }

    //老师添加个人信息
    @PostMapping("/teacher/addPersonal")
    public ResultData addTeacherPersonal(@RequestBody TeacherDetail teacherDetail,HttpServletRequest request){
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if(teacherDao.queryForPhone(username)==null){

            teacherDetail.setUsername(username);

            int num=teacherDao.teacherAddPersonal(teacherDetail);

            if (num == 1) {

                return new ResultData(true, "信息添加成功");
            }


            return new ResultData(false, "信息添加失败");
        }

            return new ResultData(false,"请勿重复添加");

        }





}
