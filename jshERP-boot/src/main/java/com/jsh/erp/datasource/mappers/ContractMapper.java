package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entities.Contract;
import com.jsh.erp.datasource.entities.TaskMaterial;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ContractMapper extends BaseMapper<Contract> {

}
