package com.example.hospital.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import com.example.hospital.api.db.dao.DoctorDao;
import com.example.hospital.api.db.dao.MisUserDao;
import com.example.hospital.api.db.dao.UserDao;
import com.example.hospital.api.db.dao.VideoDiagnoseDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.VideoDiagnoseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.ws.rs.PUT;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class VideoDiagnoseServiceImpl implements VideoDiagnoseService {
    @Resource
    private DoctorDao doctorDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MisUserDao misUserDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private VideoDiagnoseDao videoDiagnoseDao;

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
            put("open",false);
            put("currentPatient","none");//当前患者ID
            put("currentOrder","none");//当前问诊订单号
            put("currentHandle",false);//当前问诊是否处理了
            put("currentStart","none");//开始时间
            put("currentEnd","none");//结束时间
            put("currentPayment",false);//当前问诊是否付款
            put("currentStatus",1);//1未开始 2问诊中 3已结束
            put("currentNotify",false);//未推送给医生端
            put("nextPatient","none");//候诊患者ID
            put("nextOrder","none");//等候问诊的订单号
            put("nextStart","none");//等候问诊的开始时间
            put("nextEnd","none");//等候问诊的结束时间
            put("nextPayment",false);//排队问诊是否付款
            put("nextNotify",false);//未推送给医生端
        }});
    }

    public int searchDoctorId(int userId){
        HashMap map=misUserDao.searchRefId(userId);
        String job= MapUtil.getStr(map,"job");
//        if(!"医生".equals(job)){
        if("医生".equals(job)){
            throw new HospitalException("当前用户是医生");
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
        //检查是否正在执行的问诊和等待的问诊
        List<String> list=redisTemplate.opsForHash().multiGet(key,new ArrayList<>(){{
            add("currentOrder");
            add("nextOrder");
        }});
        String currentOrder=list.get(0);
        String nextOrder=list.get(1);

        //存在当前问诊或者排队问诊，医生就不能下线
        if(!"none".equals(currentOrder)||!"none".equals(nextOrder)){
            return false;
        }
        //删除医生的问诊缓存
        redisTemplate.delete(key);
        return true;
    }

    @Override
    public void updateOpenFlag(int userId,boolean open){
        //查询医生的主键值
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
        //如果不存在上线缓存，医生都没上线，所以就不需要更新open属性值
        if(!redisTemplate.hasKey(key)){
            return;
        }
        //更新open属性值，开放或者关闭挂号
        redisTemplate.opsForHash().put(key,"open",open);
    }

    @Override
    public ArrayList<HashMap> searchOnlineDoctorList(String subName,String job){
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
            if(job!=null && job.equals(tempJob)){
                continue;
            }
            HashMap map=new HashMap(){{
                put("doctorId",MapUtil.getInt(entries,"doctorId"));
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
    @Transactional
    public HashMap createVideoDiagnose(int userId, VideoDiagnoseEntity entity) {
        HashMap result = new HashMap();
        HashMap map = userDao.searchUserInfo(userId);
        String date=MapUtil.getStr(map,"date");
        String openId = MapUtil.getStr(map, "openId");
        int patientCardId = MapUtil.getInt(map, "patientCardId");
        entity.setPatientCardId(patientCardId);
        String key = "online_dotor_" + entity.getDoctorId();
        //从缓存中获取该医生的挂号金额
        String price = redisTemplate.opsForHash().get(key, "price").toString();
        int amount = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
        entity.setAmount(new BigDecimal(price));

        //尝试用Reis事务挂号
        boolean execute = (Boolean) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //获取医生上线缓存的Version
                operations.watch(key);
                //获取医生上线缓存数据
                Map entries = operations.opsForHash().entries(key);
                boolean open = MapUtil.getBool(entries, "open");
                //如果医生没开启挂号，就不能挂号
                if (!open) {
                    return false;
                }
                String nextPatient = MapUtil.getStr(entries, "nextPatient");
                //如果存在候诊的患者就不能挂号（只能存在一个候诊患者）
                if (!"none".equals(nextPatient)) {
                    return false;
                }
                try {
                    //开启Redis事务
                    operations.multi();
                    //把自己登记为候诊患者
                    operations.opsForHash().put(key, "nextPatient", patientCardId);
                    //提交事务
                    operations.exec();
                    return true;
                } catch (Exception e) {
                    log.debug("事务提交失败", e);
                    return false;
                }
            }
        });
        result.put("flag", execute);
        if (!execute) {
            return result;
        }
        DateTime now = new DateTime();
        //获取医生上线缓存的数据
        Map entries = redisTemplate.opsForHash().entries(key);
        //当前问诊患者ID
        String currentPatient = MapUtil.getStr(entries, "currentPatient");
        String expectStart = null;
        String expectEnd = null;
        //如果没有当前问诊患者，定义计时开始和结束时间
        if ("none".equals(currentPatient)) {
            expectStart = now.offsetNew(DateField.MINUTE, 1).toString("yyyy-MM-dd HH:mm:ss");
            expectEnd = now.offsetNew(DateField.MINUTE, 16).toString("yyyy-MM-dd HH:mm:ss");

        }
        //如果有当前问诊患者，就以当前问诊结束时间预估开始和结束时间
        else {
            DateTime currentEnd = new DateTime(MapUtil.getStr(entries, "currentEnd"));
            expectStart = currentEnd.offsetNew(DateField.MINUTE, 1).toString("yyyy-MM-dd HH:mm:ss");
            expectEnd = currentEnd.offsetNew(DateField.MINUTE, 16).toString("yyyy-MM-dd HH:mm:ss");
        }
        entity.setExpectStart(expectStart);
        entity.setExpectEnd(expectEnd);
        String outTradeNo = IdUtil.simpleUUID().toUpperCase();
        //设置支付时间为1分钟，
        String timeExpire = now.offsetNew(DateField.MINUTE, 1).toString("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
        //生成支付订单
        //预支付订单
        String prepayId=IdUtil.simpleUUID().toUpperCase();
        entity.setPrepayId(prepayId);
        entity.setOutTradeNo(outTradeNo);
        //保存问诊记录
        videoDiagnoseDao.insert(entity);

        //查询视频问诊记录的主键值
        HashMap data=videoDiagnoseDao.searchByOutTradeNo(outTradeNo);
        int id=MapUtil.getInt(data,"id");
        HashMap cache=new HashMap();
        cache.put("nextOrder",id);
        cache.put("nextPayment",false);
        cache.put("nextStart",expectStart);
        cache.put("nextEnd",expectEnd);
        cache.put("nextNotify",false);
        //更新缓存数据
        redisTemplate.opsForHash().putAll(key,cache);
        //微信支付的相关信息
        result.put("outTradeNo",outTradeNo);
        result.put("prepayId",prepayId);
        result.put("timeStamp",date);
        result.put("amount",amount);
        result.put("videoDiagnoseId",id);
        result.put("expectStart",expectStart);
        result.put("expectEnd",expectEnd);

        //创建付款缓存，1分钟之后销毁
        String paymentKey="patient_video_diagnose_payment#"+id;
        redisTemplate.opsForValue().set(paymentKey,false);
        //接收付款通知消息之后和主动查询付款结果都需要短暂的时间，所以缓存过期时间要设置大于60秒
        redisTemplate.expireAt(paymentKey,now.offset(DateField.SECOND,90));
        return result;
    }
}
