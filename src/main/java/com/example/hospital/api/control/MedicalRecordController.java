package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.db.Entity.PrescriptionEntity;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import com.example.hospital.api.service.MedicalRecordService;
import com.example.hospital.api.service.UserInfoCardService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medical/record")
public class MedicalRecordController {
    @Resource
    private MedicalRecordService medicalRecordService;

    @Resource
    private UserInfoCardService userInfoCardService;

    @PostMapping("/insert")
    @SaCheckLogin
    public R searchId(@RequestBody @Valid InsertMedicalRecordForm form){
        Map param = BeanUtil.beanToMap(form);
        medicalRecordService.insert(param);
        return R.ok();
    }

    @PostMapping("/searchAll")
    @SaCheckLogin
    public R searchAll(@RequestBody @Valid SearchMedicalRecordAllForm form){
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = medicalRecordService.searchAll(param);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/searchPrescription")
    @SaCheckLogin
    public R searchPrescription(@RequestBody @Valid SearchPrescriptionForm form){
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = medicalRecordService.searchPrescription(param);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/deleteByIds")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:DELETE"},mode = SaMode.OR)
    public R deleteByIds(@RequestBody @Valid DeleteMedicalRecordForm form) {
        medicalRecordService.deleteByIds(form.getIds());
        return R.ok();
    }

    @PostMapping("/deleteIds")
    @SaCheckLogin
    public R deleteIds(@RequestBody @Valid DeleteMedicalRecordForm form){
        medicalRecordService.deleteIds(form.getIds());
        return R.ok();
    }

    @PostMapping("/searchRecordAll")
    public R searchRecordAll(@RequestBody @Valid RequestByUserIdForm form){
        int userId=StpUtil.getLoginIdAsInt();
        ArrayList<HashMap> list = medicalRecordService.searchByUserIdRecord(userId);
        return R.ok().put("result",list);
    }

    @PostMapping("/searchByRecordId")
    public R searchByRecordId(@RequestBody @Valid RequestRecordIdForm form){
        Integer recordId = form.getRecordId();
        HashMap map = medicalRecordService.searchByRecordId(recordId);
        return R.ok().put("result",map);
    }

    @PostMapping("/searchMedicine")
    public R searchMedicine(@RequestBody @Valid SearchMedicineForm form){
        List<PrescriptionEntity> map = medicalRecordService.searchMedicine(form.getPatientId());
        return R.ok().put("result",map);
    }
}
