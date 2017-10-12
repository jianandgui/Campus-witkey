package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.view.PersonData;
import cn.edu.swpu.cins.weike.entity.view.ProjectRecommend;
import cn.edu.swpu.cins.weike.exception.StudentException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public interface StudentService {


    int issueProject(ProjectInfo projectInfo,String username) throws StudentException;

    int addPersonal(StudentDetail studentDetail,String username) throws StudentException;

    List<ProjectRecommend> queryForReCommod(List<String> skills,String username) throws StudentException;

    int updateInfo(StudentDetail studentDetail, String username) throws StudentException;

    List<String> queryAllProject(String projectConnector) throws StudentException;

    PersonData queryForData(String username) throws StudentException;

    List<String> queryProFollower(String projectName) throws StudentException;

    List<ProjectInfo> queryForRecommend(HttpServletRequest request);


}
