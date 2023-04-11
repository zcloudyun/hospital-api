package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchDoctorByPageForm;
import com.example.hospital.api.control.form.SearchDoctorPriceByPageForm;
import com.example.hospital.api.control.form.UpdatePriceByIdForm;
import com.example.hospital.api.service.DoctorPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/doctor/price")
public class DoctorPriceController {
    @Resource
    private DoctorPriceService doctorPriceService;

    @PostMapping("/searchByPagePrice")
    //验证用户是否已经登录，如果登录就能执行下面的方法，如果没有登录就不能执行该方法
    @SaCheckLogin
    //用户必须具备相关的权限，权限之间是OR的关系,管理员权限或者是医生权限
//    @SaCheckPermission(value={"ROOT","DOCTOR:SELECT"},mode= SaMode.OR)
    public R searchByPagePrice(@RequestBody @Valid SearchDoctorPriceByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = doctorPriceService.searchByPagePrice(param);
        log.info("{}，分发 {}", param, pageUtils);
        return R.ok().put("result", pageUtils);

    }

    @PostMapping("/updatePriceById")
    @SaCheckLogin
    public R updatePriceById(@RequestBody @Valid UpdatePriceByIdForm form){
        Map param = BeanUtil.beanToMap(form);
        doctorPriceService.updatePriceById(param);
        return R.ok();
    }
}
