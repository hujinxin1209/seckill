package com.ugia.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ugia.seckill.dao.GoodsDao;
import com.ugia.seckill.domain.Goods;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.OrderInfo;
import com.ugia.seckill.vo.GoodsVo;

@Service
public class MiaoshaService {
	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;
	
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		// 减少库存 下订单 写入秒杀订单
		goodsService.reduceStock(goods);
		// order_info miaoshao_order
		return orderService.createOrder(user, goods);
	}

}
