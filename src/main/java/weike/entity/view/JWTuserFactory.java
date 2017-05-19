package weike.entity.view;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import weike.entity.persistence.AdminInfo;
import weike.entity.persistence.StudentInfo;
import weike.entity.persistence.TeacherInfo;

import java.util.Collections;
import java.util.Date;

/**
 * Created by muyi on 17-4-18.
 */
public final class JWTuserFactory {

    private JWTuserFactory() {
    }

    public static JWTuser createStudent(StudentInfo studentinfo) {
        return new JWTuser(
                studentinfo.getId(),
                studentinfo.getUsername(),
                studentinfo.getPassword(),
                studentinfo.getEmail(),
                Collections.singletonList(ToGrantedAuthorities(studentinfo.getRole())),
                new Date(studentinfo.getLastPasswordResetDate())
        );
    }

    public static JWTuser createTeacher(TeacherInfo teacherinfo) {
        return new JWTuser(
                teacherinfo.getId(),
                teacherinfo.getUsername(),
                teacherinfo.getPassword(),
                teacherinfo.getEmail(),
                Collections.singletonList(ToGrantedAuthorities(teacherinfo.getRole())),
                new Date(teacherinfo.getLastPasswordResetDate())
        );
    }

    public static JWTuser createAdmin(AdminInfo adminInfo) {
        return new JWTuser(
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
