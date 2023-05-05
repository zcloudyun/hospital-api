package com.example.hospital.api.control;

import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.DeleteRateImageForm;
import com.example.hospital.api.control.form.DeleteVideoDiagnoseImageForm;
import com.example.hospital.api.control.form.SearchImageByRegistrationIdForm;
import com.example.hospital.api.control.form.SearchImageByVideoDiagnoseIdForm;
import com.example.hospital.api.service.RateFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rate_file")
public class RateFileController {
    @Resource
    private RateFileService rateFileService;

    @PostMapping("/uploadImg")
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam("registrationId") Integer registrationId) throws Exception {
        String list= rateFileService.uploadImg(file, registrationId);
        return R.ok().put("result",list);
    }

    @PostMapping("/searchImageByRegistrationId")
//    @SaCheckLogin
    public R searchImageByRegistrationId(@RequestBody @Valid SearchImageByRegistrationIdForm form){
        ArrayList<HashMap> list=rateFileService.searchImageByRegistrationId(form.getRegistrationId());
        return R.ok().put("result",list);
    }

    @PostMapping("/deleteImage")
//    @SaCheckLogin
    public R deleteImage(@RequestBody @Valid DeleteRateImageForm form){
        Map param= BeanUtil.beanToMap(form);
        rateFileService.deleteImage(param);
        return R.ok();
    }
}
