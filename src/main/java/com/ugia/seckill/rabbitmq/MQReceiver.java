package com.ugia.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ugia.seckill.domain.MiaoshaOrder;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
import com.ugia.seckill.service.GoodsService;
import com.ugia.seckill.service.MiaoshaService;
import com.ugia.seckill.service.OrderService;
import com.ugia.seckill.vo.GoodsVo;

@Service
public class MQReceiver {
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MiaoshaService miaoshaService;

	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	@RabbitListener(queues = MQConfig.QUEUE)
	public void receive(String message) {
		log.info("receive message" + message);
	}

	@RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
	public void miaoshaReceive(String message) {
		log.info("receive message" + message);
		MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
		MiaoshaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();

		// 判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if (stock <= 0) {
			return;
		}
		// 判断是否已经秒杀到了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order != null) {
			return ;
		}
		// 生成秒杀订单
		miaoshaService.miaosha(user, goods);
	}

	@RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
	public void receiveTopic1(String message) {
		log.info("topic queue1 message1" + message);
	}

	@RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
	public void receiveTopic2(String message) {
		log.info("topic queue2 message2" + message);
	}

	@RabbitListener(queues = MQConfig.HEADERS_QUEUE)
	public void receiveHeaders(byte[] message) {
		log.info("Headers queue message" + new String(message));
	}
}
