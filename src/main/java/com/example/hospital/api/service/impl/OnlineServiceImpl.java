package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.db.dao.DoctorDao;
import com.example.hospital.api.db.dao.MisUserDao;
import com.example.hospital.api.db.dao.UserDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.OnlineService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class OnlineServiceImpl implements OnlineService {
    @Resource
    private DoctorDao doctorDao;

    @Resource
    private MisUserDao misUserDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void online(int userId){
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
        //判断是否存在该医生的上线缓存
        if(redisTemplate.hasKey(key)){
            return;
        }
        //查询医生详情
        HashMap map=doctorDao.searchDataForOnlineCache(doctorId);
        //创建医生上线缓存
        redisTemplate.opsForHash().putAll(key,new HashMap(map){{
            put("open",true);
        }});
    }

    public int searchDoctorId(int userId){
        HashMap map=misUserDao.searchRefId(userId);
        String job= MapUtil.getStr(map,"job");
        if(!"医生".equals(job)){
            throw new HospitalException("当前用户不是医生");
        }
        Integer refId=MapUtil.getInt(map,"refId");
        if(refId==null){
            throw new HospitalException("当前用户没有关联医生表");
        }
        return refId;
    }

    @Override
    public boolean offline(int userId){
        //查找医生的主键值
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
        //判断是否存在上线缓存
        if(!redisTemplate.hasKey(key)){
            return true;
        }
        //删除医生的上线缓存
        redisTemplate.delete(key);
        return true;
    }

    @Override
    public ArrayList<HashMap> searchOnlineDoctorList(String subName, String job){
        ArrayList<HashMap> list=new ArrayList<>();
        //查找缓存的医生
        Set<String> keys=redisTemplate.keys("online_doctor_*");
        for(String key:keys){
            Map entries=redisTemplate.opsForHash().entries(key);
            String tempSubName=MapUtil.getStr(entries,"subName");
            String tempJob=MapUtil.getStr(entries,"job");
            boolean open=MapUtil.getBool(entries,"open");
            if(!open){
                continue;
            }
            //过滤掉不是规定诊室的医生
            if(subName!=null && !subName.equals(tempSubName)){
                continue;
            }
            //过滤掉不是规定诊室的医生
            if(job!=null && !job.equals(tempJob)){
                continue;
            }
            HashMap map=new HashMap(){{
                put("doctorId",MapUtil.getInt(entries,"doctorId"));
                put("userId",MapUtil.getInt(entries,"userId"));
                put("name",MapUtil.getStr(entries,"name"));
                put("photo",MapUtil.getStr(entries,"photo"));
                put("job",MapUtil.getStr(entries,"job"));
                put("description",MapUtil.getStr(entries,"description"));
                put("remark",MapUtil.getStr(entries,"remark"));
                put("price",MapUtil.getStr(entries,"price"));
            }};
            list.add(map);
        }
        return list;
    }
    @Override
    public Boolean searchisOnline(int doctorId){
        //查找缓存的医生
        Set<String> keys=redisTemplate.keys("online_doctor_"+doctorId);
        if(keys.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean isStatus(int userId){
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
        //判断是否存在该医生的上线缓存
        if(!redisTemplate.hasKey(key)){
            return false;
        }
        //添加医生答疑状态
        redisTemplate.opsForHash().put(key,"status",true);
        return true;
    }

    @Override
    public Boolean deitStatus(int userId){
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
        //判断是否存在该医生的上线缓存
        if(!redisTemplate.hasKey(key)){
            return false;
        }
        //修改答疑状态
        redisTemplate.opsForHash().put(key,"status",false);
        return true;
    }

    @Override
    public Boolean searchStatus(int doctorId){
        String key="online_doctor_"+doctorId;
        //获取医生上线缓存
        Map entries=redisTemplate.opsForHash().entries(key);
        Boolean status=MapUtil.getBool(entries,"status");
        return status;

    }
}
