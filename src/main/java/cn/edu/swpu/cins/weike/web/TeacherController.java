package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
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

/**
 * Created by muyi on 17-4-7.
 */
@CrossOrigin(maxAge = 3600)
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
    public ResultData publishProject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request) {

        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        TeacherInfo teacherinfo = teacherDao.queryByName(username);
        TeacherDetail teacherDetail = teacherDao.queryForPhone(username);
        if (projectDao.queryProjectDetail(projectInfo.getProjectName())==null) {

            if (teacherDetail != null) {
                projectInfo.setProjectConnector(username);
                projectInfo.setEmail(teacherinfo.getEmail());
                projectInfo.setQq(teacherDetail.getQq());
                if (teacherService.issueProject(projectInfo) != 1) {

                    return new ResultData(false, "发布项目失败");
                }
                return new ResultData(teacherService.queryStudentForReCommod(projectInfo.getProjectNeed()));
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
