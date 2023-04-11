package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.dao.DoctorDao;
import com.example.hospital.api.db.dao.MedicalDeptSubAndDoctorDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.DoctorService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DoctorServiceImpl extends ServiceImpl<DoctorDao, DoctorEntity> implements DoctorService {
   @Resource
   private DoctorDao doctorDao;
    @Resource
    private MedicalDeptSubAndDoctorDao medicalDeptSubAndDoctorDao;
   //重写方法
    @Override
    public PageUtils searchByPage(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=doctorDao.searchCount(param);
        if(count>0){
            list=doctorDao.searchByPage(param);
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
    public HashMap searchContent(int id){
        HashMap map=doctorDao.searchContent(id);
        JSONArray tag= JSONUtil.parseArray(MapUtil.getStr(map,"tag"));
        map.replace("tag",tag);
        return map;
    }

    @Override
    public void insert(Map param){
        //保存医生记录
        DoctorEntity entity= BeanUtil.toBean(param,DoctorEntity.class);
        doctorDao.insert(entity);
        //根据uuid查询医生主键值
        String uuid=entity.getUuid();
        Integer doctorId=doctorDao.searchIdByUuid(uuid);

        //保存医生诊室记录
        int subId=MapUtil.getInt(param,"subId");
        MedicalDeptSubAndDoctorEntity entity_1=new MedicalDeptSubAndDoctorEntity();
        entity_1.setDeptSubId(subId);
        entity_1.setDoctorId(doctorId);
        medicalDeptSubAndDoctorDao.insert(entity_1);

    }
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
    public String updatePhoto(MultipartFile file,Integer doctorId){
      try{
          String fileName="doctor-"+doctorId+".jpg";
          log.info("{}",fileName);
          //在Minio中保存医生照片
          //创建连接
          MinioClient client=new MinioClient.Builder()
                  .endpoint(endpoint).credentials(accessKey,secretKey).build();
          client.putObject(PutObjectArgs.builder().bucket("hospital")
                  .object("doctor/"+fileName)
                  .stream(file.getInputStream(),-1,5*1024*1024)
                  //上传文件的类型
                  .contentType("image/jpeg").build());
          //更新医生表photo字段
          doctorDao.updatePhoto(new HashMap(){{
              put("id",doctorId);
              put("photo","/doctor/"+fileName);
          }});
          return "/doctor/"+fileName;
      }catch(Exception e){
           log.error("保存医生的照片失败",e);
           throw new HospitalException("保存医生的照片失败");
      }

    }

    @Override
    public HashMap searchById(int id){
        HashMap map=doctorDao.searchById(id);
        String tag=MapUtil.getStr(map,"tag");
        JSONArray array=JSONUtil.parseArray(tag);
        map.replace("tag",array);
        return map;
    }

    @Override
    @Transactional
    public void update(Map param){
        doctorDao.update(param);
        //将id改为doctorId
        param=MapUtil.renameKey(param,"id","doctorId");
        medicalDeptSubAndDoctorDao.updateDoctorSubDept(param);
    }

    @Override
    @Transactional
    public void deleteByIds(Integer[] ids){
        doctorDao.deleteByIds(ids);
    }

    @Override
    public ArrayList<HashMap> searchByDeptSubId(int deptSubId){
        ArrayList<HashMap> list=doctorDao.searchByDeptSubId(deptSubId);
        return list;
    }

    @Override
    public HashMap searchDoctorInfoById(int id){
        HashMap map=doctorDao.searchDoctorInfoById(id);
        return map;
    }
}
