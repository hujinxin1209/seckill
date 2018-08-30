package com.ugia.seckill.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.service.MiaoshaUserService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver{
	
	@Autowired
	MiaoshaUserService userService;

	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		HttpServletResponse response = arg2.getNativeResponse(HttpServletResponse.class);
		HttpServletRequest request = arg2.getNativeRequest(HttpServletRequest.class);
		
		String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieNameToken)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazzClass = parameter.getParameterType();
		
		return clazzClass == MiaoshaUser.class;
	}

}
