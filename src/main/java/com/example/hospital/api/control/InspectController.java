package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.service.InspectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inspect")
public class InspectController {
    @Resource
    private InspectService inspectService;
    @GetMapping("/searchAll")
    public R searchAll(){
        ArrayList<HashMap> list = inspectService.searchAll();
        return R.ok().put("result",list);
    }

    @PostMapping("/searchByPage")
    public R searchByPage(@RequestBody @Valid SearchInspectByPageForm form){
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = inspectService.searchByPage(param);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/insert")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:INSERT"},mode=SaMode.OR)
    public R insert(@RequestBody @Valid InsertInspectForm form) {
        Map param = BeanUtil.beanToMap(form);
        inspectService.insert(param);
        return R.ok();
    }

    @PostMapping("/update")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:UPDATE"},mode=SaMode.OR)
    public R update(@RequestBody @Valid UpdateInspectForm form) {
        Map param = BeanUtil.beanToMap(form);
        inspectService.update(param);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:DELETE"},mode = SaMode.OR)
    public R deleteByIds(@RequestBody @Valid DeleteByIdForm form) {
        inspectService.deleteByIds(form.getIds());
        return R.ok();
    }
}
