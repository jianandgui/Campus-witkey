package weike.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weike.dao.StudentDao;
import weike.entity.view.ProjectRecommend;

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

    public List<ProjectRecommend> reduceStudentRepeate(List<String> skills){

        List<ProjectRecommend> list;
        list =new ArrayList<ProjectRecommend>();
        for (String skill: skills){
            list.addAll(studentDao.queryAllRecommod(skill));

        }
        //对List去重处理
        List<ProjectRecommend> list1=new ArrayList<ProjectRecommend>();
        int num=0;
        for(ProjectRecommend projectRecommend :list){
            String username= projectRecommend.getUsername();
            for(ProjectRecommend projectRecommend1 :list1) {
                if(username.equals(projectRecommend1.getUsername())) {
                    num++;
                }
            }
            if(num==0) {
                list1.add(projectRecommend);
            }
        }
        return list1;
    }


}
