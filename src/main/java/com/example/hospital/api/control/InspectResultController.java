package com.example.hospital.api.control;

import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertInspectResultForm;
import com.example.hospital.api.service.InspectResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/inspect_result")
public class InspectResultController {
    @Resource
    private InspectResultService inspectResultService;

    @PostMapping("/insert")
    public R insert(@RequestBody @Valid InsertInspectResultForm form){
       Map param= BeanUtil.beanToMap(form);
       inspectResultService.insert(param);
       return R.ok();
    }
}
