package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by muyi on 17-4-18.
 */
@Repository
@Mapper
public interface AdminDao {

    AdminInfo queryByName(String AdminName);

    int addAdmin(AdminInfo adminInfo);
}
