package com.ugia.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
	
	public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY1, msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY2, msg+"2");
		
	}
	
	public void sendFanout(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send Fanout message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg+"1");
		
	}
	
	public void sendHeader(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send Header message:"+msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), null);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
		
	}
	
}
