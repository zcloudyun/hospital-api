package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.GetDeptSubDoctorsRequest;
import com.example.hospital.api.control.form.InsertDeptSubForm;
import com.example.hospital.api.control.form.SearchMedicalDeptSubListForm;
import com.example.hospital.api.control.form.UpdateDeptSubByIdForm;
import com.example.hospital.api.service.MedicalDeptService;
import com.example.hospital.api.service.MedicalDeptSubService;
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

@Slf4j
@RestController
@RequestMapping("/medical/dept/sub")
public class MedicalDeptSubController {
    @Resource
    private MedicalDeptSubService medicalDeptSubService;

    @PostMapping("/searchMedicalDeptSubList")
    @SaCheckLogin
    public R searchMedicalDeptSubList(@RequestBody @Valid SearchMedicalDeptSubListForm form){
        ArrayList<HashMap> list=medicalDeptSubService.searchMedicalDeptSubList(form.getDeptId());
        return R.ok().put("result",list);
    }

    @PostMapping("/getMedicalDeptSubDoctors")
    public R searchMedicalDeptSubDoctorList(@RequestBody GetDeptSubDoctorsRequest request) {
        log.info("查询诊室下医生信息：{}", request);
        PageUtils page= this.medicalDeptSubService.searchMedicalDeptSubDoctorList(request);
        return R.ok().put("result",page);
    }

    @PostMapping("/insert")
    @SaCheckLogin
    public R insert(@RequestBody @Valid InsertDeptSubForm form){
        Map param = BeanUtil.beanToMap(form);
        medicalDeptSubService.insert(param);
        return R.ok();
    }

    @PostMapping("/updateById")
    @SaCheckLogin
    public R updateById(@RequestBody @Valid UpdateDeptSubByIdForm form){
        Map param = BeanUtil.beanToMap(form);
        medicalDeptSubService.updateById(param);
        return R.ok();
    }

}
