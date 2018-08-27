package com.ugia.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ugia.seckill.domain.User;

@Mapper
public interface UserDao {
	@Select("select * from user where id=#{id}")
	public User getById(int id); 
	
	@Insert("insert into user(id, name)values(#{id},#{name})")
	public int insert(User user);
}
