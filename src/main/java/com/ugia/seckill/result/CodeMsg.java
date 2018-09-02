package com.ugia.seckill.result;

public class CodeMsg {
	private int code;
	private String msg;
	
	// 通用异常
	public static CodeMsg SUCESS = new CodeMsg(0, "sucess");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常: %s");
	// 登录模块
	public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已失效");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号码格式错误");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
	public static CodeMsg PASSWORD_WRONG = new CodeMsg(500215, "密码错误");
	// 秒杀模块
	public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
	public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
	
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public CodeMsg fillAgs(Object...objects ) {
		int code = this.code;
		String message = String.format(this.msg, objects);
		return new CodeMsg(code, message);
	}
}
