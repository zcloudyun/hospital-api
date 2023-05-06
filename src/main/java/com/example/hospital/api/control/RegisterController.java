package com.example.hospital.api.control;

import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.PhoneForm;
import com.example.hospital.api.service.impl.SmsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Resource
    private SmsService smsService;

    @PostMapping("/getCode")
    public R getCode(@RequestBody @Valid PhoneForm form){
        String code = smsService.sendMessage(form.getPhone());
        return R.ok().put("result",code);
    }
}
