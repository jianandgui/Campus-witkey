package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.service.ProjectService;

import java.util.List;

/**
 * Created by muyi on 17-4-8.
 */
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/weike")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //显示所有项目
    @GetMapping("/projects")
    //requestmapping requestmethod=get
    public ResultData queryAll(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        try {
            return new ResultData(projectService.showProjectAll(offset, limit));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());}
    }

    @GetMapping("/index")
    public ResultData queryForIndex(){
        try{
            List<ProjectView> list=projectService.queryForIndex();
            return new ResultData(true,list);
        }catch (Exception e)  {
            return new ResultData(false,e.getMessage());}

    }

    @GetMapping("/projectName")
    public ResultData queryForProjectByName(@RequestParam String projectName) {
        try {
            ProjectDetail projectDetail = projectService.showProject(projectName);
            return new ResultData(projectDetail);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());}
    }


    //根据关键词搜索项目
    @GetMapping("/projectsByWords")
    public ResultData queryByKeyWords(@RequestParam String keyWords) {
        try {
            List<ProjectView> projectViews = projectService.queryByKeyWords(keyWords);
            if (!projectViews.isEmpty()) {
                return new ResultData(projectViews);
            }
            return new ResultData(false, ProjectEnum.NO_PROJECTS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());}
    }
}
