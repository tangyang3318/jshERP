package com.jsh.erp.utils;

import com.jsh.erp.datasource.entities.TaskProcesses;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.util.List;
import java.util.stream.Collectors;


public class ProcessesUtils {

    public static List<TaskProcesses> getChildList(List<TaskProcesses> processesList, Long id) {
        return processesList.stream().filter(item -> item.getParentProcesses().longValue() == id.longValue()).collect(Collectors.toList());
    }
}
