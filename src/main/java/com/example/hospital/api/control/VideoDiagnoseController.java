package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.config.tencent.TrtcUtil;
import com.example.hospital.api.control.form.CreateVideoDiagnoseForm;
import com.example.hospital.api.control.form.SearchOnlineDoctorListForm;
import com.example.hospital.api.control.form.UpdateCanRegisterForm;
import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import com.example.hospital.api.service.VideoDiagnoseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
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
}
