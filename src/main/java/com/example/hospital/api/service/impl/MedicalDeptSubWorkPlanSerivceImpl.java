package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.db.Entity.DoctorWorkPlanEntity;
import com.example.hospital.api.db.Entity.DoctorWorkPlanScheduleEntity;
import com.example.hospital.api.db.dao.DoctorWorkPlanDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.DoctorWorkPlanScheduleService;
import com.example.hospital.api.service.MedicalDeptSubWorkPlanService;
import com.tencentcloudapi.vpc.v20170312.models.CreateLocalGatewayRequest;
import com.tencentcloudapi.vpc.v20170312.models.LocalGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static cn.hutool.core.util.NumberUtil.add;

@Slf4j
@Service
public class MedicalDeptSubWorkPlanSerivceImpl implements MedicalDeptSubWorkPlanService {
    @Resource
    private DoctorWorkPlanDao doctorWorkPlanDao;

    @Resource
    private DoctorWorkPlanScheduleService doctorWorkPlanScheduleService;

    @Override
    public JSONArray searchWorkPlanInRange (Map param, ArrayList dataList){
        ArrayList<HashMap> list=doctorWorkPlanDao.searchWorkPlanInRange(param);
        //保存诊室id
        Integer tempSubId=null;
        //保存出诊时间
        String tempDate=null;
        //保存临时处理的结果
        HashMap tempResult=new HashMap();
        for(HashMap one:list){
            String deptName= MapUtil.getStr(one,"deptName");
            int deptSubId=MapUtil.getInt(one,"deptSubId");
            String deptSubName=MapUtil.getStr(one,"deptSubName");
            String doctorName=MapUtil.getStr(one,"doctorName");
            int workPlanId=MapUtil.getInt(one,"workPlanId");
            String date=MapUtil.getStr(one,"date");
            //判断是不是第一条记录
            if(tempSubId ==null){
                tempSubId=deptSubId;
                tempDate=date;
                //把第一条记录当做一个诊室
                HashMap temp=new HashMap(){{
                    put("deptName",deptName);
                    put("deptSubId",deptSubId);
                    put("deptSubName",deptSubName);
                    //该诊室出诊计划
                    put("plan",new LinkedHashMap<>(){{
                        put(date,new ArrayList<>(){{
                            add(doctorName);
                        }});
                    }});
                }};
                tempResult.put(deptSubId,temp);
            }
            //非第一条记录，但是该记录与前一条记录是相同诊室，而且是同一天出诊
            else if(tempSubId ==deptSubId && tempDate.equals(date)){
                //取出该诊室
                HashMap map=(HashMap) tempResult.get(deptSubId);
                //从诊室中取出出诊计划
                LinkedHashMap plan=(LinkedHashMap) map.get("plan");
                //找到该天出诊医生名单列表
                ArrayList doctors=(ArrayList) plan.get(date);
                //把医生名字添加到列表中
                doctors.add(doctorName);
            }
            //非第一条记录，但是该记录与前一条记录是相同诊室，但不是同一天出诊
            else if(tempSubId ==deptSubId && !tempDate.equals(date)){
                tempDate=date;//更新日期
                //取出该诊室
                HashMap map=(HashMap) tempResult.get(deptSubId);
                //从诊室中取出出诊计划
                LinkedHashMap plan=(LinkedHashMap) map.get("plan");
                //创建新的出诊日期列表，添加该医生的名字
                plan.put(date,new ArrayList<>(){{
                    add(doctorName);
                }});
            }
            //如果该记录与上一个记录不是同诊室
            else if(tempSubId !=deptSubId){
                tempSubId=deptSubId;
                tempDate=date;
                //创建新的诊室对象
                HashMap temp=new HashMap(){{
                    put("deptName",deptName);
                    put("deptSubId",deptSubId);
                    put("deptSubName",deptSubName);
                    //出诊计划
                    put("plan",new LinkedHashMap<>(){{
                        //添加出诊列表
                        put(date,new ArrayList<>(){{
                            add(doctorName);
                        }});
                    }});
                }};
                //把新诊室对象添加到结果集
                tempResult.put(deptSubId,temp);
            }
        }
        //为了循环HashMap中的元素，所以提取所有的元素
        Set<Map.Entry> set=tempResult.entrySet();

        //循环每个元素
        set.forEach(one ->{
            //诊室对象
            HashMap map=(HashMap) one.getValue();
            //该诊室的出诊计划
            LinkedHashMap plan=(LinkedHashMap) map.get("plan");
            //业务方法第二个参数，提取每个日期，判断出诊计划中是否有该日期
            //如果出诊计划没有该日期，说明该天没有医生出诊
            dataList.forEach(date->{
                if(!plan.containsKey(date)){
                    //某天没有医生出诊，就往出诊计划中添加空的名单列表
                    plan.put(date,new ArrayList<>());
                }
            });
            //由于往LinkedHashMap中添加的新元素（空的出诊列表）,所以要对所有元素排序
            TreeMap sort=MapUtil.sort(plan, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    String key1=(String) o1;
                    String key2=(String) o2;
                    boolean bool=new DateTime(key1).isAfter(new DateTime(key2));
                    return bool ? 1:-1;
                }
            });
            //把排好序的出诊计划更新到诊室对象中
            map.replace("plan",sort);
        });
        //每个诊室的plan是TreeMap，我们要转换成列表形式，将来才能变成JSON数组
        Collection<HashMap> values=tempResult.values();
        values.forEach(one ->{
            TreeMap plan=(TreeMap) one.get("plan");
            //取出TreeMap每个元素
            Set<Map.Entry> tempSet=plan.entrySet();
            ArrayList temp=new ArrayList();
            //把出诊计划保存到列表中
            tempSet.forEach(entry -> {
                temp.add(new HashMap<>(){{
                    put("date",entry.getKey());
                    put("doctors",entry.getValue());
                }});
            });
            //更新诊室对象的plan
            one.replace("plan",temp);
        });
        return JSONUtil.parseArray(values);
    }
    @Override
    public String insert(Map param){
        //查询当天该医生是否存在出诊记录
        Integer id=doctorWorkPlanDao.searchId(param);
        if(id!=null){
            return "已经存在出诊计划，不能重复添加";
        }
        DoctorWorkPlanEntity entity= BeanUtil.toBean(param,DoctorWorkPlanEntity.class);
        int totalMaximum=MapUtil.getInt(param,"totalMaximum");
        entity.setMaximum(totalMaximum);
        //保存出诊计划
        doctorWorkPlanDao.insert(entity);
        //查询出诊计划主键值
        id=doctorWorkPlanDao.searchId(param);
        Integer[] slots=(Integer[]) param.get("slots");
        int slotMaximum=MapUtil.getInt(param,"slotMaximum");
        ArrayList<DoctorWorkPlanScheduleEntity>list=new ArrayList<>();
        for(Integer slot:slots){
            DoctorWorkPlanScheduleEntity entity1=BeanUtil.toBean(param,DoctorWorkPlanScheduleEntity.class);
            entity1.setWorkPlanId(id);
            entity1.setMaximum(slotMaximum);
            entity1.setSlot(slot);
            list.add(entity1);
        }
        doctorWorkPlanScheduleService.insert(list);
        return "插入成功";
    }

    @Override
    @Transactional
    public void deleteWorkPlan(int workPlanId){
        //查询出诊计划挂号人数
        Integer num=doctorWorkPlanDao.searchNumById(workPlanId);
        if(num>0){
            throw new HospitalException("该出诊计划已经有患者，禁止删除");
        }
        doctorWorkPlanDao.deleteById(workPlanId);
        doctorWorkPlanScheduleService.deleteByWorkPlanId(workPlanId);
    }
}
