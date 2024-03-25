package com.jsh.erp.service.taskMaterial;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.*;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.service.depotItem.DepotItemService;
import com.jsh.erp.service.log.LogService;
import com.jsh.erp.service.material.MaterialService;
import com.jsh.erp.service.sequence.SequenceService;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.service.userBusiness.UserBusinessService;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskMaterialService {
    private Logger logger = LoggerFactory.getLogger(TaskMaterialService.class);
    @Resource
    private TaskMaterialMapper taskMaterialMapper;
    @Resource
    private DepotHeadMapper depotHeadMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private MaterialService materialService;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private DepotItemService depotItemService;
    @Resource
    private UserService userService;

    public IPage<TaskMaterial> getListByPage(Long taskId,Integer pageNo,Integer pageSize) {
        IPage page = new Page();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
        }
        QueryWrapper<TaskMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        IPage page1 = taskMaterialMapper.selectPage(page, queryWrapper);
        return page1;
    }

    public List<TaskMaterial> getListByIds(List<Long> ids) {
        QueryWrapper<TaskMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        return taskMaterialMapper.selectList(queryWrapper);
    }

    public TaskMaterial getTaskMaterialByid(Long id) {
        QueryWrapper<TaskMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",id);
        return taskMaterialMapper.selectOne(queryWrapper);
    }

    /**
     * 领料（批量领料）
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void getMaterial(JSONObject jsonObject) throws Exception {
        JSONArray taskMaterialList = jsonObject.getJSONArray("taskMaterialList");
        List<TaskMaterial> taskMaterialList1 = taskMaterialList.toJavaList(TaskMaterial.class);
        Map<Long, String> collect = taskMaterialList1.stream().collect(Collectors.toMap(TaskMaterial::getId, item -> {
            return item.getMaterialEntity() != null ? item.getMaterialEntity().getUnit() : null;
        }));
        Long depotId = jsonObject.getLong("depotId");
        Long taskId = jsonObject.getLong("taskId");
        BigDecimal getMaterialNumber = jsonObject.getBigDecimal("getMaterial");
        Boolean flag = false;
        List<Long> ids = new ArrayList<>(collect.keySet());
        Map<Long,BigDecimal> currentStockMap = new HashMap<>();
        User userInfo=userService.getCurrentUser();
        String prefixNo = "QTCK";
        // 1. 校验数据是否正确。能否领料成功
        List<TaskMaterial> listByIds = this.getListByIds(ids);
        Task task = taskService.selectById(taskId);
        for (TaskMaterial listById : listByIds) {
            BigDecimal currentStockByParam = depotItemService.getCurrentStockByParam(depotId, listById.getMaterialId());
            BigDecimal needNumber = (ids.size() == 1 && getMaterialNumber != null) ? getMaterialNumber : listById.getMaterialNeedNumber().subtract(listById.getMaterialHasNumber()).subtract(listById.getMaterialUseNumber());
            currentStockMap.put(listById.getId(),currentStockByParam);
            int compareTo = currentStockByParam.compareTo( needNumber);
            if(compareTo < 0){
                throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_CODE,
                        String.format(ExceptionConstants.MATERIAL_STOCK_NOT_ENOUGH_MSG,listById.getMaterialEntity().getName()));
            }
        }
        // 2. 创建其他出库订单。
        // 2.1 生成出库订单主表信息
        DepotHead depotHead = new DepotHead();
        depotHead.setLinkNumber(task.getBillNo() + " 任务领料");
        String number = prefixNo + sequenceService.buildOnlyNumber() ;
        depotHead.setNumber(number);
        depotHead.setDefaultNumber(number);
        depotHead.setOperTime(new Date());
        depotHead.setSubType(BusinessConstants.SUB_TYPE_OTHER);
        depotHead.setType(BusinessConstants.DEPOTHEAD_TYPE_OUT);
        depotHead.setChangeAmount(BigDecimal.ZERO);
        depotHead.setTotalPrice(BigDecimal.ZERO);
        depotHead.setDiscountLastMoney(BigDecimal.ZERO);
        depotHead.setCreator(userInfo==null?null:userInfo.getId());
        depotHead.setOrganId(null);
        depotHead.setAccountId(null);
        depotHead.setStatus(BusinessConstants.BILLS_STATUS_UN_AUDIT);
        depotHead.setTenantId(null);
        depotHead.setId(null);
        depotHeadMapper.insert(depotHead);
        List<Map<String,Object>> list = new ArrayList();
        // 2.2 生成出库订单子表信息
        for (TaskMaterial listById : listByIds) {
            BigDecimal decimal = (ids.size() == 1 && getMaterialNumber != null) ? getMaterialNumber : listById.getMaterialNeedNumber().subtract(listById.getMaterialHasNumber()).subtract(listById.getMaterialUseNumber());
            if(decimal.compareTo(new BigDecimal(0)) > 0 ){
                Map<String,Object> map = new HashMap<>();
                map.put("name",listById.getMaterialEntity().getName());
                map.put("standard",null);
                map.put("model",null);
                map.put("color",null);
                map.put("materialOther",null);
                map.put("stock",currentStockMap.get(listById.getId()));
                map.put("unit",collect.get(listById.getId()));
                map.put("snList","");
                map.put("batchNumber","");
                map.put("expirationDate","");
                map.put("sku","");
                map.put("preNumber","");
                map.put("finishNumber","");
                map.put("operNumber",decimal);
                map.put("unitPrice",null);
                map.put("allPrice",null);
                map.put("remark","");
                map.put("linkId","");
                map.put("depotId",depotId);
                map.put("barCode",listById.getBarCode());
                map.put("orderNum",1);
                list.add(map);
                //设置领料后的值
                listById.setMaterialHasNumber(listById.getMaterialHasNumber().add(decimal));
                listById.setMaterialGetNumber(listById.getMaterialGetNumber().add(decimal));
                // 3. 修改任务材料表。
                taskMaterialMapper.updateById(listById);
                flag = true;
            }
        }
        if(flag){
            depotItemService.saveDetials(JSONObject.toJSONString(list),depotHead.getId(), "add",null);
        }else{
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_GET_ERROR_CODE, ExceptionConstants.MATERIAL_GET_ERROR_MSG);
        }
    }

    public void useMaterial(List<Long> ids) {
        // 1. 获取所有数据
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        // 2. 修改任务材料数据，并且保存
        List<TaskMaterial> listByIds = this.getListByIds(ids);
        for (TaskMaterial listById : listByIds) {
            if(listById.getMaterialHasNumber().compareTo(new BigDecimal(0)) > 0){
                listById.setMaterialUseNumber(listById.getMaterialUseNumber().add(listById.getMaterialHasNumber()));
                listById.setMaterialHasNumber(new BigDecimal(0));
                taskMaterialMapper.updateById(listById);
            }
        }
    }

    public void useMaterialByNumber(JSONObject jsonObject) {
        Long taskMaterialId = jsonObject.getLong("taskMaterialId");
        BigDecimal useNumber = jsonObject.getBigDecimal("useNumber");
        BigDecimal lostNumber = jsonObject.getBigDecimal("lostNumber");
        if(taskMaterialId == null){
            return;
        }
        TaskMaterial taskMaterialByid = this.getTaskMaterialByid(taskMaterialId);
        if(taskMaterialByid.getMaterialHasNumber().compareTo(useNumber) < 0){
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_USE_ERROR_CODE, ExceptionConstants.MATERIAL_USE_ERROR_MSG);
        }
        taskMaterialByid.setMaterialHasNumber(taskMaterialByid.getMaterialHasNumber().subtract(useNumber));
        taskMaterialByid.setMaterialUseNumber(taskMaterialByid.getMaterialUseNumber().add(useNumber));
        taskMaterialByid.setMaterialLostNumber(taskMaterialByid.getMaterialLostNumber().add(lostNumber));
        taskMaterialMapper.updateById(taskMaterialByid);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertTaskMaterial(TaskMaterial taskMaterial) throws Exception {
        taskMaterial.setMaterialHasNumber(new BigDecimal(0));
        taskMaterial.setMaterialGetNumber(new BigDecimal(0));
        taskMaterial.setMaterialReturnNumber(new BigDecimal(0));
        taskMaterial.setMaterialUseNumber(new BigDecimal(0));
        taskMaterial.setMaterialLostNumber(new BigDecimal(0));
        taskMaterial.setStatus(BusinessConstants.PROCESSES_NO_COM);
        taskMaterial.setCreateTime(new Date());
        User userInfo=userService.getCurrentUser();
        taskMaterial.setCreator(userInfo.getId());
        if(taskMaterial.getTemplate() == null || taskMaterial.getTemplate().equals(BusinessConstants.IS_NOT_TEMPLETE)){
            taskMaterial.setTemplate(BusinessConstants.IS_NOT_TEMPLETE);
        }else{
            taskMaterial.setTemplate(BusinessConstants.IS_TEMPLETE);
        }
        taskMaterialMapper.insert(taskMaterial);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateTaskMaterial(TaskMaterial taskMaterial) {
        taskMaterialMapper.updateById(taskMaterial);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void warehousingProduct(JSONObject jsonObject) throws Exception {
        Long depotId = jsonObject.getLong("depotId");
        Long taskId = jsonObject.getLong("taskId");
        String barCode = jsonObject.getString("barCode");
        String unit = jsonObject.getString("unit");
        BigDecimal operNumber = jsonObject.getBigDecimal("operNumber");
        Task task = taskService.getTask(taskId);
        if(task == null){
            return;
        }
        Long materialId = task.getMaterialId();
        Material material = materialService.getMaterial(materialId);
        BigDecimal currentStockByParam = depotItemService.getCurrentStockByParam(depotId, material.getId());
        User userInfo=userService.getCurrentUser();
        String prefixNo = "QTRK";
        // 1. 创建其他出库订单。
        DepotHead depotHead = new DepotHead();
        depotHead.setLinkNumber(task.getBillNo() + "任务入库");
        String number = prefixNo + sequenceService.buildOnlyNumber() ;
        depotHead.setNumber(number);
        depotHead.setDefaultNumber(number);
        depotHead.setOperTime(new Date());
        depotHead.setSubType(BusinessConstants.SUB_TYPE_OTHER);
        depotHead.setType(BusinessConstants.DEPOTHEAD_TYPE_IN);
        depotHead.setChangeAmount(BigDecimal.ZERO);
        depotHead.setTotalPrice(BigDecimal.ZERO);
        depotHead.setDiscountLastMoney(BigDecimal.ZERO);
        depotHead.setCreator(userInfo==null?null:userInfo.getId());
        depotHead.setOrganId(null);
        depotHead.setAccountId(null);
        depotHead.setStatus(BusinessConstants.BILLS_STATUS_UN_AUDIT);
        depotHead.setTenantId(null);
        depotHead.setId(null);
        depotHeadMapper.insert(depotHead);
        List<Map<String,Object>> list = new ArrayList();
        // 2.2 生成出库订单子表信息
        Map<String,Object> map = new HashMap<>();
        map.put("name",material.getName());
        map.put("standard",null);
        map.put("model",null);
        map.put("color",null);
        map.put("materialOther",null);
        map.put("stock",currentStockByParam);
        map.put("unit",unit);
        map.put("snList","");
        map.put("batchNumber","");
        map.put("expirationDate","");
        map.put("sku","");
        map.put("preNumber","");
        map.put("finishNumber","");
        map.put("operNumber",operNumber);
        map.put("unitPrice",null);
        map.put("allPrice",null);
        map.put("remark","");
        map.put("linkId","");
        map.put("depotId",depotId);
        map.put("barCode",barCode);
        map.put("orderNum",1);
        list.add(map);
        //设置领料后的值
        task.setOverQuantity(task.getOverQuantity().add(operNumber));
        // 3. 修改任务表。
        taskService.updateTask(task);
        depotItemService.saveDetials(JSONObject.toJSONString(list),depotHead.getId(), "add",null);
    }

    public void deleteTaskMaterial(List<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            taskMaterialMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 查询所需物料
     * @param taskMaterial
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<TaskMaterial> searchTaskMaterial(TaskMaterial taskMaterial, Integer pageNo, Integer pageSize) {
        IPage<TaskMaterial> page = new Page();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
        }
        return taskMaterialMapper.searchTaskMaterial(page,taskMaterial);
    }

    public void setPurchaseOrder(JSONObject jsonObject) {

    }

    public List<TaskMaterial> searchTaskMaterialTempByBarCode(String barCord) {
        QueryWrapper<TaskMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bar_code",barCord);
        queryWrapper.eq("template",BusinessConstants.IS_TEMPLETE);
        return taskMaterialMapper.selectList(queryWrapper);
    }
}
