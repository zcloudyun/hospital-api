package com.example.hospital.api.config;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.sax.ElementName;
import com.example.hospital.api.db.dao.*;
import com.example.hospital.api.socket.WebSocketService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomByStrRoomIdRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
    @Value("${tencent.cloud.secretId}")
    private String cloudSecretId;

    @Value("${tencent.cloud.secretKey}")
    private String cloudSecretKey;

    @Value("${tencent.trtc.appId}")
    private long trtcAppId;

    @Resource
    private MedicalRegistrationDao medicalRegistrationDao;

    @Resource
    private DoctorWorkPlanScheduleDao doctorWorkPlanScheduleDao;

    @Resource
    private DoctorWorkPlanDao doctorWorkPlanDao;

    @Resource
    private VideoDiagnoseDao videoDiagnoseDao;

    @Resource
    private MisUserDao misUserDao;

    @Resource
    private RedisTemplate redisTemplate;


    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer){
        super(listenerContainer);
    }

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern){
        //获取过期数据的key
        String key=message.toString();
        if(key.startsWith("video_diagnose_")){
            String temp=key.substring(key.lastIndexOf("_")+1);
            int currentOrder=Integer.parseInt(temp.split("#")[0]);
            int doctorId=Integer.parseInt(temp.split("#")[1]);
            int userId=misUserDao.searchUserId(new HashMap(){{
                put("refId",doctorId);
                put("job","医生");
            }});
            String onlineKey="online_doctor_"+doctorId;
            if(key.startsWith("video_diagnose_start_")){
                redisTemplate.opsForHash().put(onlineKey,"currentStatus",2);
//                String roomId=redisTemplate.opsForHash().get(onlineKey,"roomId").toString();
                String roomId= IdUtil.simpleUUID().toUpperCase();
                redisTemplate.opsForHash().put(onlineKey,"roomId",roomId);
                HashMap param=new HashMap(){{
                    put("id",currentOrder);
                    put("status",2);
                    put("realStart",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
                }};
                videoDiagnoseDao.updateStatus(param);
                WebSocketService.sendInfo("StartDiagnose"+"#"+roomId+"&"+currentOrder,userId+"");
            }else if(key.startsWith("video_diagnose_end_")){
                //更新上线缓存中的当前问诊状态
                redisTemplate.opsForHash().put(onlineKey,"currentStatus",3);
                String roomId=redisTemplate.opsForHash().get(onlineKey,"roomId").toString();
                //更新数据库记录
                HashMap param=new HashMap(){{
                    put("id",currentOrder);
                    put("status",3);
                    put("realEnd",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
                }};
                videoDiagnoseDao.updateStatus(param);
                Map entries=redisTemplate.opsForHash().entries(onlineKey);
                String nextPatient=MapUtil.getStr(entries,"nextPatient");
                String nextOrder=MapUtil.getStr(entries,"nextOrder");
                String nextStart=MapUtil.getStr(entries,"nextStart");
                String nextEnd=MapUtil.getStr(entries,"nextEnd");
                boolean nextPayment=MapUtil.getBool(entries,"nextPayment");
                if(!"none".equals(nextOrder)){
                    //把等候的问诊换成当前问诊
                    entries.replace("currentPatient",nextPatient);
                    entries.replace("currentOrder",nextOrder);
                    entries.replace("currentHandle",false);
                    entries.replace("currentStart",nextStart);
                    entries.replace("currentEnd",nextEnd);
                    entries.replace("currentPayment",nextPayment);
                    entries.replace("currentNotify",false);
                    entries.replace("currentStatus",1);

                    //清除等候问诊的缓存
                    entries.replace("nextPatient","none");
                    entries.replace("nextOrder","none");
                    entries.replace("nextStart",null);
                    entries.replace("nextEnd",null);
                    entries.replace("nextPayment",false);
                    entries.replace("nextNotify",false);
                }else{

                    entries.replace("currentPatient","none");
                    entries.replace("currentOrder","none");
                    entries.replace("currentHandle",false);
                    entries.replace("currentStart",null);
                    entries.replace("currentEnd",null);
                    entries.replace("currentPayment",false);
                    entries.replace("currentNotify",false);
                    entries.replace("currentStatus",null);

                }
                redisTemplate.opsForHash().putAll(onlineKey,entries);
                WebSocketService.sendInfo("EndDiagnose"+"#"+roomId,userId+"");
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    log.error("线程休眠异常",e);
                }
                Credential cred=new Credential(cloudSecretId,cloudSecretKey);
                TrtcClient client=new TrtcClient(cred,"ap-beijing");
                DismissRoomByStrRoomIdRequest request=new DismissRoomByStrRoomIdRequest();
                request.setSdkAppId(trtcAppId);
                request.setRoomId(roomId);
                try{
                    client.DismissRoomByStrRoomId(request);
                }catch (TencentCloudSDKException e){
                    log.error("销毁TRTC房间失败",e);
                }
                redisTemplate.opsForHash().put(onlineKey,"roomId",null);
            }

        }
//        视频问诊挂号缓存被销毁
        else if(key.startsWith("patient_video_diagnose_payment")){
            //获得挂号单id
            int id=Integer.parseInt(key.split("#")[1]);
            //查询付款状态
            HashMap map=videoDiagnoseDao.searchPaymentStatus(id);
            int paymentStatus=MapUtil.getInt(map,"paymentStatus");
            int doctorId=MapUtil.getInt(map,"doctorId");
            key="online_doctor_"+doctorId;
            //只要不是付款成功，就销毁该患者候诊缓存
            if(paymentStatus!=2){
                Map entites=redisTemplate.opsForHash().entries(key);
                //判断就诊缓存是不是当前的挂号单
                if(id==MapUtil.getInt(entites,"nextOrder")){
                    redisTemplate.opsForHash().putAll(key,new HashMap(){{
                        put("nextPatient","none");//就诊患者id
                        put("nextOrder","none");//等候就诊的订单号
                        put("nextStart","none");//等候问诊的开始时间
                        put("nextEnd","none");//等候问诊的结束时间
                        put("nextPayment",false);//等候问诊是否付款
                        put("nextNotify",false);//是否通知了医生端
                    }});
                }
                //关闭视频问诊订单
                videoDiagnoseDao.closePayment(new HashMap(){{
                    put("id",id);
                }});
            }else{
                   int userId=misUserDao.searchUserId(new HashMap(){{
                       put("refId",doctorId);
                       put("job","医生");
                   }});
                   WebSocketService.sendInfo("RefreshDiagnose",userId+"");
            }
        }
        //判断过期时间的数据是否为挂号单
        if(key.startsWith("registration_payment_")){
            //从key中提取挂号单流水号
            String outTradeNo=key.split("_")[2];
            //更新挂号支付订单状态为4
            medicalRegistrationDao.discardPayment(outTradeNo);
            //出诊计划已挂号人数减1
            doctorWorkPlanDao.releaseNumByOutTradeNo(outTradeNo);
            //出诊时段已挂号人数减1
            doctorWorkPlanScheduleDao.releaseNumByOutTradeNo(outTradeNo);
            //查询挂号单的workPlanId和scheduleId
            HashMap map=medicalRegistrationDao.searchWorkPlanIdAndScheduleId(outTradeNo);
            int scheduleId= MapUtil.getInt(map,"doctorScheduleId");
            int workPlanId=MapUtil.getInt(map,"workPlanId");
            key="doctor_schedule_"+scheduleId;
            if(redisTemplate.hasKey(key)){
                //更新缓存中已挂号人数的数量减去1
                redisTemplate.opsForHash().increment(key,"num",-1);
            }
        }
        super.onMessage(message,pattern);
    }
}
