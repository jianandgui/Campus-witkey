package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
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
    public int teacherRegister(TeacherInfo teacherinfo);


    //用于登录(根据用户名查询老师信息)
    public TeacherInfo queryByName(String username);

    //根据邮箱查找对象 看是否已经注册
    public TeacherInfo queryEamil(String email);

    //获取老师电话
    public TeacherDetail queryForPhone(String username);

    //老师添加个人信息
    public int teacherAddPersonal(TeacherDetail teacherDetail);

    //修改密码
    public int updatePassword(@Param("username") String username, @Param("password") String password);

    //查询符合条件的学生
    public List<ProjectRecommend> queryAllRecommod(String skills);

}


