package com.ugia.seckill.redis;

public class Miaoshakey extends BasePrefix{
	private Miaoshakey(String prefix) {
		super(prefix);
	}
	
	public static Miaoshakey isGoodsOver = new Miaoshakey("go");
	
}
