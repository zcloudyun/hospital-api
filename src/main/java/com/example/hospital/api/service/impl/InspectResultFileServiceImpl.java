package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.db.dao.InspectResultFileDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.InspectResultFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class InspectResultFileServiceImpl implements InspectResultFileService {
    @Resource
    private InspectResultFileDao inspectResultFileDao;

    //存放的是minio服务器的地址
    @Value("${minio.endpoint}")
    private String endpoint;

    //minio的账户和密码
    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Override
    //事务的注解
    @Transactional
    public HashMap uploadImg(MultipartFile file, int recordId){
        HashMap map = new HashMap();
        try{
            //随机生成文件名
            String filename= IdUtil.simpleUUID()+".jpg";
            String path="patient-wx/inspect/"+filename;
            //在Minio中保存医生照片
            //创建连接
            MinioClient client=new MinioClient.Builder()
                    .endpoint(endpoint).credentials(accessKey,secretKey).build();
            client.putObject(PutObjectArgs.builder().bucket("hospital")
                    .object(path)
                    .stream(file.getInputStream(),-1,5*1024*1024)
                    //上传文件的类型
                    .contentType("image/jpeg").build());
            map.put("recordId",recordId);
            map.put("filename",filename);
            map.put("path",path);
            inspectResultFileDao.uploadImg(map);
            return map;
        }catch(Exception e) {
            throw new HospitalException("保存就诊材料失败");
        }
    }

    @Override
    public ArrayList<HashMap> searchImageByRecordId(int recordId){
        ArrayList<HashMap> list=inspectResultFileDao.searchImageByRecordId(recordId);
        return list;
    }

    @Override
    public void deleteImage(Map param){
        int recordId= MapUtil.getInt(param,"recordId");
        //判断是否包含filename参数
        String filename=MapUtil.getStr(param,"filename");
        //删除某张照片
        if(filename!=null){
            this.removeImageFile(filename);
        }
        //删除该视频问诊所有照片
        else{
            //查询该视频所有照片
            ArrayList<String> list = inspectResultFileDao.searchfilenameByRecordId(recordId);
            list.forEach(one->{
                this.removeImageFile(one);
            });
        }
        //删除数据表记录
        inspectResultFileDao.delete(param);
    }

    private void removeImageFile(String filename){
        try{
            String path="patient-wx/inspect/"+filename;
            //删除minio文件
            MinioClient client=new MinioClient.Builder()
                    .endpoint(endpoint).credentials(accessKey,secretKey).build();
            client.removeObject(RemoveObjectArgs.builder().bucket("hospital").object(path).build());
        }catch (Exception e){
            throw new HospitalException("删除视频问诊图片失败");
        }
    }
}
