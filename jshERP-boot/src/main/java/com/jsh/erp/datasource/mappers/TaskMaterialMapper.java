package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskExample;
import com.jsh.erp.datasource.entities.TaskMaterial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskMaterialMapper extends BaseMapper<TaskMaterial> {
}
