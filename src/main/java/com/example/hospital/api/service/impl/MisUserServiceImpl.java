package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.Entity.MisUserEntity;
import com.example.hospital.api.db.dao.MisUserDao;
import com.example.hospital.api.service.MisUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public PageUtils searchUserByPages(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=misUserDao.searchCount(param);
        if(count>0){
            list=misUserDao.searchUserByPages(param);
        }else{
            list=new ArrayList<>();
        }
        //获取当前页数
        int page= MapUtil.getInt(param,"page");
        //获取每页记录数
        int length=MapUtil.getInt(param,"length");
        PageUtils pageUtils=new PageUtils(list,count,page,length);
        return pageUtils;
    }

    @Override
    public void insertUser(Map param){
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
        param.replace("password",password);
        //保存医生记录
        MisUserEntity entity= BeanUtil.toBean(param,MisUserEntity.class);
        misUserDao.insertUser(entity);
    }

    @Override
    public void updateUser(Map param){
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
        param.replace("password",password);
        misUserDao.updateUser(param);
    }

    @Override
    @Transactional
    public void deleteUser(Integer[] ids){
        misUserDao.deleteUser(ids);
    }

    @Override
    public MisUserEntity getUserEntityById(Integer userId) {
        return this.misUserDao.selectDoctorUserById(userId);
    }
}
