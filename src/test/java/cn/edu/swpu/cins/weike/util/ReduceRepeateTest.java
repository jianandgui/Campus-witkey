package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by muyi on 17-7-20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReduceRepeateTest {

    @Autowired
    private StudentDao studentDao;


    @Resource
    private ReduceRepeate reduceRepeate;

    @Test
    public void reduceStudentRepeate() throws Exception {

        List<String> list=new ArrayList<>();
        list.add("springmvc");
        list.add("\"Java\"");
        list.add("struct2");
        System.out.println(reduceRepeate.reduceStudentRepeate(list));
    }

}