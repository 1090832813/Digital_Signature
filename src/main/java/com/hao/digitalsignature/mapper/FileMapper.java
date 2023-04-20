package com.hao.digitalsignature.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.digitalsignature.entity.Files;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface FileMapper extends BaseMapper<Files> {

//    @Select("select * from picture where picture_user = #{picture_user}")
//    List<Files> selectByUid(Integer picture_user);
}
