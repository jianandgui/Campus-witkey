package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by muyi on 17-7-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDaoTest {

    @Autowired
    private AdminDao adminDao;
    @Test
    public void queryByName() throws Exception {

        System.out.println(adminDao.queryByName("test"));
    }

    @Test
    public void addAdmin() throws Exception {
        AdminInfo adminInfo=new AdminInfo();
        adminInfo.setEmail("test");
        adminInfo.setLastPasswordResetDate(121312312);
        adminInfo.setPassword("test");
        adminInfo.setRole("ROLE_ADMIN");
        adminInfo.setUsername("test");
        System.out.println(adminDao.addAdmin(adminInfo));
    }

}