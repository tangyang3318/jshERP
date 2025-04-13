package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("other_price")
public class OtherPrice implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private BigDecimal priceNumber;
    private String priceTypeName;
    private Long depotId;
    private Long tenantId;
    private String otherDesc;
}
