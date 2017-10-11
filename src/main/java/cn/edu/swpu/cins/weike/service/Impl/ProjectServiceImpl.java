package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.view.IndexVO;
import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.exception.StudentException;
import cn.edu.swpu.cins.weike.exception.TeacherException;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.service.StudentService;
import cn.edu.swpu.cins.weike.service.TeacherService;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.ProjectDao;

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
    public ProjectDetail showProject(String projectName) throws ProjectException {
        String proClickNum = RedisKey.getBizProClickNum(projectName);
        try {
            long hitsNum = jedisAdapter.incr(proClickNum);
            jedisAdapter.add(proClickNum, hitsNum);
            ProjectDetail projectDetail = projectDao.queryProjectDetail(projectName);
            projectDetail.setProHits(hitsNum);
            return projectDetail;
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public List<ProjectView> queryByKeyWords(String keyWords) throws ProjectException {
        try {
            return projectDao.queryByKeywords(keyWords);
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
    public List<IndexVO> queryForIndex(int offset) throws ProjectException{
        try {
            List<ProjectDetail> projectDetails=projectDao.queryForIndex((--offset) * pageCount, pageCount);
            List<IndexVO> indexVOList=projectDetails
                    .stream()
                    .map(IndexVO::new).collect(Collectors.toList()).stream().map(indexVO -> {
                //项目详细情况
                String proName=indexVO.getProjectDetails().getProjectName();
                String proClickNum = RedisKey.getBizProClickNum(proName);
                long hitsNum = Long.parseLong(jedisAdapter.get(proClickNum));
                indexVO.getProjectDetails().setProHits(hitsNum);
                //项目申请成功的人
                String proApplySuccess = RedisKey.getBizProApplicant(proName);
                List<String> applyPersons=jedisAdapter.smenber(proApplySuccess).stream().collect(Collectors.toList());
                indexVO.setApplySuccessPerson(applyPersons);
                //项目成功申请的人数
                indexVO.setApplySuccessNum(applyPersons.size());
                //项目关注的人
                String proFollowerKeys = RedisKey.getBizProFollower(proName);
                List<String> proFollowers=jedisAdapter.smenber(proFollowerKeys).stream().collect(Collectors.toList());
                indexVO.setFollowPros(proFollowers);
                //项目关注人数
                indexVO.setFollowNum(proFollowers.size());
                //项目发布人详细信息
                        String projectConnector = indexVO.getProjectDetails().getProjectConnector();
                        try {
                            if (studentService.queryForData(projectConnector) == null) {
                                indexVO.setPersonData(teacherService.queryForData(projectConnector));
                            }
                            else {
                                indexVO.setPersonData(studentService.queryForData(projectConnector));
                            }
                        }
                        catch (Exception e){
                            try {
                                throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
                            } catch (ProjectException e1) {
                                e1.printStackTrace();
                            }
                        }
                return indexVO;
            }).collect(Collectors.toList());
            return indexVOList;
        }
        catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
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
