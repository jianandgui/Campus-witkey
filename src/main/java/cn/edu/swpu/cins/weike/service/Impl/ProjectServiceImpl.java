package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.StudentService;
import cn.edu.swpu.cins.weike.service.TeacherService;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    JedisAdapter jedisAdapter;

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
        try {
            //项目关注人
            List<String> proFollowers=jedisAdapter.smenber(proFollowerKeys).stream().collect(Collectors.toList());

            indexVO.setFollowPros(proFollowers);
            //项目关注人数
            indexVO.setFollowNum(proFollowers.size());
            //项目申请成功的人
            List<String> applyPersons=jedisAdapter.smenber(proApplySuccess).stream().collect(Collectors.toList());
            indexVO.setApplySuccessPerson(applyPersons);
            //项目成功申请的人数
            indexVO.setApplySuccessNum(applyPersons.size());
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
    public List<IndexVO> queryForIndex(int offset) {
        try {
            List<ProjectDetail> projectDetails=projectDao.queryForIndex((--offset) * pageCount, pageCount);
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
                    //项目详细情况
                    String proName=indexVO.getProjectDetails().getProjectName();

                    String proClickNum = RedisKey.getBizProClickNum(proName);
                    long hitsNum;
                    if (jedisAdapter.get(proClickNum) != null) {
                         hitsNum= Long.parseLong(jedisAdapter.get(proClickNum));
                        indexVO.getProjectDetails().setProHits(hitsNum);
                    }
                    String projectConnector = indexVO.getProjectDetails().getProjectConnector();
                    getPersonData(indexVO, projectConnector,proName);
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
            proApplyInfo.setApplySuccess(jedisAdapter.smenber(proApplySuccess).stream().collect(Collectors.toList()));
            proApplyInfo.setApplyFailed(jedisAdapter.smenber(proApplyFailed).stream().collect(Collectors.toList()));
            proApplyInfo.setApplying(jedisAdapter.smenber(proApplying).stream().collect(Collectors.toList()));
            return proApplyInfo;
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }


}
