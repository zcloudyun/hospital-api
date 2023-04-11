package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.GetDeptSubDoctorsRequest;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;
import com.example.hospital.api.db.Entity.vo.MedicalDeptSubManageVO;
import com.example.hospital.api.db.dao.MedicalDeptSubDao;
import com.example.hospital.api.service.DoctorService;
import com.example.hospital.api.service.MedicalDeptService;
import com.example.hospital.api.service.MedicalDeptSubAndDoctorService;
import com.example.hospital.api.service.MedicalDeptSubService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalDeptSubServiceImpl extends ServiceImpl<MedicalDeptSubDao, MedicalDeptSubEntity>
        implements MedicalDeptSubService {
      @Resource
      private MedicalDeptSubDao medicalDeptSubDao;
      @Resource
      private MedicalDeptSubAndDoctorService medicalDeptSubAndDoctorService;
      @Resource
      private DoctorService doctorService;
      @Resource
      private MedicalDeptService medicalDeptService;


      @Override
      public ArrayList<HashMap> searchMedicalDeptSubList(int deptId){
          ArrayList<HashMap> list=medicalDeptSubDao.searchMedicalDeptSubList(deptId);
          return list;
      }

      @Override
      public PageUtils searchByPages(Map param){
          ArrayList<HashMap> list=null;
          //查询的记录总数
          long count=medicalDeptSubDao.searchCount(param);
          if(count>0){
              list=medicalDeptSubDao.searchByPages(param);
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

    /**
     * 查询科室下诊室
     * @param deptId
     * @return
     */
    @Override
    public List<MedicalDeptSubEntity> getMedicalDeptSubListByDeptId(Integer deptId) {

        List<MedicalDeptSubEntity> list = this.lambdaQuery()
                .eq(MedicalDeptSubEntity::getDeptId, deptId)
                .list();
        return list;
    }

    /**
     * 获取诊室下医生
     * @param request
     * @return
     */
    @Override
    public R searchMedicalDeptSubDoctorList(GetDeptSubDoctorsRequest request) {

        // 参数校验
        if (Objects.isNull(request)) {
            // 校验失败
            request = new GetDeptSubDoctorsRequest();
        }

        // 响应对象
        List<MedicalDeptSubManageVO> voList = new ArrayList<>();
        // 分页查询
        Page<MedicalDeptSubEntity> page = this.lambdaQuery()
                .eq(Objects.nonNull(request.getDeptId()), MedicalDeptSubEntity::getDeptId, request.getDeptId())
                .eq(StringUtils.isNotEmpty(request.getDeptSubName()), MedicalDeptSubEntity::getName, request.getDeptSubName())
                .page(Page.of(request.getStart(), request.getLength()));

        List<MedicalDeptSubEntity> records = page.getRecords();
        voList = records.stream().map(subDept -> {
            // 查询诊室下医生映射
            List<MedicalDeptSubAndDoctorEntity> doctorEntityList = this.medicalDeptSubAndDoctorService.getSubDeptDoctorList(subDept.getId());
            // 查询医生
            List<Integer> doctorIds = doctorEntityList.stream().map(MedicalDeptSubAndDoctorEntity::getDoctorId).collect(Collectors.toList());
            MedicalDeptSubManageVO vo = new MedicalDeptSubManageVO();

            if (CollectionUtils.isNotEmpty(doctorIds)) {
                List<DoctorEntity> doctorList = this.doctorService.listByIds(doctorIds);

                // 计算普通医生
                long simpleDoctorCount = doctorList.stream().filter(doctor -> doctor.getJob().contains("主治")).count();
                vo.setSimpleDoctorListCount(simpleDoctorCount)
                        .setDoctorCount(doctorList.size())
                        .setProfessionalDoctorListCount(doctorList.size() - simpleDoctorCount);
            } else {
                vo.setDoctorCount(0).setSimpleDoctorListCount(0L).setProfessionalDoctorListCount(0L);
            }
            // 查询科室
            MedicalDeptEntity dept = this.medicalDeptService.getById(subDept.getDeptId());
            vo.setDeptEntity(dept);
            vo.setDeptSubEntity(subDept);
            return vo;
        }).collect(Collectors.toList());
        return R.data(voList);
    }

    @Override
    public void insert(Map param){
        MedicalDeptSubEntity entity= BeanUtil.toBean(param,MedicalDeptSubEntity.class);
        medicalDeptSubDao.insert(entity);
    }

    @Override
    public void updateById(Map param){

        medicalDeptSubDao.updateById(param);
    }


}
