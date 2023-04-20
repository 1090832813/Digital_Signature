package com.hao.digitalsignature.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.digitalsignature.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper  extends BaseMapper<User> {
// extend BaseMapper 由plus提供，不需要自己写接口
//    @Select("select * from user")
//    public List<User> getAll();
//
//    @Insert("insert into user values(#{user_id},#{user_name},#{password})")
//    public int insert(User user);
//
//    @Delete("delete from user where id=#{id}")
//    int delete(int id);
//
//    @Update("update user set username=#{user_name} where id=#{user_id}")
//    int update(User user);
//
//    @Select("select * from user where id=#{user_id}")
//    User find(int id);

    @Select("select * from user")
    @Results(
            {       //前面是表里字段，后面是类里的字段
                    @Result(column = "id",property = "id"),
                    @Result(column = "username",property = "username"),
                    @Result(column = "id",property = "files",javaType = List.class,
                            many = @Many(select = "com.hao.digitalsignature.mapper.FileMapper.selectByUid")
                    )
            }
    )
    List<User> selectAllUserAndFiles();
}
