package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.entity.view.TeacherPersonData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
@Repository
@Mapper
public interface TeacherDao {


    //增加一个老师信息（注册）
    int teacherRegister(TeacherInfo teacherinfo);


    //用于登录(根据用户名查询老师信息)
    TeacherInfo queryByName(String username);

    //根据邮箱查找对象 看是否已经注册
    TeacherInfo queryEamil(String email);

    //获取老师电话
    TeacherDetail queryForPhone(String username);

    //老师添加个人信息
    int teacherAddPersonal(TeacherDetail teacherDetail);

    //修改密码
    int updatePassword(@Param("username") String username, @Param("password") String password);

    //查询符合条件的学生
    List<ProjectRecommend> queryAllRecommod(String skills);

    //修改个人信息
    int updateInfo(TeacherDetail teacherDetail);

    //查询发布所有项目
    List<String> queryAllProject(String projectConnector);

    TeacherPersonData queryForData(String username);

    int updateImage(@Param("username") String username, @Param("imagePath") String imagePath);

}


