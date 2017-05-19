package weike.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import weike.entity.view.ResultData;
import weike.service.ProjectService;

/**
 * Created by muyi on 17-4-8.
 */
@RestController
@RequestMapping("/weike")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    //显示所有项目
    @GetMapping("/projects")
    public ResultData queryAll(@RequestParam("offset") int offset, @RequestParam("limit") int limit){

        return new ResultData(projectService.showProjectAll(offset,limit));

    }






}
