package com.example.hospital.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import com.example.hospital.api.db.dao.*;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.VideoDiagnoseService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private VideoDiagnoseFileDao videoDiagnoseFileDao;

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
        if(!"医生".equals(job)){
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
        String nextOrder=String.valueOf(list.get(1));

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
            if(job!=null && !job.equals(tempJob)){
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
    public HashMap createVideoDiagnose(int userId, VideoDiagnoseEntity entity) {
        HashMap result = new HashMap();
        HashMap map = userDao.searchByUserId(userId);
//        String date=MapUtil.getStr(map,"date");
        String openId = MapUtil.getStr(map, "openId");
        int patientCardId = MapUtil.getInt(map, "patientCardId");
        entity.setPatientCardId(patientCardId);
        String key = "online_doctor_" + entity.getDoctorId();
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
            expectStart = now.offsetNew(DateField.MINUTE, 3).toString("yyyy-MM-dd HH:mm:ss");
            expectEnd = now.offsetNew(DateField.MINUTE, 8).toString("yyyy-MM-dd HH:mm:ss");
        }
        //如果有当前问诊患者，就以当前问诊结束时间预估开始和结束时间
        else {
            DateTime currentEnd = new DateTime(MapUtil.getStr(entries, "currentEnd"));
            expectStart = currentEnd.offsetNew(DateField.MINUTE, 3).toString("yyyy-MM-dd HH:mm:ss");
            expectEnd = currentEnd.offsetNew(DateField.MINUTE, 8).toString("yyyy-MM-dd HH:mm:ss");
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
//        result.put("timeStamp",date);
        result.put("amount",price);
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
    @Override
    public HashMap searchVideoDiagnoseInfo(int userId){
        HashMap map=new HashMap();
        int docotrId=this.searchDoctorId(userId);
        String key="online_doctor_"+docotrId;
        if(!redisTemplate.hasKey(key)){
            return map;
        }
        Map entries=redisTemplate.opsForHash().entries(key);
        String currentOrder=MapUtil.getStr(entries,"currentOrder");
        String nextOrder=MapUtil.getStr(entries,"nextOrder");
        Boolean currentPayment=MapUtil.getBool(entries,"currentPayment");
        Boolean nextPayment=MapUtil.getBool(entries,"nextPayment");
//        if(!"none".equals(currentOrder) && (currentPayment!=null && currentPayment)){
        if(!"none".equals(currentOrder)){
            int diagnoseId=Integer.parseInt(currentOrder);
            Map currentInfo=videoDiagnoseDao.searchVideoDiagnoseInfo(diagnoseId);
            map.put("currentInfo",currentInfo);
        }
//        if(!"none".equals(nextOrder) && (nextPayment!=null && nextPayment)){
        if(!"none".equals(nextOrder)){
            int diagnoseId=Integer.parseInt(nextOrder);
            Map nextInfo=videoDiagnoseDao.searchVideoDiagnoseInfo(diagnoseId);
            map.put("nextInfo",nextInfo);
//            map.put("nextPayment",nextPayment);
        }
        return map;
    }

    @Override
    public HashMap refreshInfo(int userId) {
        int doctorId=this.searchDoctorId(userId);
        String key="online_doctor_"+doctorId;
//        把医生上线缓存的部分属性数据取出来，保存到这个Map中返回给浏览器
        HashMap map=new HashMap();

//        不存在医生上线缓存的情况
        if(!redisTemplate.hasKey(key)){
            map.put("status","offline");
            return map;
        }
        Map entries=redisTemplate.opsForHash().entries(key);
        map.put("status","online");
        map.put("open",MapUtil.getBool(entries,"open"));
        map.put("currentOrder",MapUtil.getStr(entries,"currentOrder"));
        Integer currentStatus=MapUtil.getInt(entries,"currentStatus");
        map.put("currentStatus",currentStatus);
        //如果患者已经付款会有程序往上线缓存中添加以及视频问诊RoomId
        if(currentStatus!=null&& currentStatus==2){
            map.put("roomId",MapUtil.getStr(entries,"roomId"));
        }
        map.put("currentStart",MapUtil.getStr(entries,"currentStart"));
        map.put("currentEnd",MapUtil.getStr(entries,"currentEnd"));
        return map;
    }

    @Override
    @Transactional
    public boolean updatePayment(Map param){
        String outTradeNo=MapUtil.getStr(param,"outTradeNo");
        //根据流水号查询挂号信息
        HashMap data=videoDiagnoseDao.searchByOutTradeNo(outTradeNo);
        int id=MapUtil.getInt(data,"id");
        int doctorId=MapUtil.getInt(data,"doctorId");
        String key="online_doctor_"+doctorId;
        //获取医生上线缓存
        Map entries=redisTemplate.opsForHash().entries(key);
        //获取当前问诊挂号单ID
        String currentOrder=MapUtil.getStr(entries,"currentOrder");
        String nextOrder=MapUtil.getStr(entries,"nextOrder");
        //如果挂号单是当前问诊的，就更新上线缓存
        if(currentOrder.equals(id+"")){
            redisTemplate.opsForHash().put(key,"currentPayment",true);
            redisTemplate.opsForHash().put(key,"currentNotify",false);

        }
        //如果挂号单是排队问诊的，就更新上线缓存
        else if(nextOrder.equals(id+"")){
           redisTemplate.opsForHash().put(key,"nextPayment",true);
           redisTemplate.opsForHash().put(key,"nextNotify",false);
        }
        //如果挂号单既不属于当前问诊页不属于排队问诊，就执行退款
        else{
            log.error("没有找到缓存，无法跟进付款缓存");
        }
//        更新数据库中的付款状态
        videoDiagnoseDao.updatePayment(param);
        int paymentStatus=MapUtil.getInt(param,"paymentStatus");
        if(paymentStatus==2){
            redisTemplate.expire("patient_video_diagnose_payment#"+id,5, TimeUnit.SECONDS);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean searchPaymentResult(String outTradeNo){
        String transactionId = videoDiagnoseDao.searchPaymentResult(outTradeNo);
        if(transactionId !=null){
            //更新挂号单为已付款，并且记录transactionId
            this.updatePayment(new HashMap(){{
                put("outTradeNo",outTradeNo);
                put("transactionId",transactionId);
                put("paymentStatus",2);
            }});
            return true;
        }else{
            return false;
        }
    }

    //存放的是minio服务器的地址
    @Value("${minio.endpoint}")
    private String endpoint;

    //minio的账户和密码
    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Override
    //事务的注解
    @Transactional
    public String uploadImg(MultipartFile file, Integer videoDiagnoseId){
        HashMap map = new HashMap();
        try{
            //随机生成文件名
            String filename=IdUtil.simpleUUID()+".jpg";
            String path="patient-wx/video_diagnose/"+filename;
            //在Minio中保存医生照片
            //创建连接
            MinioClient client=new MinioClient.Builder()
                    .endpoint(endpoint).credentials(accessKey,secretKey).build();
            client.putObject(PutObjectArgs.builder().bucket("hospital")
                    .object(path)
                    .stream(file.getInputStream(),-1,5*1024*1024)
                    //上传文件的类型
                    .contentType("image/jpeg").build());
            map.put("videoDiagnoseId",videoDiagnoseId);
            map.put("filename",filename);
            map.put("path",path);
            videoDiagnoseFileDao.uploadImg(map);
            return filename;
        }catch(Exception e) {
            log.error("保存就诊材料失败", e);
            throw new HospitalException("保存就诊材料失败");
        }
    }

    @Override
    public ArrayList<HashMap> searchImageByVideoDiagnoseId(int videoDiagnoseId){
        ArrayList<HashMap> list=videoDiagnoseFileDao.searchImageByVideoDiagnoseId(videoDiagnoseId);
        return list;
    }

    @Override
    public void deleteImage(Map param){
        int videoDiagnoseId=MapUtil.getInt(param,"videoDiagnoseId");
        //判断是否包含filename参数
        String filename=MapUtil.getStr(param,"filename");
        //删除某张照片
        if(filename!=null){
            this.removeImageFile(filename);
        }
        //删除该视频问诊所有照片
        else{
            //查询该视频所有照片
            ArrayList<String> list = videoDiagnoseFileDao.searchfilenameByVideoDiagnoseId(videoDiagnoseId);
            list.forEach(one->{
                this.removeImageFile(one);
            });
        }
        //删除数据表记录
        videoDiagnoseFileDao.delete(param);
    }

    private void removeImageFile(String filename){
        try{
            String path="patient-wx/video_diagnose/"+filename;
            //删除minio文件
            MinioClient client=new MinioClient.Builder()
                    .endpoint(endpoint).credentials(accessKey,secretKey).build();
            client.removeObject(RemoveObjectArgs.builder().bucket("hospital").object(path).build());
        }catch (Exception e){
            log.error("删除视频问诊图片失败",e);
            throw new HospitalException("删除视频问诊图片失败");
        }
    }

    @Override
    public String searchRoomId(int doctorId){
        String key="online_doctor_"+doctorId;
        Map entries=redisTemplate.opsForHash().entries(key);
        String roomId=MapUtil.getStr(entries,"roomId");
        return roomId;
    }

    @Override
    public ArrayList<String> searchImgByVideoDiagnoseId(int videoDiagnoseId){
        ArrayList<String> list=videoDiagnoseFileDao.searchImgByVideoDiagnoseId(videoDiagnoseId);
        return list;
    }

}
