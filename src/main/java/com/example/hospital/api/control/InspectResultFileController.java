package com.example.hospital.api.control;

import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.DeleteInspectImageForm;
import com.example.hospital.api.control.form.DeleteVideoDiagnoseImageForm;
import com.example.hospital.api.control.form.SearchImageByRecordIdForm;
import com.example.hospital.api.control.form.SearchImageByVideoDiagnoseIdForm;
import com.example.hospital.api.service.InspectResultFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/insepct_result_file")
public class InspectResultFileController {
    @Resource
    private InspectResultFileService inspectResultFileService;
    @PostMapping("/uploadImg")
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam("recordId") int recordId) throws Exception {
        HashMap map = inspectResultFileService.uploadImg(file, recordId);
        return R.ok().put("result",map);
    }
    @PostMapping("/searchImageByRecordId")
//    @SaCheckLogin
    public R searchImageByVideoDiagnoseId(@RequestBody @Valid SearchImageByRecordIdForm form){
        ArrayList<HashMap> list=inspectResultFileService.searchImageByRecordId(form.getRecordId());
        return R.ok().put("result",list);
    }
    @PostMapping("/deleteImage")
//    @SaCheckLogin
    public R deleteImage(@RequestBody @Valid DeleteInspectImageForm form){
        Map param= BeanUtil.beanToMap(form);
        inspectResultFileService.deleteImage(param);
        return R.ok();
    }
}
