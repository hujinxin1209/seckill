package com.ugia.seckill.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ugia.seckill.domain.MiaoshaOrder;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.OrderInfo;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
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

	/**
	 * QPS:1306 5000 * 10
	 */
	/**
	 */
	@RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
	@ResponseBody
	public Result<OrderInfo> miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// 判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);// 10个商品，req1 req2
		int stock = goods.getStockCount();
		if (stock <= 0) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		// 判断是否已经秒杀到了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		// 减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
		return Result.success(orderInfo);
	}
}
