package com.jsh.erp.datasource.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/2/25 11:40
 */
@Data
public class TaskEx extends Task {
    //负责人名字
    private String creatorName;
    //商品
    private Material material;
}
