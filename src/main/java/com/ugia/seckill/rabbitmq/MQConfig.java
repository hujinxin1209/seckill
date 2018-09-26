package com.ugia.seckill.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
	public static final String MIAOSHA_QUEUE = "miaosha_queue";
	public static final String QUEUE = "queue";
	public static final String HEADERS_QUEUE = "headers.queue";
	public static final String TOPIC_QUEUE1 = "topic.queue1";
	public static final String TOPIC_QUEUE2 = "topic.queue2";
	public static final String TOPIC_EXCHANGE= "topicExchange";
	public static final String ROUTING_KEY1 = "topic.key1";
	public static final String ROUTING_KEY2 = "topic.#";
	public static final String FANOUT_EXCHANGE = "fanoutExchange";
	public static final String HEADERS_EXCHANGE = "headersExchange";
	
	// Direct模式 交换机Exchange
	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}
	
	// Topic模式 匹配 routing key
	@Bean
	public Queue topicQueue1() {
		return new Queue(TOPIC_QUEUE1, true);
	}
	@Bean
	public Queue topicQueue2() {
		return new Queue(TOPIC_QUEUE2, true);
	}
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TOPIC_EXCHANGE);
	}
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
	}
	@Bean
	public Binding topicBinding2() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY2);
	}
	
	// Fanout模式 广播
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(FANOUT_EXCHANGE);
	}
	@Bean
	public Binding fanoutBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
	}
	@Bean
	public Binding fanoutBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
	}
	
	// Header模式 匹配 map
	@Bean
	public HeadersExchange headersExchange() {
		return new HeadersExchange(HEADERS_EXCHANGE);
	}
	@Bean
	public Queue headersQueue() {
		return new Queue(HEADERS_QUEUE);
	}
	@Bean
	public Binding headersBinding() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header1", "value1");
		map.put("header2", "value2");
		return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
		
	}
}
