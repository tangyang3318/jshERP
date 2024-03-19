package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskExample;
import com.jsh.erp.datasource.entities.TaskMaterial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskMaterialMapper extends BaseMapper<TaskMaterial> {

    Page<TaskMaterial> searchTaskMaterial( IPage<TaskMaterial> page, @Param("taskMaterial") TaskMaterial taskMaterial);
}
