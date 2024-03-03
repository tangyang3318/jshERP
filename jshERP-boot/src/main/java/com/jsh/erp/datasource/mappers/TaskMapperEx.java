package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.DepotEx;
import com.jsh.erp.datasource.entities.TaskEx;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TaskMapperEx {

    List<TaskEx> selectByConditionTask(@Param("taskEx") TaskEx taskEx,
                                       @Param("planBeginTime") String planBeginTime,
                                       @Param("planEndTime") String planEndTime,
                                       @Param("beginTime") String beginTime,
                                       @Param("endTime") String endTime,
                                       @Param("offset") int offset,
                                       @Param("rows") int rows);

    Long countsByTask ( @Param("taskEx") TaskEx taskEx,
                        @Param("planBeginTime") String planBeginTime,
                        @Param("planEndTime") String planEndTime,
                        @Param("beginTime") String beginTime,
                        @Param("endTime") String endTime);
}
