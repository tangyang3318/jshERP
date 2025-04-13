package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import com.jsh.erp.datasource.entities.OtherPrice;

@Component
public interface OtherPriceDao extends BaseMapper<OtherPrice> {

}
