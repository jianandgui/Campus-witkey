package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-6-8.
 */

@Component
public class ReduceRepeate {


    //用来去出查询时的重复
    @Autowired
    private StudentDao studentDao;

    public List<ProjectRecommend> reduceStudentRepeat(List<String> skills,String username) {

        List<ProjectRecommend> list;
        list = new ArrayList<ProjectRecommend>();
        skills.forEach(s -> list.addAll(studentDao.queryAllRecommod(s)));
        //对List去重处理
        List<ProjectRecommend> projectRecommodList = new ArrayList<ProjectRecommend>();
        //重写了hashCode和equal方法 尝试使用hash插入去重
        HashSet<ProjectRecommend> projectRecommends=new HashSet<>();
        list.forEach(projectRecommend -> {
            projectRecommends.add(projectRecommend);
        });

        projectRecommends.forEach(projectRecommend -> {
            projectRecommodList.add(projectRecommend);
        });

        //去除自己



        return  projectRecommodList.stream().filter(projectRecommend -> !projectRecommend.getUsername().equals(username)).collect(Collectors.toList());
    }


}
