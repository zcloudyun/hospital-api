package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchDeptSubScheduleForm;
import com.example.hospital.api.control.form.SearchDoctorByDeptSubIdForm;
import com.example.hospital.api.control.form.SearchScheduleByWorkPlanIdForm;
import com.example.hospital.api.control.form.UpdateDoctorScheduleForm;
import com.example.hospital.api.service.DoctorWorkPlanScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/doctor/work_plan/schedule")
public class DoctorWorkPlanScheduleController {
    @Resource
    private DoctorWorkPlanScheduleService doctorWorkPlanScheduleService;

    @PostMapping("/searchDeptSubSchedule")
    @SaCheckLogin
//    @SaCheckPermission(value = {"ROOT","SCHEDULE:SELECT"},mode = SaMode.OR)
    public R searchDeptSubSchedule(@RequestBody @Valid SearchDeptSubScheduleForm form){
        Map param= BeanUtil.beanToMap(form);
        ArrayList list=doctorWorkPlanScheduleService.searchDeptSubSchedule(param);
        return R.ok().put("result",list);
    }

    @PostMapping("/searchByWorkPlanId")
    @SaCheckLogin
//    @SaCheckPermission(value = {"ROOT","SCHEDULE:SELECT"},mode = SaMode.OR)
    public R SearchByWorkPlanId(@RequestBody @Valid SearchScheduleByWorkPlanIdForm form){
        HashMap map=doctorWorkPlanScheduleService.searchByWorkPlanId(form.getWorkPlanId());
        return R.ok().put("result",map);
    }

    @PostMapping("/updateSchedule")
    @SaCheckLogin
//    @SaCheckPermission(value = {"ROOT","SCHEDULE:UPDATE"},mode = SaMode.OR)
    public R updateSchedule(@RequestBody @Valid UpdateDoctorScheduleForm form){
        Map param=BeanUtil.beanToMap(form);
        doctorWorkPlanScheduleService.updateSchedule(param);
        return R.ok();
    }
}
