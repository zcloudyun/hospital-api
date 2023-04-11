package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Resource
    private MedicineService medicineService;

    @PostMapping("/searchByPage")
    @SaCheckLogin
    //用户必须具备相关的权限，权限之间是OR的关系,管理员权限或者是医生权限
//    @SaCheckPermission(value={"ROOT","DOCTOR:SELECT"},mode= SaMode.OR)
    public R SearchByPage(@RequestBody @Valid SearchMedicineByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = medicineService.searchByPage(param);
        log.info("{}，分发 {}", param, pageUtils);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/insert")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:INSERT"},mode=SaMode.OR)
    public R insert(@RequestBody @Valid InsertMedicineForm form) {
        log.info("接收到的参数:{}", form);
        Map param = BeanUtil.beanToMap(form);
        medicineService.insert(param);
        return R.ok();
    }

    @PostMapping("/update")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:UPDATE"},mode=SaMode.OR)
    public R update(@RequestBody @Valid UpdateMedicineForm form) {
        Map param = BeanUtil.beanToMap(form);
        medicineService.update(param);
        return R.ok();
    }

    @GetMapping("/searchAll")
    @SaCheckLogin
    public R searchAll(){
       ArrayList<HashMap>list= medicineService.searchAll();
       return R.ok().put("result",list);

    }

}


