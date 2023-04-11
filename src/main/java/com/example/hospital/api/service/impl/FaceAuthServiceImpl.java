package com.example.hospital.api.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.example.hospital.api.db.Entity.FaceAuthEntity;
import com.example.hospital.api.db.dao.FaceAuthDao;
import com.example.hospital.api.db.dao.UserInfoCardDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.FaceAuthService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FaceAuthServiceImpl implements FaceAuthService {
    @Value("${tencent.cloud.secretId}")
    private String secretId;

    @Value("${tencent.cloud.secretKey}")
    private String secretKey;

    @Value("${tencent.cloud.face.groupName}")
    private String groupName;

    @Value("${tencent.cloud.face.region}")
    private String region;

    @Resource
    private FaceAuthDao faceAuthDao;

    @Resource
    private UserInfoCardDao userInfoCardDao;

    @Override
    public boolean hasFaceAuthInDay(Map param){
        Integer id=faceAuthDao.hasFaceAuthInDay(param);
        boolean bool=(id!=null);
        return bool;
    }

    @Override
    @Transactional
    public void createFaceModel(Map param){
        int userId= MapUtil.getInt(param,"userId");
        String photo=MapUtil.getStr(param,"photo");
        Credential cred=new Credential(secretId,secretKey);
        IaiClient clent=new IaiClient(cred,region);
        //查询用户姓名和性别
        HashMap map=userInfoCardDao.searchUserInfoCard(userId);
        String name=MapUtil.getStr(map,"name");
        String sex=MapUtil.getStr(map,"sex");
        //创建request对象
        CreatePersonRequest req=new CreatePersonRequest();
        req.setGroupId(groupName);//人员库ID
        req.setPersonId(userId + "");//人员id
        long gender=sex.equals("男")? 1L:2L;
        req.setGender(gender);
        req.setQualityControl(2L);//照片质量等级
        req.setUniquePersonControl(4L);//重复人员识别等级
        req.setPersonName(name);//姓名
        req.setImage(photo);//base64图片
        try{
            CreatePersonResponse resp=clent.CreatePerson(req);
            if(StrUtil.isNotBlank(resp.getFaceId())){
                //更新数据表，用户已经录入人脸模型
                userInfoCardDao.updateExistFaceModel(new HashMap(){{
                    put("userId",userId);
                    put("existFaceModel",true);
                }});
            }
        }catch (TencentCloudSDKException e){
            throw new HospitalException(e);
        }
    }

    @Override
    @Transactional
    public boolean verifyFaceModel(int userId,String photo){
        try{
            Credential cred=new Credential(secretId,secretKey);
            IaiClient client=new IaiClient(cred,region);
            //执行人脸识别
            VerifyPersonRequest verifyPersonRequest=new VerifyPersonRequest();
            verifyPersonRequest.setPersonId(userId+"");
            verifyPersonRequest.setImage(photo);
            verifyPersonRequest.setQualityControl(2L);
            VerifyPersonResponse verifyPersonResponse=client.VerifyPerson(verifyPersonRequest);
            boolean bool1=verifyPersonResponse.getIsMatch();
            if(!bool1){
                return bool1;
            }
            //利用腾讯云执行静态活体检测
            DetectLiveFaceRequest detectLiveFaceRequest=new DetectLiveFaceRequest();
            detectLiveFaceRequest.setImage(photo);
            DetectLiveFaceResponse detectLiveFaceResponse=client.DetectLiveFace(detectLiveFaceRequest);
            boolean bool2=detectLiveFaceResponse.getIsLiveness();
            //合并人脸识别和活体识别的结果
            boolean bool=bool1 && bool2;
            //如果身份验证通过，就写入数据库
            if(bool){
                Integer cradId=userInfoCardDao.searchIdByUserId(userId);
                FaceAuthEntity entity=new FaceAuthEntity();
                entity.setPatientCardId(cradId);
                entity.setDate(DateUtil.today());
                faceAuthDao.insert(entity);
            }
            return bool;
        }catch (TencentCloudSDKException e){
          log.error("人脸验证失败",e);
          return false;
        }
    }
}
