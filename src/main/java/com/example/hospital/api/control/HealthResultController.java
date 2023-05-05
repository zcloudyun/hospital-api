package com.example.hospital.api.control;

import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchHealthResultForm;
import com.example.hospital.api.service.HealthResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health_result")
public class HealthResultController {
    @Resource
    private HealthResultService healthResultService;

    @PostMapping("/searchByType")
    public R searchByType(@RequestBody @Valid SearchHealthResultForm form){
        Map param = BeanUtil.beanToMap(form);
        HashMap map = healthResultService.searchByType(param);
        return R.ok().put("result",map);
    }
}
