package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.config.tencent.TrtcUtil;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import com.example.hospital.api.service.VideoDiagnoseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

//获取TRTC签名
@RestController
@RequestMapping("/video_diagnose")
@Slf4j
public class VideoDiagnoseController {
    @Value("${tencent.trtc.appId}")
    private int appId;

    @Resource
    private TrtcUtil trtcUtil;

    @Resource
    private VideoDiagnoseService videoDiagnoseService;

    @GetMapping("/searchMyUserSig")
    @SaCheckLogin
    public R searchMyUserSig(){
//        获取医生的ID
        int userId= StpUtil.getLoginIdAsInt();
        String userSig=trtcUtil.genUserSig(userId+"");
        return R.ok().put("userSig",userSig).put("userId",userId).put("appId",appId);
    }

    @GetMapping("/online")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode = SaMode.OR)
    public R online(){
        int userId=StpUtil.getLoginIdAsInt();
        videoDiagnoseService.online(userId);
        return R.ok();
    }

    @GetMapping("/offline")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode = SaMode.OR)
    public R offline(){
        int userId=StpUtil.getLoginIdAsInt();
        boolean bool=videoDiagnoseService.offline(userId);
        return R.ok().put("result",bool);
    }

    @PostMapping("/updateOpenFlag")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode = SaMode.OR)
    public R updateOpenFlag(@RequestBody @Valid UpdateCanRegisterForm form){
        int userId=StpUtil.getLoginIdAsInt();
        videoDiagnoseService.updateOpenFlag(userId,form.getOpen());
        return R.ok();
    }

    @PostMapping("/searchOnlineDoctorList")
    @SaCheckLogin
    public R searchOnlineDoctorList(@RequestBody @Valid SearchOnlineDoctorListForm form){
        ArrayList<HashMap> list=videoDiagnoseService.searchOnlineDoctorList(form.getSubName(),form.getJob());
        return R.ok().put("result",list);
    }
    @PostMapping("/createVideoDiagnose")
    public R createVideoDiagnose(@RequestBody @Valid CreateVideoDiagnoseForm form){
        int userId=StpUtil.getLoginIdAsInt();
        VideoDiagnoseEntity entity= BeanUtil.toBean(form,VideoDiagnoseEntity.class);
        HashMap result=videoDiagnoseService.createVideoDiagnose(userId,entity);
        return R.ok().put("result",result);
    }

    @GetMapping("/refreshInfo")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNSE"},mode = SaMode.OR)
    public R refreshInfo(){
        int userId= StpUtil.getLoginIdAsInt();
        HashMap map=videoDiagnoseService.refreshInfo(userId);
        return R.ok().put("result",map);
    }

    @GetMapping("/searchVideoDiagnoseInfo")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode=SaMode.OR)
    public R searchVideoDiagnoseInfo(){
        int userId=StpUtil.getLoginIdAsInt();
        HashMap map=videoDiagnoseService.searchVideoDiagnoseInfo(userId);
        return R.ok().put("result",map);
    }

    @SneakyThrows
    @PostMapping("/transactionCallback")
    public R transactionCallback(@RequestBody @Valid SearchPaymentResultForm form){
        //更新挂号记录的付款状态和付款单Id
        //创建支付单ID
        String transactionId= IdUtil.simpleUUID().toUpperCase();
        Map param=BeanUtil.beanToMap(form);
        String outTradeNo=MapUtil.getStr(param,"outTradeNo");
        //更新挂号记录的付款状态和付款账单ID
        boolean bool= videoDiagnoseService.updatePayment(new HashMap(){{
            put("outTradeNo",outTradeNo);
            put("transactionId",transactionId);
            put("paymentStatus",2);
        }});
       return R.ok().put("result",bool);
    }
    @PostMapping("/searchPaymentResult")
    public R searchPaymentResult(@RequestParam("outTradeNo") String outTradeNo){
       boolean bool=videoDiagnoseService.searchPaymentResult(outTradeNo);
       return  R.ok().put("result",bool);
    }
    @PostMapping("/uploadImg")
    public R upload(@RequestParam("file") MultipartFile file,@RequestParam("videoDiagnoseId") Integer videoDiagnoseId) throws Exception {
        String list= videoDiagnoseService.uploadImg(file, videoDiagnoseId);
        return R.ok().put("result",list);
    }

    @PostMapping("/searchImageByVideoDiagnoseId")
//    @SaCheckLogin
    public R searchImageByVideoDiagnoseId(@RequestBody @Valid SearchImageByVideoDiagnoseIdForm form){
        ArrayList<HashMap> list=videoDiagnoseService.searchImageByVideoDiagnoseId(form.getVideoDiagnoseId());
        return R.ok().put("result",list);
    }

    @PostMapping("/deleteImage")
//    @SaCheckLogin
    public R deleteImage(@RequestBody @Valid DeleteVideoDiagnoseImageForm form){
       Map param=BeanUtil.beanToMap(form);
       videoDiagnoseService.deleteImage(param);
       return R.ok();
    }

    @GetMapping("/searchUserSig")
//    @SaCheckLogin
    public R searchUserSig(){
        int userId=StpUtil.getLoginIdAsInt();
        String userSign=trtcUtil.genUserSig(userId+"");
        return R.ok().put("userSig",userSign).put("userId",userId).put("appId",appId);
    }

    @PostMapping("/searchRoomId")
//    @SaCheckLogin
    public R searchRoomId(@RequestBody @Valid SearchRoomIdForm form){
        String roomId=videoDiagnoseService.searchRoomId(form.getDoctorId());
        return R.ok().put("result",roomId);
    }

    @PostMapping("/searchimgByVideoDiagnoseId")
//    @SaCheckLogin
    private R searchimgByVideoDiagnoseId(@RequestBody @Valid SearchImageByVideoDiagnoseIdForm form){
        ArrayList<String> list=videoDiagnoseService.searchImgByVideoDiagnoseId(form.getVideoDiagnoseId());
        return R.ok().put("result",list);
    }
}
