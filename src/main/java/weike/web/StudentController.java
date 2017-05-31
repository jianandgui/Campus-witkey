package weike.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import weike.config.filter.JwtTokenUtil;
import weike.dao.ProjectDao;
import weike.dao.StudentDao;
import weike.entity.persistence.ProjectInfo;
import weike.entity.persistence.StudentDetail;
import weike.entity.persistence.StudentInfo;
import weike.entity.view.ProjectDetail;
import weike.entity.view.ProjectInfoForSkills;
import weike.entity.view.ResultData;
import weike.entity.view.StudentDetailForLevel;
import weike.service.MailService;
import weike.service.ProjectService;
import weike.service.StudentService;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by muyi on 17-4-7.
 */
@CrossOrigin(origins = "http://182.150.37.74", maxAge = 3600)
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
    public ResultData pulishproject(@RequestBody ProjectInfoForSkills projectInfoForSkills, HttpServletRequest request){


        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        StudentInfo studentinfo = studentDao.selectStudent(username);
        StudentDetail studentDetail = studentDao.queryForStudentPhone(username);

        if(studentDetail!=null) {

            if(projectDao.queryProjectDetail(projectInfoForSkills.getProjectName())==null) {
                projectInfoForSkills.setProjectConnector(username);
                projectInfoForSkills.setEmail(studentinfo.getEmail());
                projectInfoForSkills.setQq(studentDetail.getQq());

                int num = studentService.issueProject(projectInfoForSkills);

                if (num != 1) {

                    return new ResultData(false, "发布项目失败");
                }
                return new ResultData(studentService.queryForReCommod(projectInfoForSkills.getProjectNeed()));
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
    public ResultData addPersonalDetail(@RequestBody StudentDetailForLevel studentDetailForLevel, HttpServletRequest request){
        String authHeader = request.getHeader(this.tokenHeader);
        final String authToken = authHeader.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if(studentDao.queryForStudentPhone(username)==null) {

            studentDetailForLevel.setUsername(username);

            int num=studentService.addPersonal(studentDetailForLevel);

            if (num ==1) {

                return new ResultData(true, "信息添加成功");
            }


            return new ResultData(false, "信息添加失败");
        }

        else
            return new ResultData(false,"请勿重复添加");
    }


}
