package com.ugia.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.User;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
import com.ugia.seckill.service.GoodsService;
import com.ugia.seckill.service.MiaoshaUserService;
import com.ugia.seckill.service.UserService;
import com.ugia.seckill.util.ValidatorUtil;
import com.ugia.seckill.vo.GoodsVo;
import com.ugia.seckill.vo.LoginVo;

import javassist.expr.NewArray;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	RedisService redisService;
	@Autowired
	MiaoshaUserService miaoshaUserService;
	@Autowired
	GoodsService goodsService;
	
	@RequestMapping("/to_list")
	public String listGoodsVo(HttpServletResponse response, Model model, MiaoshaUser user) {
		model.addAttribute("user", user);
		// 查询商品
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);
		return "goods_list";
	}
	
	@RequestMapping("/to_detail/{goodsId}")
	public String detail(MiaoshaUser user, Model model, @PathVariable("goodsId")long goodsId) {
		model.addAttribute("user", user);
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);
		
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int miaoshaStatus = 0;
		int remainSeconds = 0;
		
		if(now < startAt) { // 秒杀未开始，倒计时
			miaoshaStatus = 0;
			remainSeconds = (int)(startAt - now) / 1000;
		}else if(now > endAt) { // 秒杀已结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else { // 秒杀正在进行
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		return "goods_detail";
	}
}
