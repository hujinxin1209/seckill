package com.ugia.seckill.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Required;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.util.ValidatorUtil;


public class IsMobileValidator implements ConstraintValidator<IsMobile, String>{

	private boolean required = false;
	
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			} else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
