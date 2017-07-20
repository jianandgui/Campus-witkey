package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muyi on 17-6-8.
 */

@Component
public class ReduceRepeate {


    //用来去出查询时的重复
    @Autowired
    private StudentDao studentDao;

    public List<ProjectRecommend> reduceStudentRepeate(List<String> skills) {

        List<ProjectRecommend> list;
        list = new ArrayList<ProjectRecommend>();

        skills.forEach(s -> list.addAll(studentDao.queryAllRecommod(s)));


//        for (String skill : skills) {
//            list.addAll(studentDao.queryAllRecommod(skill));
//        }




        //对List去重处理
        List<ProjectRecommend> ProjectRecommodList = new ArrayList<ProjectRecommend>();
        int num = 0;
        for (ProjectRecommend projectRecommend : list) {
            String username = projectRecommend.getUsername();
            for (ProjectRecommend projectRecommend1 : ProjectRecommodList) {
                if (username.equals(projectRecommend1.getUsername())) {
                    num++;
                }
            }
            if (num == 0) {
                ProjectRecommodList.add(projectRecommend);
            }
        }
        return ProjectRecommodList;
    }


}
