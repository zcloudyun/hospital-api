package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.example.hospital.api.db.dao.MisUserDao;
import com.example.hospital.api.service.MisUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class MisUserServiceImpl implements MisUserService {

    @Resource
    private MisUserDao misUserDao;
    @Override
    public Integer login(Map param) {
    //从Map里面获取字符串
        String username= MapUtil.getStr(param,"username");
        String password=MapUtil.getStr(param,"password");
        MD5 md5=MD5.create();
        //进行哈希加密
        String temp=md5.digestHex(username);
        //前6位字符
        String tempStart= StrUtil.subWithLength(temp,0,6);
        //后三位字符
        String tempEnd=StrUtil.subSuf(temp,temp.length()-3);
        //混淆原始密码并哈希加密
        password=md5.digestHex(tempStart+password+tempEnd);
        log.info("混淆之后的密码{}",password);
        param.replace("password",password);
        Integer userId=misUserDao.login(param);
        log.info("id为{}",userId);
        return userId;

    }
}
