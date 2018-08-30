package com.ugia.seckill.exception;


import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
		if(e instanceof GlobalException){
			GlobalException ex = (GlobalException)e;
			return Result.error(ex.getCm());
		}else if(e instanceof BindException) {
			BindException ex = (BindException) e;
			java.util.List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String args = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillAgs(args));
			
		} else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
