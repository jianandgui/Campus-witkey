package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.entity.view.TeacherPersonData;
import cn.edu.swpu.cins.weike.exception.TeacherException;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface TeacherService {


    public int teacherAddPersonal(TeacherDetail teacherDetail) throws TeacherException;


    //老师发布项目
    public int issueProject(ProjectInfo projectInfo) throws TeacherException;

    //TODO
    //老师发送邮件给学生

    //发布项目进行推荐
    public List<ProjectRecommend> queryStudentForReCommod(List<String> skills) throws TeacherException;

    public int updateInfo(TeacherDetail teacherDetail, String username) throws TeacherException;

    //查询发布过的所有项目
    public List<String> queryAllProject(String projectConnector) throws TeacherException;

    public TeacherPersonData queryForData(String username) throws TeacherException;


}
