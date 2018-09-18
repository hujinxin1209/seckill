package com.ugia.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP.Queue;
import com.ugia.seckill.redis.RedisService;

@Service
public class MQSender {
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
	@Autowired
	AmqpTemplate amqpTemplate;
	
	public void send(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message:" + message);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
	}
}
