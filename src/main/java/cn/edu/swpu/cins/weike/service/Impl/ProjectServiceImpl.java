package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.StudentService;
import cn.edu.swpu.cins.weike.service.TeacherService;
import cn.edu.swpu.cins.weike.util.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-4-7.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectDao projectDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private JedisAdapter jedisAdapter;
    @Autowired
    private GetUsrName getUsrName;
    @Autowired
    private RedisToList redisToList;
    @Autowired
    private CheckProjectInfo checkProjectInfo;
    @Autowired
    public ProjectServiceImpl(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }
    @Value("${event.service.pageCount}")
    private int pageCount;




    @Override
    public List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException {
        try {
            return projectDao.queryAll(offset * pageCount, pageCount);
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public IndexVO showProject(String projectName) throws ProjectException {
        String proClickNum = RedisKey.getBizProClickNum(projectName);
        try {
            long hitsNum = jedisAdapter.incr(proClickNum);
            //1、
            jedisAdapter.add(proClickNum, hitsNum);
            IndexVO indexVO = new IndexVO();
            ProjectDetail projectDetail = projectDao.queryProjectDetail(projectName);
            projectDetail.setProHits(hitsNum);
            indexVO.setProjectDetails(projectDetail);
            String publisher = projectDetail.getProjectConnector();
            getPersonData(indexVO, publisher,projectName);
            return indexVO;
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    public void getPersonData(IndexVO indexVO,String username,String projectName) {

        String proFollowerKeys = RedisKey.getBizProFollower(projectName);
        String proApplySuccess = RedisKey.getBizProApplicant(projectName);
        String proApplyingPerson = RedisKey.getBizProApplying(projectName);
        try {
            List<String> proFollowers=redisToList.redisToList(proFollowerKeys); //项目关注人
            indexVO.setFollowPros(proFollowers);
            indexVO.setFollowNum(proFollowers.size());//项目关注人数
            List<String> applyPersons=redisToList.redisToList(proApplySuccess);//项目申请成功的人
            indexVO.setApplySuccessPerson(applyPersons);
            indexVO.setApplySuccessNum(applyPersons.size());//项目成功申请的人数
            List<String> applyingPerson = redisToList.redisToList(proApplyingPerson);
            indexVO.setProApplyingPerson(applyingPerson);
            indexVO.setApplyingNum(applyingPerson.size());
            //发布人详情
            if (studentService.queryForData(username) == null) {
                indexVO.setPersonData(teacherService.queryForData(username));
            } else {
                indexVO.setPersonData(studentService.queryForData(username));
            }
        } catch (Exception e) {
               throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }


    @Override
    public List<IndexVO> queryByKeyWords(String keyWords) throws ProjectException {
        try {
            List<ProjectDetail> projectDetailList = projectDao.queryByKeywords(keyWords);
            List<IndexVO> indexVOList = getProjectDetail(projectDetailList);
            return indexVOList;
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    /**
     * 首页显示项目详情 发布人详情 项目关注人 人数和成功通过申请人数
     * @return
     * @throws ProjectException
     */
    @Override
    public List<IndexVO> queryForIndex(int offset,HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            List<ProjectDetail> projectDetails=projectDao
                    .queryForIndex((--offset) * pageCount, pageCount)
                    .stream().filter(projectDetail -> {
                        if (username.equals(projectDetail.getProjectConnector())) {
                            return false;
                        }else {
                            return true;
                        }
                    }).collect(Collectors.toList());
            List<IndexVO> indexVOList = getProjectDetail(projectDetails);
            return indexVOList;
        }
        catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    public List<IndexVO> getProjectDetail(List<ProjectDetail> projectDetails) {
        List<IndexVO> indexVOList=projectDetails
                .stream()
                .map(IndexVO::new)
                .collect(Collectors.toList())
                .stream().map(indexVO -> {
                    String proName=indexVO.getProjectDetails().getProjectName();//项目详细情况
                    String proClickNum = RedisKey.getBizProClickNum(proName);
                    long hitsNum;
                    if (jedisAdapter.get(proClickNum) != null) {
                         hitsNum= Long.parseLong(jedisAdapter.get(proClickNum));
                         indexVO.getProjectDetails().setProHits(hitsNum);
                    }
                    String projectConnector = indexVO.getProjectDetails().getProjectConnector();
                    getPersonData(indexVO, projectConnector,proName);//获取每个项目的详细情况
                    return indexVO;
                }).collect(Collectors.toList());
        return indexVOList;
    }

    @Override
    public ProApplyInfo queryProApplyInfoByName(String projectName) throws ProjectException {
        String proApplySuccess = RedisKey.getBizProApplicant(projectName);
        String proApplyFailed = RedisKey.getBizProApplyFail(projectName);
        String proApplying = RedisKey.getBizProApplying(projectName);
        ProApplyInfo proApplyInfo = new ProApplyInfo();
        try {
            proApplyInfo.setApplySuccess(redisToList.redisToList(proApplySuccess));
            proApplyInfo.setApplyFailed(redisToList.redisToList(proApplyFailed));
            proApplyInfo.setApplying(redisToList.redisToList(proApplying));
            return proApplyInfo;
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public int deleteProByProName(String projectName, HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            int count = projectDao.deletePro(projectName, username);
            if (count != 1) {
                throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int updateProByName(String projectName,ProjectInfo projectInfo, HttpServletRequest request) {
        try {
            checkProjectInfo.checkProjectInfo(projectInfo);
            String username = getUsrName.AllProjects(request);
            if (projectDao.queryProjectDetail(projectInfo.getProjectName()) != null) {
                throw new ProjectException(ExceptionEnum.REPEATE_PRO_NAME.getMsg());
            }
            int modCount = projectDao.updatePro(projectName, username, projectInfo);
            if (modCount != 1) {
                throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ApplyPro getApplyDetail(HttpServletRequest request) throws RuntimeException{
        String username = getUsrName.AllProjects(request);
        String applySuccess = RedisKey.getBizJoinSuccess(username);
        String applyFailed = RedisKey.getBizJoinFail(username);
        String apllying = RedisKey.getBizApplyingPro(username);
        List<String> applySuccessList = redisToList.redisToList(applySuccess);
        List<String> applyFailedList = redisToList.redisToList(applyFailed);
        List<String> applyingList = redisToList.redisToList(apllying);
        ApplyPro applyPro = new ApplyPro(applySuccessList, applyFailedList, applyingList);
        return applyPro;
    }


}
