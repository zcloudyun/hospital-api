package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchOnlineDoctorListForm;
import com.example.hospital.api.control.form.SearchisOnlineForm;
import com.example.hospital.api.service.OnlineService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/isOnline")
public class OnlineController {
    @Resource
    private OnlineService onlineService;
    @GetMapping("/online")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode = SaMode.OR)
    public R online(){
        int userId= StpUtil.getLoginIdAsInt();
        onlineService.online(userId);
        return R.ok();
    }

    @GetMapping("/offline")
    @SaCheckLogin
//    @SaCheckPermission(value = {"VIDEO_DIAGNOSE:DIAGNOSE"},mode = SaMode.OR)
    public R offline(){
        int userId=StpUtil.getLoginIdAsInt();
        boolean bool=onlineService.offline(userId);
        return R.ok().put("result",bool);
    }

    @PostMapping("/searchOnlineDoctorList")
    @SaCheckLogin
    public R searchOnlineDoctorList(@RequestBody @Valid SearchOnlineDoctorListForm form){
        ArrayList<HashMap> list=onlineService.searchOnlineDoctorList(form.getSubName(),form.getJob());
        return R.ok().put("result",list);
    }

    @PostMapping("/searchisOnline")
    @SaCheckLogin
    public R searchIsOnline(@RequestBody @Valid SearchisOnlineForm form){
        Boolean bool=onlineService.searchisOnline(form.getDoctorId());
        return R.ok().put("result",bool);
    }
}
