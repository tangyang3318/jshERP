package com.jsh.erp.datasource.entities;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Author: TangYang
 * @Description:
 * @Date: 23:27 2024/12/25
 */
@Data
public class AccountRequestVo {
    private  Integer currentPage;
    private Integer pageSize;
    private Long accountId;
    private BigDecimal initialAmount;
    private Long contractId;
}
