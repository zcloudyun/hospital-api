package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchPrescriptionByRegisrationIdForm;
import com.example.hospital.api.service.DoctorPrescriptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/prescription")
public class DoctorPrescriptionController {
    @Resource
    private DoctorPrescriptionService doctorPrescriptionService;

    @PostMapping
    @SaCheckLogin
    public R searchPrescriptionByRegistrationId(@RequestBody @Valid SearchPrescriptionByRegisrationIdForm form){
        int userId= StpUtil.getLoginIdAsInt();
        form.setUserId(userId);
        Map param= BeanUtil.beanToMap(form);
        HashMap map=doctorPrescriptionService.searchPrescriptionByRegistrationId(param);
        return R.ok().put("result",map);
    }
}
