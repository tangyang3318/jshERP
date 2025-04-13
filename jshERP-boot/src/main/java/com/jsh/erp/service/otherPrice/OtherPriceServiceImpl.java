package com.jsh.erp.service.otherPrice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.jsh.erp.datasource.entities.OtherPrice;
import com.jsh.erp.datasource.mappers.OtherPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OtherPriceServiceImpl implements OtherPriceService{

    @Autowired
    OtherPriceDao otherPriceDao;

    @Override
    public int insertBeatchPrice(List<OtherPrice> otherPrices) {
        int i = 0 ;
        if(!CollectionUtils.isEmpty(otherPrices)){
            for (OtherPrice otherPrice : otherPrices) {
                otherPriceDao.insert(otherPrice);
                i++;
            }
        }
        return i;
    }

    @Override
    public int updateBeatchPrice(List<OtherPrice> otherPrices) {
        int i = 0 ;
        if(!CollectionUtils.isEmpty(otherPrices)){
            for (OtherPrice otherPrice : otherPrices) {
                otherPriceDao.updateById(otherPrice);
                i++;
            }
        }
        return i;
    }

    @Override
    public int updateBeatchByDepotId(List<OtherPrice> otherPrices, Long depotId) {
        int i= 0;
        if(!CollectionUtils.isEmpty(otherPrices) && ObjectUtils.isNotEmpty(depotId)){
            for (OtherPrice otherPrice : otherPrices) {
                if(otherPrice.getId() == null ){
                    otherPrice.setDepotId(depotId);
                    otherPriceDao.insert(otherPrice);
                }else{
                    otherPriceDao.updateById(otherPrice);
                }
                i +=1;
            }
            //删除不存在的id
            List<Long> collect = otherPrices.stream().map(OtherPrice::getId).collect(Collectors.toList());
            if(ObjectUtils.isNotEmpty(collect)){
                i += deleteNotInIds(collect);
            }
        }
        return i;
    }

    @Override
    public int deleteBeatchPrice(List<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            return otherPriceDao.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public int deleteNotInIds(List<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            QueryWrapper<OtherPrice> queryWrapper = new QueryWrapper<>();
            queryWrapper.notIn("id",ids);
            return otherPriceDao.delete(queryWrapper);
        }
        return 0;
    }

    @Override
    public int deleteByDepotIds(List<Long> depotIds) {
        if(ObjectUtils.isNotEmpty(depotIds)){
            QueryWrapper<OtherPrice> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("depot_id",depotIds);
            return otherPriceDao.delete(queryWrapper);
        }
        return 0;
    }

    @Override
    public List<OtherPrice> getOtherPriceListByDepotId(Long depotId) {
        QueryWrapper<OtherPrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depot_id",depotId);
        return otherPriceDao.selectList(queryWrapper);
    }
}
