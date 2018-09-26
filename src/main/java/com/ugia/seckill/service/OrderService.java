package com.ugia.seckill.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ugia.seckill.dao.OrderDao;
import com.ugia.seckill.domain.MiaoshaOrder;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.OrderInfo;
import com.ugia.seckill.redis.OrderKey;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.vo.GoodsVo;

@Service
public class OrderService {
	@Autowired
	OrderDao orderDao;
	@Autowired
	RedisService redisService;

	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {

		//return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
	}

	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setUserId(user.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		orderDao.insertMiaoshaOrder(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goods.getId(), MiaoshaOrder.class);
		return orderInfo;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}
	
	
}
