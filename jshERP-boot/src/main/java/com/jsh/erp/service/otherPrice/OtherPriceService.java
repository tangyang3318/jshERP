package com.jsh.erp.service.otherPrice;

import com.jsh.erp.datasource.entities.OtherPrice;
import com.jsh.erp.datasource.mappers.OtherPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OtherPriceService {
    /**
     * 批量新增
     * @param otherPrices
     * @return
     */
     int insertBeatchPrice(List<OtherPrice> otherPrices);

    /**
     * 批量编辑
     * @param otherPrices
     * @return
     */
    int updateBeatchPrice(List<OtherPrice> otherPrices);

    /**
     * 根据depotId 批量编辑
     * @param otherPrices
     * @return
     */
    int updateBeatchByDepotId(List<OtherPrice> otherPrices,Long depotId);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBeatchPrice(List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteNotInIds(List<Long> ids);

    /**
     * 批量删除
     * @param depotIds
     * @return
     */
    int deleteByDepotIds(List<Long> depotIds);


    /**
     * 批量获取结果集
     * @param depotId
     * @return
     */
    List<OtherPrice> getOtherPriceListByDepotId(Long depotId);

}
