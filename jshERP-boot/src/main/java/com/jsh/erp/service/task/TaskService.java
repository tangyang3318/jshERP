package com.jsh.erp.service.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.*;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.service.log.LogService;
import com.jsh.erp.service.systemConfig.SystemConfigService;
import com.jsh.erp.service.taskProcesses.TaskProcessesService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.service.userBusiness.UserBusinessService;
import com.jsh.erp.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskMapperEx taskMapperEx;
    @Resource
    private UserService userService;
    @Resource
    private TaskMaterialMapper taskMaterialMapper;
    @Resource
    private TaskProcessesMapper taskProcessesMapper;
    @Resource
    private UserBusinessService userBusinessService;
    @Resource
    private LogService logService;
    @Resource
    private DepotHeadMapperEx depotHeadMapperEx;
    @Resource
    private DepotItemMapperEx depotItemMapperEx;

    public Task getTask(long id)throws Exception {
        Task task=null;
        try{
            task=taskMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return task;
    }

    public List<Task> getTaskListByIds(String ids) throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Task> list = new ArrayList<>();
        try{
            TaskExample example = new TaskExample();
            example.createCriteria().andIdIn(idList);
            list = taskMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Task> getTask() throws Exception {
        TaskExample example = new TaskExample();
//        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Task> list=null;
        try{
            list=taskMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Task> getAllList()throws Exception {
        TaskExample example = new TaskExample();
        example.createCriteria().andEnabledEqualTo(true).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("sort asc, id desc");
        List<Task> list=null;
        try{
            list=taskMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Task selectById(Long id){
        return taskMapper.selectByPrimaryKey(id);
    }

    public List<TaskEx> select( TaskEx taskEx, String planBeginTime,
                                String planEndTime, String beginTime,
                                String endTime, int offset, int rows)throws Exception {
        List<TaskEx> list=null;
        try{
            list=taskMapperEx.selectByConditionTask(taskEx, planBeginTime, planEndTime,beginTime,endTime, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countTask(TaskEx taskEx, String planBeginTime,
                          String planEndTime, String beginTime,
                          String endTime)throws Exception {
        Long result=null;
        try{
            result=taskMapperEx.countsByTask(taskEx, planBeginTime, planEndTime,beginTime,endTime);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteTaskByIds(List<Long> ids)throws Exception {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andIdIn(ids);
        //todo 还需要删除工序等
        return  taskMapper.deleteByExample(taskExample);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertTask(Task task) throws Exception {
        List<TaskMaterial> taskMaterialList = task.getTaskMaterialList();
        List<TaskProcesses> taskProcessesList = task.getTaskProcessesList();
        User userInfo=userService.getCurrentUser();
        task.setCreator(userInfo.getId());
        task.setStatus(BusinessConstants.TASK_STATE_STATUS_UN_AUDIT);
        task.setCreateTime(new Date());
        task.setOverQuantity(new BigDecimal(0));
        // 1. 新增任务
        int insert = taskMapper.insert(task);
        // 2. 新增耗材
        if(!CollectionUtils.isEmpty(taskMaterialList)){
            for (TaskMaterial taskMaterial : taskMaterialList) {
                taskMaterial.setCreateTime(new Date());
                taskMaterial.setCreator(userInfo.getId());
                taskMaterial.setTaskId(task.getId());
                taskMaterial.setTemplate(BusinessConstants.IS_NOT_TEMPLETE);
                taskMaterialMapper.insert(taskMaterial);
            }
        }
        // 3. 新增工序
        if(!CollectionUtils.isEmpty(taskProcessesList)){
            for (TaskProcesses taskProcesses : taskProcessesList) {
                taskProcesses.setCreateTime(new Date());
                taskProcesses.setCreator(userInfo.getId());
                taskProcesses.setTaskId(task.getId());
                taskProcesses.setTemplate(BusinessConstants.IS_NOT_TEMPLETE);
                taskProcesses.setStatus(BusinessConstants.PROCESSES_STATE_STATUS_UN_AUDIT);
                taskProcessesMapper.insert(taskProcesses);
            }
        }
        return 1;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateTask(Task task) {
        return taskMapper.updateByPrimaryKeySelective(task);
    }

    public BigDecimal getCanWarehousing(Long taskId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        return task.getOverQuantity();
    }

    public void overTask(Long taskId) throws Exception {
        // 1. 任务排查是否数量够了。
        Task task = this.getTask(taskId);
        if(task.getQuantity().subtract(task.getOverQuantity()).compareTo(new BigDecimal(0)) > 0 ){
            throw new BusinessRunTimeException(ExceptionConstants.QUANTITY_NOT_ENOUGH_ERROR_CODE,
                    ExceptionConstants.QUANTITY_NOT_ENOUGH_ERROR_MSG);
        }
        // 2. 排查下面工序任务是否都已完成
        QueryWrapper<TaskProcesses> taskProcessesQueryWrapper = new QueryWrapper<>();
        taskProcessesQueryWrapper.eq("task_id",taskId);
        List<TaskProcesses> taskProcesses = taskProcessesMapper.selectList(taskProcessesQueryWrapper);
        if(!CollectionUtils.isEmpty(taskProcesses)){
            List<TaskProcesses> collect = taskProcesses.stream().filter(item -> "1".equals(item.getStatus())).collect(Collectors.toList());
            if(collect.size() > 0){
                throw new BusinessRunTimeException(ExceptionConstants.QUANTITY_NOT_COMPLETE_ERROR_CODE,
                        ExceptionConstants.QUANTITY_NOT_COMPLETE_ERROR_MSG);
            }
        }
        task.setStatus(BusinessConstants.TASK_STATE_STATUS_END);
        this.updateTask(task);
    }

    public Map<String,Integer> getTaskFinishMessage() {
        Map<String,Integer> returnmap = new HashMap<>();
        // 获取
        List<Task> tasks = taskMapper.selectByExample(new TaskExample());
        returnmap.put("taskTotalNumber",tasks.size());
        Integer notFinishNumber = 0 , finishNumberNumber = 0
                ,postponeNumber = 0,onScheduleNumber = 0;
        for (Task task : tasks) {
            if(task.getStatus().equals(BusinessConstants.TASK_STATE_STATUS_END)){
                finishNumberNumber++;
                //已完工的。
                if(task.getOverTime().after(task.getPlanOverTime())) {
                    postponeNumber ++;
                }else{
                    onScheduleNumber ++;
                }
            }else{
                notFinishNumber ++;
                if(task.getPlanOverTime() != null && new Date().after(task.getPlanOverTime())) {
                    postponeNumber ++;
                }
            }
        }
        returnmap.put("notFinishNumber",notFinishNumber);
        returnmap.put("finishNumberNumber",finishNumberNumber);
        returnmap.put("postponeNumber",postponeNumber);
        returnmap.put("onScheduleNumber",onScheduleNumber);
        return returnmap;
    }

//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int insertTask(JSONObject obj, HttpServletRequest request)throws Exception {
//        Task task = JSONObject.parseObject(obj.toJSONString(), Task.class);
//        int result=0;
//        try{
//            depot.setType(0);
//            List<Task> depotList = getTask();
//            if(depotList.size() == 0) {
//                depot.setIsDefault(true);
//            } else {
//                depot.setIsDefault(false);
//            }
//            depot.setEnabled(true);
//            result=taskMapper.insertSelective(depot);
//            //新增仓库时给当前用户自动授权
//            Long userId = userService.getUserId(request);
//            Long depotId = getIdByName(depot.getName());
//            String ubKey = "[" + depotId + "]";
//            List<UserBusiness> ubList = userBusinessService.getBasicData(userId.toString(), "UserDepot");
//            if(ubList ==null || ubList.size() == 0) {
//                JSONObject ubObj = new JSONObject();
//                ubObj.put("type", "UserDepot");
//                ubObj.put("keyId", userId);
//                ubObj.put("value", ubKey);
//                userBusinessService.insertUserBusiness(ubObj, request);
//            } else {
//                UserBusiness ubInfo = ubList.get(0);
//                JSONObject ubObj = new JSONObject();
//                ubObj.put("id", ubInfo.getId());
//                ubObj.put("type", ubInfo.getType());
//                ubObj.put("keyId", ubInfo.getKeyId());
//                ubObj.put("value", ubInfo.getValue() + ubKey);
//                userBusinessService.updateUserBusiness(ubObj, request);
//            }
//            logService.insertLog("仓库",
//                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(depot.getName()).toString(), request);
//        }catch(Exception e){
//            JshException.writeFail(logger, e);
//        }
//        return result;
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int updateDepot(JSONObject obj, HttpServletRequest request) throws Exception{
//        Depot depot = JSONObject.parseObject(obj.toJSONString(), Depot.class);
//        int result=0;
//        try{
//            result= taskMapper.updateByPrimaryKeySelective(depot);
//            logService.insertLog("仓库",
//                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(depot.getName()).toString(), request);
//        }catch(Exception e){
//            JshException.writeFail(logger, e);
//        }
//        return result;
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int deleteDepot(Long id, HttpServletRequest request)throws Exception {
//        return batchDeleteDepotByIds(id.toString());
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int batchDeleteDepot(String ids, HttpServletRequest request) throws Exception{
//        return batchDeleteDepotByIds(ids);
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int batchDeleteDepotByIds(String ids)throws Exception {
//        int result=0;
//        String [] idArray=ids.split(",");
//        //校验单据子表	jsh_depot_item
//        List<DepotItem> depotItemList=null;
//        try{
//            depotItemList = depotItemMapperEx.getTaskItemListListByDepotIds(idArray);
//        }catch(Exception e){
//            JshException.readFail(logger, e);
//        }
//        if(depotItemList!=null&&depotItemList.size()>0){
//            logger.error("异常码[{}],异常提示[{}],参数,DepotIds[{}]",
//                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
//            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
//                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
//        }
//        //记录日志
//        StringBuffer sb = new StringBuffer();
//        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
//        List<Task> list = getTaskListByIds(ids);
//        for(Depot depot: list){
//            sb.append("[").append(depot.getName()).append("]");
//        }
//        logService.insertLog("仓库", sb.toString(),
//                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
//        User userInfo=userService.getCurrentUser();
//        //校验通过执行删除操作
//        try{
//            result = taskMapperEx.batchDeleteDepotByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
//        }catch(Exception e){
//            JshException.writeFail(logger, e);
//        }
//        return result;
//    }
//
//    public int checkIsNameExist(Long id, String name)throws Exception {
//        TaskExample example = new TaskExample();
//        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
//        List<Task> list=null;
//        try{
//            list= taskMapper.selectByExample(example);
//        }catch(Exception e){
//            JshException.readFail(logger, e);
//        }
//        return list==null?0:list.size();
//    }
//
//    public List<Task> findUserDepot()throws Exception{
//        TaskExample example = new TaskExample();
//        example.createCriteria().andTypeEqualTo(0).andEnabledEqualTo(true)
//                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
//        example.setOrderByClause("sort asc, id desc");
//        List<Task> list=null;
//        try{
//            list= taskMapper.selectByExample(example);
//        }catch(Exception e){
//            JshException.readFail(logger, e);
//        }
//        return list;
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int updateIsDefault(Long depotId) throws Exception{
//        int result=0;
//        try{
//            //全部取消默认
//            Depot allDepot = new Depot();
//            allDepot.setIsDefault(false);
//            TaskExample allExample = new TaskExample();
//            allExample.createCriteria();
//            taskMapper.updateByExampleSelective(allDepot, allExample);
//            //给指定仓库设为默认
//            Depot depot = new Depot();
//            depot.setIsDefault(true);
//            TaskExample example = new TaskExample();
//            example.createCriteria().andIdEqualTo(depotId);
//            taskMapper.updateByExampleSelective(depot, example);
//            logService.insertLog("仓库",BusinessConstants.LOG_OPERATION_TYPE_EDIT+depotId,
//                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
//            result = 1;
//        }catch(Exception e){
//            JshException.writeFail(logger, e);
//        }
//        return result;
//    }
//
//    /**
//     * 根据名称获取id
//     * @param name
//     */
//    public Long getIdByName(String name){
//        Long id = 0L;
//        TaskExample example = new TaskExample();
//        example.createCriteria().andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
//        List<Task> list = taskMapper.selectByExample(example);
//        if(list!=null && list.size()>0) {
//            id = list.get(0).getId();
//        }
//        return id;
//    }
//
//    public List<Long> parseDepotList(Long depotId) throws Exception {
//        List<Long> depotList = new ArrayList<>();
//        if(depotId !=null) {
//            depotList.add(depotId);
//        } else {
//            //未选择仓库时默认为当前用户有权限的仓库
//            JSONArray depotArr = findDepotByCurrentUser();
//            for(Object obj: depotArr) {
//                JSONObject object = JSONObject.parseObject(obj.toString());
//                depotList.add(object.getLong("id"));
//            }
//        }
//        return depotList;
//    }
//
//    public JSONArray findDepotByCurrentUser() throws Exception {
//        JSONArray arr = new JSONArray();
//        String type = "UserDepot";
//        Long userId = userService.getCurrentUser().getId();
//        List<Task> dataList = findUserDepot();
//        //开始拼接json数据
//        if (null != dataList) {
//            boolean depotFlag = systemConfigService.getTaskFlag();
//            if(depotFlag) {
//                List<UserBusiness> list = userBusinessService.getBasicData(userId.toString(), type);
//                if(list!=null && list.size()>0) {
//                    String depotStr = list.get(0).getValue();
//                    if(StringUtil.isNotEmpty(depotStr)){
//                        depotStr = depotStr.replaceAll("\\[", "").replaceAll("]", ",");
//                        String[] depotArr = depotStr.split(",");
//                        for (Depot depot : dataList) {
//                            for(String depotId: depotArr) {
//                                if(depot.getId() == Long.parseLong(depotId)){
//                                    JSONObject item = new JSONObject();
//                                    item.put("id", depot.getId());
//                                    item.put("depotName", depot.getName());
//                                    item.put("isDefault", depot.getIsDefault());
//                                    arr.add(item);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                for (Depot depot : dataList) {
//                    JSONObject item = new JSONObject();
//                    item.put("id", depot.getId());
//                    item.put("depotName", depot.getName());
//                    item.put("isDefault", depot.getIsDefault());
//                    arr.add(item);
//                }
//            }
//        }
//        return arr;
//    }
//
//    /**
//     * 当前用户有权限使用的仓库列表的id，用逗号隔开
//     * @return
//     * @throws Exception
//     */
//    public String findDepotStrByCurrentUser() throws Exception {
//        JSONArray arr =  findDepotByCurrentUser();
//        StringBuffer sb = new StringBuffer();
//        for(Object object: arr) {
//            JSONObject obj = (JSONObject)object;
//            sb.append(obj.getLong("id")).append(",");
//        }
//        String depotStr = sb.toString();
//        if(StringUtil.isNotEmpty(depotStr)){
//            depotStr = depotStr.substring(0, depotStr.length()-1);
//        }
//        return depotStr;
//    }
//
//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
//    public int batchSetStatus(Boolean status, String ids)throws Exception {
//        logService.insertLog("仓库",
//                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ENABLED).toString(),
//                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
//        List<Long> depotIds = StringUtil.strToLongList(ids);
//        Depot depot = new Depot();
//        depot.setEnabled(status);
//        TaskExample example = new TaskExample();
//        example.createCriteria().andIdIn(depotIds);
//        int result=0;
//        try{
//            result = taskMapper.updateByExampleSelective(depot, example);
//        }catch(Exception e){
//            JshException.writeFail(logger, e);
//        }
//        return result;
//    }
}
