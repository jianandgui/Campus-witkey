package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.ProjectEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-4-8.
 */
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/WeiKe")
public class ProjectController {
    private ProjectService projectService;


    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

//    //显示所有项目
//    @GetMapping("/projects")
//    public ResultData queryAll(@RequestParam("offset") int offset,
//                               @RequestParam("limit") int limit) {
//        try {
//            return new ResultData(projectService.showProjectAll(offset, limit));
//        } catch (Exception e) {
//            return new ResultData(false, e.getMessage());
//        }
//    }

    /**
     * 首页显示项目详情 发布人详情 项目关注人 人数和成功通过申请人数
     *
     * @return
     */
    @GetMapping("/index")
    public ResultData queryForIndex(@RequestParam(required = false, name = "offset", defaultValue = "1") int offset) throws Exception {
        try {
        List<IndexVO> indexVOList = projectService.queryForIndex(offset);
        return new ResultData(true, indexVOList);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 根据项目名字查看项目详情
     *
     * @param projectName
     * @return
     */
    @GetMapping("/projectName")
    public ResultData queryForProjectByName(@RequestParam String projectName) {
        try {
            IndexVO indexVO= projectService.showProject(projectName);
            return new ResultData(true, indexVO);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 根据关键词查询项目
     *
     * @param keyWords
     * @return
     */
    @GetMapping("/projectsByWords")
    public ResultData queryByKeyWords(@RequestParam String keyWords) {
        try {
            List<IndexVO> projectViews = projectService.queryByKeyWords(keyWords);
            if (!projectViews.isEmpty()) {
                return new ResultData(projectViews);
            }
            return new ResultData(false, ProjectEnum.NO_PROJECTS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/{projectName}")
    public ResultData deletePro(@PathVariable("projectName") String projectName, HttpServletRequest request) {
        try {
            projectService.deleteProByProName(projectName, request);
            return new ResultData(true, "删除成功");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/{projectName}")
    public ResultData updatePro(@PathVariable("projectName") String projectName, @RequestBody ProjectInfo projectInfo, HttpServletRequest request) {
        try {
            projectService.updateProByName(projectName, projectInfo, request);
            return new ResultData(true, "修改成功！");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }
}
