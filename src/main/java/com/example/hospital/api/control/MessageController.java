package com.example.hospital.api.control;


import cn.dev33.satoken.stp.StpUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 获取患者会话医生列表
     * @param userId 患者id
     * @return
     */
    @GetMapping("/talk/list")
    public R getTalkDoctorList() {
        Integer userId= StpUtil.getLoginIdAsInt();
        List<DoctorEntity> doctorList = this.messageService.searchDoctorList(userId);

        return R.ok().put("talkList", doctorList);
    }

}
