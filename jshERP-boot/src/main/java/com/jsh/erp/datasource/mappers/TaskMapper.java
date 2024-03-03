package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    // 根据id获取任务
    Task selectByPrimaryKey(Long id);
    // 查询总数
    long countByExample(TaskExample example);

    int deleteByExample(TaskExample example);

     int deleteByPrimaryKey(Long id);

     int insert(Task record);

     int insertSelective(Task record);

     List<Task> selectByExample(TaskExample example);


    int updateByExampleSelective(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByExample(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
}
