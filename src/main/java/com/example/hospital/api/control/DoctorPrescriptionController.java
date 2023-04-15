package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchPrescriptionByRegisrationIdForm;
import com.example.hospital.api.db.dao.MedicalRecordDao;
import com.example.hospital.api.service.DoctorPrescriptionService;
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
@RequestMapping("/prescription")
public class DoctorPrescriptionController {
    @Resource
    private DoctorPrescriptionService doctorPrescriptionService;


    @PostMapping("/searchPrescriptionById")
    @SaCheckLogin
    public R searchPrescriptionById(@RequestBody @Valid SearchPrescriptionByRegisrationIdForm form){
        int userId= StpUtil.getLoginIdAsInt();
        form.setUserId(userId);
        Map param= BeanUtil.beanToMap(form);
        HashMap map=doctorPrescriptionService.searchPrescriptionById(param);
        return R.ok().put("result",map);
    }
}
