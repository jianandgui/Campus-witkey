package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class StudentDaoTest {

    @Autowired
    StudentDao studentDao;


    @Test
    public void studentRegister() throws Exception {

        StudentInfo studentInfo =new StudentInfo();
        studentInfo.setEmail("wewe");
        studentInfo.setLastPasswordResetDate(12312312);
        studentInfo.setPassword("asdasd");
        studentInfo.setRole("asdasd");
        studentInfo.setUsername("test");
        assertEquals(1,studentDao.studntRegister(studentInfo));
    }

    @Test
    public void studentAddPersonal() throws Exception {

        List<String> skills =new ArrayList<>();
        skills.add("test");
        skills.add("test2");
        StudentDetail studentDetail=new StudentDetail();
        studentDetail.setEduBackgroud("sasd");
        studentDetail.setEntryUniversity(123123);
        studentDetail.setExperience("asdas");
        studentDetail.setImage("asdada");
        studentDetail.setLeaveUniversity(123123);
        studentDetail.setLevel("a");
        studentDetail.setMajorAndGrade("asdasd");
        studentDetail.setQq(123123);
        studentDetail.setSelfFeel("asdasdad");
        studentDetail.setSex("asdasdasd");
        studentDetail.setSkills(skills);
        studentDetail.setUsername("asdasd");
        studentDetail.setUniversity("saasdasdasdada");

        studentDao.studentAddPersonal(studentDetail);
    }

    @Test
    public void selectStudent() throws Exception {

        log.info(studentDao.selectStudent("test").toString());
    }

    @Test
    public void queryEmail() throws Exception {
        log.info(studentDao.queryEmail("879604213@qq.com").toString());
    }

    @Test
    public void queryForStudentPhone() throws Exception {
    }

    @Test
    public void updatePassword() throws Exception {
    }

    @Test
    public void queryAllRecommod() throws Exception {
    }

    @Test
    public void updateInfo() throws Exception {
    }

    @Test
    public void queryAllProject() throws Exception {
    }

    @Test
    public void queryPerson() throws Exception {
    }

}