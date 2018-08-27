package com.ugia.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ugia.seckill.dao.UserDao;
import com.ugia.seckill.domain.User;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	public User getById(int id) {
		return userDao.getById(id);
	}
	
	@Transactional
	public boolean tx() {
		User user = new User(2, "sss");
		userDao.insert(user);
		
		User user1 = new User(3, "dd");
		userDao.insert(user1);
		return true;
	}
}
