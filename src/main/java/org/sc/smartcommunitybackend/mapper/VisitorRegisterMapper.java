package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.VisitorRegister;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【visitor_register(访客登记表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.VisitorRegister
*/
@Mapper
public interface VisitorRegisterMapper extends BaseMapper<VisitorRegister> {

}




