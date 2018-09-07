package com.ugia.seckill.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.dao.MiaoshaUserDao;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.exception.GlobalException;
import com.ugia.seckill.redis.MiaoshaUserKey;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.util.MD5Util;
import com.ugia.seckill.util.UUIDUtil;
import com.ugia.seckill.vo.LoginVo;

@Service
public class MiaoshaUserService {
	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;

	@Autowired
	RedisService redisService;

	public MiaoshaUser getById(long id) {
		// 取缓存
		MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, 
				MiaoshaUser.class);
		if(user != null) {
			return user;
		}
		user = miaoshaUserDao.getById(id);
		if(user != null) {
			redisService.set(MiaoshaUserKey.getById, ""+id, user);
		}
		return user;
	}
	
	public boolean updatePassword(String token, long id, String password) {
		MiaoshaUser user = getById(id);
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		MiaoshaUser toBeUpdate = new MiaoshaUser();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
		miaoshaUserDao.update(toBeUpdate);
		
		// 处理缓存
		redisService.delete(MiaoshaUserKey.getById, ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(MiaoshaUserKey.token, token, user);
		return true;
	}

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}

		String mobile = loginVo.getMobile();
		String formPassword = loginVo.getPassword();

		MiaoshaUser user = getById(Long.parseLong(mobile));
		if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}

		// 验证密码
		String dbPassword = user.getPassword();
		String saltDB = user.getSalt();
		String calcPas = MD5Util.formPassToDBPass(formPassword, saltDB);
		if (!calcPas.equals(dbPassword)) {
			throw new GlobalException(CodeMsg.PASSWORD_WRONG);
		}
		// 生成cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return true;
	}

	private void addCookie(HttpServletResponse response, String token,  MiaoshaUser user) {
		
		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		// 延长token有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
}
