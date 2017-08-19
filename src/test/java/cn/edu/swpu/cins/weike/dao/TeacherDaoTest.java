package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TeacherDaoTest {

    @Autowired
    TeacherDao teacherDao;

    @Test
    public void teacherRegister() throws Exception {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setEmail("sadasd");
        teacherInfo.setLastPasswordResetDate(123123);
        teacherInfo.setPassword("asdasd");
        teacherInfo.setRole("asda");
        teacherInfo.setUsername("teacher");
assertEquals(1,teacherDao.teacherRegister(teacherInfo));

    }

    @Test
    public void queryByName() throws Exception {

        log.info(teacherDao.queryByName("muyi").toString());
    }

    @Test
    public void queryEamil() throws Exception {
        log.info(teacherDao.queryEamil("879604213@qq.com").toString());

    }

    @Test
    public void queryForPhone() throws Exception {
        log.info(teacherDao.queryForPhone("fd").toString());
    }

    @Test
    public void teacherAddPersonal() throws Exception {
        TeacherDetail teacherDetail=new TeacherDetail();
        teacherDetail.setAcademy("asdas");
        teacherDetail.setImage("asdasd");
        teacherDetail.setQq(213131);
        teacherDetail.setRank("asdasd");
        teacherDetail.setSex("asda");
        teacherDetail.setUniversity("asdasd");
        teacherDetail.setUsername("乱七八糟");
        assertEquals(1,teacherDao.teacherAddPersonal(teacherDetail));
    }

    @Test
    public void updatePassword() throws Exception {

        assertEquals(1,teacherDao.updatePassword("teacher", "teacher"));
    }

    @Test
    public void queryAllRecommod() throws Exception {
        log.info(teacherDao.queryAllRecommod("s").toString());
    }

    @Test
    public void updateInfo() throws Exception {
        TeacherDetail teacherDetail=new TeacherDetail();
        teacherDetail.setAcademy("asdas11");
        teacherDetail.setImage("asdasd2");
        teacherDetail.setQq(21313123);
        teacherDetail.setRank("asdasd23");
        teacherDetail.setSex("asda23");
        teacherDetail.setUniversity("asdasd23");
        teacherDetail.setUsername("乱七八糟");
       assertEquals(1, teacherDao.updateInfo(teacherDetail));
    }

    @Test
    public void queryAllProject() throws Exception {
        log.info(teacherDao.queryAllProject("muyi").toString());
    }

    @Test
    public void queryForData() throws Exception {
       log.info( teacherDao.queryForData("木易").toString());
    }

}