package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.entity.view.ProApplyInfo;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.entity.view.ProjectView;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.service.ProjectService;
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

    @Override
    public List<ProjectDetail> queryForIndex() throws ProjectException {
        try {
            List<ProjectDetail> projectDetails=projectDao.queryForIndex();
            projectDetails.forEach(projectDetail -> {
                String proClickNum = RedisKey.getBizProClickNum(projectDetail.getProjectName());
                long hitsNum = jedisAdapter.incr(proClickNum);
                projectDetail.setProHits(hitsNum);

            });
            return projectDetails;
        } catch (Exception e) {
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
