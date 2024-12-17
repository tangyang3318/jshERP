package com.jsh.erp.service.contract;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entities.Contract;
import com.jsh.erp.datasource.mappers.ContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public IPage<Contract>  selectPageList(Map<String, String> map, int pageNo, int pageSize){
        if(pageNo <= 0){
            pageNo = 1;
        }
        if(pageSize <= 0){
            pageSize = 10;
        }
        IPage<Contract> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
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
