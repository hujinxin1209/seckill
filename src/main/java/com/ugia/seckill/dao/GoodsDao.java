package com.ugia.seckill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import com.ugia.seckill.vo.GoodsVo;

@Mapper
public interface GoodsDao {
	
	@Select("select g.*, mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from "
			+ "miaosha_goods mg left join goods g on mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*, mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from "
			+ "miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id=#{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId")long goodsId);
}
