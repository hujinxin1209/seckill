package com.ugia.seckill.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ugia.seckill.domain.MiaoshaOrder;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.OrderInfo;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.service.GoodsService;
import com.ugia.seckill.service.MiaoshaService;
import com.ugia.seckill.service.MiaoshaUserService;
import com.ugia.seckill.service.OrderService;
import com.ugia.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
	@RequestMapping("/do_miaosha")
	public String list(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		
		if(user == null) {
			return "login";
		}
		// 判断商品库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getGoodsStock();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}
		// 判断是否已经秒杀到
		MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(miaoshaOrder != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
			return "miaosha_fail";
		}
		// 减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
		return "order_detail";
	}
}
