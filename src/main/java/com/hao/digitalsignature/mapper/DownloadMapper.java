package com.hao.digitalsignature.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.digitalsignature.entity.Download;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DownloadMapper extends BaseMapper<Download> {
    @Select("select * from downpicture where BINARY picture_realname=#{picture_realname}")
    Download find(String id);
}
