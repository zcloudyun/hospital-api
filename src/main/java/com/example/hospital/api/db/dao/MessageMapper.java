package com.example.hospital.api.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.api.db.Entity.Message;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zhang'yun
* @description 针对表【tb_message(消息表)】的数据库操作Mapper
* @createDate 2023-05-05 14:44:00
* @Entity generator.com.example.hospital.api.db.domain.Message
*/
public interface MessageMapper extends BaseMapper<Message> {

    @Select("select * from tb_message where sender_id = #{userId}")
    public List<Message> getUserAllMessage(@Param("userId")Integer userId);


}




