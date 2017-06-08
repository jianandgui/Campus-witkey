package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.dao.AdminDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by muyi on 17-4-18.
 */
@Service
public class JwtUserServiceDetailImpl implements UserDetailsService {


    @Autowired
    private AdminDao adminDao;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private StudentDao studentDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminInfo adminInfo = adminDao.queryByName(username);
        StudentInfo studentinfo = studentDao.selectStudent(username);
        TeacherInfo teacherinfo = teacherDao.queryByName(username);
        if (adminInfo != null && adminInfo.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            return JwtUserFactory.createAdmin(adminInfo);
        } else if (studentinfo != null && studentinfo.getRole().equalsIgnoreCase("ROLE_STUDENT")) {
            return JwtUserFactory.createStudent(studentinfo);
        } else if (teacherinfo != null && teacherinfo.getRole().equalsIgnoreCase("ROLE_TEACHER")) {
            return JwtUserFactory.createTeacher(teacherinfo);
        } else {
            return null;
        }
    }
}
