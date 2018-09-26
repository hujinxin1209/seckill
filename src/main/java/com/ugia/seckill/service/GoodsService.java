package com.ugia.seckill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ugia.seckill.dao.GoodsDao;
import com.ugia.seckill.dao.UserDao;
import com.ugia.seckill.domain.Goods;
import com.ugia.seckill.domain.MiaoshaGoods;
import com.ugia.seckill.domain.User;
import com.ugia.seckill.vo.GoodsVo;

@Service
public class GoodsService {
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsDao.reduceStock(g);
		return ret > 0;
	}
}
