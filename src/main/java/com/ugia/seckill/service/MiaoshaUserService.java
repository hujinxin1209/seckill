package com.ugia.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ugia.seckill.dao.MiaoshaUserDao;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.util.MD5Util;
import com.ugia.seckill.vo.LoginVo;

@Service
public class MiaoshaUserService {
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}

	public CodeMsg login(LoginVo loginVo) {
		if(loginVo == null) {
			return CodeMsg.SERVER_ERROR;
		}
		
		String mobile = loginVo.getMobile();
		String formPassword = loginVo.getPassword();
		
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			return CodeMsg.MOBILE_NOT_EXIST;
		}
		
		// 验证密码
		String dbPassword = user.getPassword();
		String saltDB = user.getSalt();
		String calcPas = MD5Util.formPassToDBPass(formPassword, saltDB);
		if(!calcPas.equals(dbPassword)) {
			return CodeMsg.PASSWORD_WRONG;
		}
		return CodeMsg.SUCESS;
	}
}
