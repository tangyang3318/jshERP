package com.jsh.erp.service.contract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jsh.erp.datasource.entities.Contract;
import com.jsh.erp.datasource.mappers.ContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: TangYang
 * @Description:
 * @Date: 10:40 2024/12/15
 */
@Service
public class ContractService {
    @Autowired
    ContractMapper contractMapper;

    public Contract searchOne(Long id) {
        return contractMapper.selectById(id);
    }

    public int searchByCode(String code) {
        QueryWrapper<Contract> contractQueryWrapper = new QueryWrapper<>();
        contractQueryWrapper.eq("contract_code",code);
        return contractMapper.selectCount(contractQueryWrapper);
    }

    public IPage<Contract>  selectPageList(Map<String, String> map, int pageNo, int pageSize){
        if(pageNo <= 0){
            pageNo = 1;
        }
        if(pageSize <= 0){
            pageSize = 10;
        }
        IPage<Contract> page = new Page<>(pageNo, pageSize);
        Contract contract = JSONObject.parseObject(map.get("search"),Contract.class);
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(contract.getContractCode())) {
            queryWrapper.eq(Contract::getContractCode,contract.getContractCode());
        }
        if (StringUtils.isNotEmpty(contract.getContractName())) {
            queryWrapper.eq(Contract::getContractName,contract.getContractName());
        }
        if (StringUtils.isNotEmpty(contract.getTimeRange())) {
            JSONArray objects = JSONObject.parseArray(contract.getTimeRange());
            queryWrapper.between(Contract::getCreateTime,objects.getString(0),objects.getString(1));
        }
        queryWrapper.orderByDesc(Contract::getCreateTime);
        return contractMapper.selectPage(page, queryWrapper);
    }

    public List<Contract> selectList(Map<String, String> map, int pageNo, int pageSize) {
        IPage<Contract> mapIPage = selectPageList(map,pageNo, pageSize);
        return mapIPage.getRecords();
    }

    public Long selectCount(Map<String, String> map, int pageNo, int pageSize) {
        IPage<Contract> mapIPage = selectPageList(map,pageNo, pageSize);
        return mapIPage.getTotal();
    }

    public int insert(Contract contract) {
        return contractMapper.insert(contract);
    }

    public int update(Contract contract) {
        return contractMapper.updateById(contract);
    }

    public int deleteById(Long id) {
        return contractMapper.deleteById(id);
    }

    public int deleteByIds(String ids) {
        return contractMapper.deleteBatchIds(Arrays.asList(ids.split(",")));
    }

    public int checkIsNameExist(Long id, String name) {
        LambdaQueryWrapper<Contract> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getId,id);
        queryWrapper.eq(Contract::getContractName,name);
        Integer integer = contractMapper.selectCount(queryWrapper);
        return integer;
    }
}
