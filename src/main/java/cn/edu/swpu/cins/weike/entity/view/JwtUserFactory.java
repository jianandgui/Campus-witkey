package cn.edu.swpu.cins.weike.entity.view;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;

import java.util.Collections;
import java.util.Date;

/**
 * Created by muyi on 17-4-18.
 */
public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser createStudent(StudentInfo studentinfo) {
        return new JwtUser(
                studentinfo.getId(),
                studentinfo.getUsername(),
                studentinfo.getPassword(),
                studentinfo.getEmail(),
                Collections.singletonList(ToGrantedAuthorities(studentinfo.getRole())),
                new Date(studentinfo.getLastPasswordResetDate())
        );
    }

    public static JwtUser createTeacher(TeacherInfo teacherinfo) {
        return new JwtUser(
                teacherinfo.getId(),
                teacherinfo.getUsername(),
                teacherinfo.getPassword(),
                teacherinfo.getEmail(),
                Collections.singletonList(ToGrantedAuthorities(teacherinfo.getRole())),
                new Date(teacherinfo.getLastPasswordResetDate())
        );
    }

    public static JwtUser createAdmin(AdminInfo adminInfo) {
        return new JwtUser(
                adminInfo.getId(),
                adminInfo.getUsername(),
                adminInfo.getPassword(),
                adminInfo.getEmail(),
                Collections.singletonList(ToGrantedAuthorities(adminInfo.getRole())),
                new Date(adminInfo.getLastPasswordResetDate())
        );
    }

    private static GrantedAuthority ToGrantedAuthorities(String authorities) {
        return new SimpleGrantedAuthority(authorities);

    }
}
