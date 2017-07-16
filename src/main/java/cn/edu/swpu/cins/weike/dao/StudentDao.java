package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.view.PersonData;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
@Repository
@Mapper
public interface StudentDao {

    //学生注册
    public int studntRegister(StudentInfo studentinfo);

    //学生注册后填写个人信息
    public int studentAddPersonal(StudentDetail studentDetail);

    //根据学生用户名查找学生（用于注册）
    public StudentInfo selectStudent(String username);


    //查找数据库中是否有相同邮箱（用于登录）或者邮箱的注册状态
    public StudentInfo queryEmail(String email);

    //查询用户详细信息用于获取QQ号码
    public StudentDetail queryForStudentPhone(String username);

    //学生修改密码
    public int updatePassword(@Param("username") String username, @Param("password") String password);

    //查询符合条件的学生
    public List<ProjectRecommend> queryAllRecommod(String skills);

    //学生修改个人信息
    public int updateInfo(StudentDetail studentDetail);

    //查询发布所有项目
    public List<String> queryAllProject(String projectConnector);

    //学生查询个人所有信息
    public PersonData queryPerson(String username);

}
