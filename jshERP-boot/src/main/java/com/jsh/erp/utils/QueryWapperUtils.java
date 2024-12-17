package com.jsh.erp.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: TangYang
 * @Description:
 * @Date: 11:06 2024/12/15
 */
@Component
public class QueryWapperUtils {
    public static void setQueryWapperByMap(QueryWrapper queryWapperByMap, Map<String, String> map){
        if(map != null && map.keySet() != null && map.keySet().size() > 0){
            for (String key : map.keySet()) {
                queryWapperByMap.eq(key,map.get(key));
            }
        }
    }
}
