package weike.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import weike.entity.persistence.ProjectInfo;
import weike.entity.view.ProjectView;

import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
@Repository
@Mapper
public interface ProjectDao {

    //增加一个新项目
    public int addProject(ProjectInfo projectInfo);

    //查询所有项目(只查三个字段 projectName projectProfile projectApply)
    public List<ProjectView> queryAll(@Param("offset") int offset, @Param("limit") int limit);


    //根据名字显示一个项目的详细情况
    public ProjectInfo queryProjectDetail(String name);

    //根据项目名字删除一个项目（管理员权限或本人权限）
    public int deleteByName(int projectName);

    //根据关键词检索项目
    public List<ProjectView> queryByKeywords(@Param("offset") int offset, @Param("limit") int limit,@Param("keywords") String keywords);

}
