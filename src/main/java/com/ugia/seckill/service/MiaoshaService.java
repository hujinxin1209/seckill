package com.ugia.seckill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ugia.seckill.dao.GoodsDao;
import com.ugia.seckill.domain.Goods;
import com.ugia.seckill.domain.MiaoshaOrder;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.OrderInfo;
import com.ugia.seckill.redis.MiaoshaKey;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.util.MD5Util;
import com.ugia.seckill.util.UUIDUtil;
import com.ugia.seckill.vo.GoodsVo;

@Service
public class MiaoshaService {
	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;
	
	@Autowired
	RedisService redisService;
	
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		// 减少库存 下订单 写入秒杀订单
		boolean success = goodsService.reduceStock(goods);
		if(success) {
			// order_info miaoshao_order
			return orderService.createOrder(user, goods);
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
	}

	// 
	public long getMiaoshaResult(Long id, long goodsId) {
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
		if(order != null) {
			return order.getOrderId();
		} else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private void setGoodsOver(Long id) {
		redisService.set(MiaoshaKey.isGoodsOver, ""+id, true);
	}

	
	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
	}

	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}

	public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
		if(user == null || path == null) {
			return false;
		}
		String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, String.class);
		return path.equals(pathOld);
	}

	public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
    	redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, str);
		return str;
	}

}
