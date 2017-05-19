package weike.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import weike.entity.persistence.AdminInfo;

/**
 * Created by muyi on 17-4-18.
 */
@Repository
@Mapper
public interface AdminDao {

    public AdminInfo queryByName(String AdminName);

    public int addAdmin(AdminInfo adminInfo);
}
