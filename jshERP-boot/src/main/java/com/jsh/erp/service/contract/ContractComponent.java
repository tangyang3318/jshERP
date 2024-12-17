package com.jsh.erp.service.contract;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.Contract;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.QueryUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "contract_component")
@ContractResource
public class ContractComponent implements ICommonQuery {
    @Resource
    private ContractService contractService;
    @Resource
    private UserService userService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return contractService.searchOne(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return contractService.selectList(map, QueryUtils.currentPage(map), QueryUtils.pageSize(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        return contractService.selectCount(map, QueryUtils.currentPage(map), QueryUtils.pageSize(map));
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        Contract contract = JSONObject.parseObject(obj.toJSONString(),Contract.class);
        User userInfo = userService.getCurrentUser();
        contract.setCreator(userInfo==null?null:userInfo.getId());
        contract.setCreateTime(new Date());
        contract.setStatus("0");
        return contractService.insert(contract);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        Contract contract = JSONObject.parseObject(obj.toJSONString(),Contract.class);
        return contractService.update(contract);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return contractService.deleteById(id);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return contractService.deleteByIds(ids);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return contractService.checkIsNameExist(id,name);
    }

}
