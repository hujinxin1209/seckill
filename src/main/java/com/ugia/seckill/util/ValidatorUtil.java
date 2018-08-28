package com.ugia.seckill.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;


public class ValidatorUtil {
	
	private static final Pattern MOBILE_PATTERN_PATTERN = Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher matcher = MOBILE_PATTERN_PATTERN.matcher(src);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isMobile("334232423d"));
	}
}
