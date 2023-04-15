package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.db.Entity.request.MedicineDeptManageRequest;
import com.example.hospital.api.db.Entity.vo.MedicalDepartmentManagementVO;
import com.example.hospital.api.service.MedicalDeptService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medical/dept")
public class MedicalDeptController {
    @Resource
    private MedicalDeptService medicalDeptService;

    @GetMapping("/searchAll")
    @SaCheckLogin
    public R searchAll(){
        ArrayList<HashMap> list=medicalDeptService.searchAll();
        return R.ok().put("result",list);
    }

    @GetMapping("/searchDeptAndSub")
    @SaCheckLogin
    public R searchDeptAndSub(){
        HashMap map=medicalDeptService.searchDeptAndSub();
        return R.ok().put("result",map);
    }

    @PostMapping("/searchMedicalDeptList")
    public R searchMedicalDeptList(@RequestBody @Valid SearchMedicalDeptListForm form){
        Map param= BeanUtil.beanToMap(form);
        ArrayList<HashMap> list=medicalDeptService.searchMedicalDeptList(param);
        return R.ok().put("result",list);
    }

    /**
     *
     * @return
     */
    @PostMapping("/getMedicineDepartmentManagement")
    public R getMedicineDepartmentManagement(@RequestBody MedicineDeptManageRequest request) {
        PageUtils page = this.medicalDeptService.getMedicineDepartmentManagement(request);
        return R.ok().put("result",page);
    }
    @PostMapping("/insert")
    @SaCheckLogin
    public R insert(@RequestBody @Valid InsertDeptForm form){
        Map param = BeanUtil.beanToMap(form);
        medicalDeptService.insert(param);
        return R.ok();
    }

    @PostMapping("/updateById")
    @SaCheckLogin
    public R updateById(@RequestBody @Valid UpdateDeptByIdForm form){
        Map param = BeanUtil.beanToMap(form);
        medicalDeptService.updateById(param);
        return R.ok();
    }

}
