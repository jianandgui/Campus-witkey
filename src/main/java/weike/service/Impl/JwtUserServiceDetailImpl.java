package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import weike.dao.AdminDao;
import weike.dao.StudentDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.AdminInfo;
import weike.entity.persistence.StudentInfo;
import weike.entity.persistence.TeacherInfo;
import weike.entity.view.JWTuserFactory;

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
            return JWTuserFactory.createAdmin(adminInfo);
        } else if (studentinfo != null && studentinfo.getRole().equalsIgnoreCase("ROLE_STUDENT")) {
            return JWTuserFactory.createStudent(studentinfo);
        } else if (teacherinfo != null && teacherinfo.getRole().equalsIgnoreCase("ROLE_TEACHER")) {
            return JWTuserFactory.createTeacher(teacherinfo);
        } else {
            return null;
        }
    }
}
