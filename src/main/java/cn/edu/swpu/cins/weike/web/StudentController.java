package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.StudentService;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by muyi on 17-4-7.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/WeiKe")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private ProjectService projectService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ProjectDao projectDao;


    @Autowired
    private MailService mailService;

    //学生发布项目(增加推荐人选功能)
    @PostMapping("/student/addProject")
    public ResultData pulishproject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request){


        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        StudentInfo studentinfo = studentDao.selectStudent(username);
        StudentDetail studentDetail = studentDao.queryForStudentPhone(username);

        if(studentDetail!=null) {
            if(projectDao.queryProjectDetail(projectInfo.getProjectName())==null) {
                projectInfo.setProjectConnector(username);
                projectInfo.setEmail(studentinfo.getEmail());
                projectInfo.setQq(studentDetail.getQq());
                int num = studentService.issueProject(projectInfo);
                if (num != 1) {
                    return new ResultData(false, "发布项目失败");
                }
                return new ResultData(studentService.queryForReCommod(projectInfo.getProjectNeed()));
            }
            return new ResultData(false,"请不要重复发布项目");
        }
        else {
            return new ResultData(false,"个人信息未填完整");
        }
    }

    //学生向项目申请人发邮件申请参加项目
    //TODO
    @GetMapping("/student/sendApply")
    public ResultData sendMail(@RequestParam String projectName,HttpServletRequest request){
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        StudentDetail studentDetail=studentDao.queryForStudentPhone(username);
        ProjectDetail projectInfo =projectDao.queryProjectDetail(projectName);
        String email= projectInfo.getEmail();
        mailService.sendMail(email,"申请项目",username);
        return new ResultData(true,"邮件发送成功");
    }


    @PostMapping("student/addPersonalDeail")
    public ResultData addPersonalDetail(@RequestBody StudentDetail studentDetail, HttpServletRequest request){
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if(studentDao.queryForStudentPhone(username)==null) {
            studentDetail.setUsername(username);
            int num=studentService.addPersonal(studentDetail);
            if (num ==1) {
                return new ResultData(true, "信息添加成功");
            }
            return new ResultData(false, "信息添加失败");
        }
        else
            return new ResultData(false,"请勿重复添加");
    }


}
