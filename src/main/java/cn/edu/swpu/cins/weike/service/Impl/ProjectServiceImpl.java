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


    //显示所有项目(三个字段)
    @Override
    public List<ProjectView> showProjectAll(int offset, int limit) throws ProjectException {
        try {
            return projectDao.queryAll(offset * pageCount, pageCount);
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    //查询一个项目的详细信息
    @Override
    public ProjectDetail showProject(String projectName)  throws ProjectException{
        try {
            String proClickNum = RedisKey.getBizProClickNum(projectName);
            long hitsNum=jedisAdapter.incr(proClickNum);
            ProjectDetail projectDetail=projectDao.queryProjectDetail(projectName);
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
    public List<ProjectView> queryForIndex() throws ProjectException {
        try {
            return projectDao.queryForIndex();
        } catch (Exception e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public ProApplyInfo queryProApplyInfoByName(String projectName) {
        String proApplySuccess = RedisKey.getBizProApplicant(projectName);
        String proApplyFailed =RedisKey.getBizProApplyFail(projectName);
        String proApplying = RedisKey.getBizProApplying(projectName);
        ProApplyInfo proApplyInfo=new ProApplyInfo();
        proApplyInfo.setApllySuccess(jedisAdapter.smenber(proApplySuccess).stream().collect(Collectors.toList()));
        proApplyInfo.setApplyFailed(jedisAdapter.smenber(proApplyFailed).stream().collect(Collectors.toList()));
        proApplyInfo.setApplying(jedisAdapter.smenber(proApplying).stream().collect(Collectors.toList()));
        return proApplyInfo;
    }
}
