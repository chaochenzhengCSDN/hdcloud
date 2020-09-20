/*
 *    Copyright (c) 2018-2025, hodo All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: 江苏红豆工业互联网有限公司
 */

package com.hodo.hdcloud.pay.handler.impl;

import com.hodo.hdcloud.common.sequence.sequence.Sequence;
import com.hodo.hdcloud.pay.entity.PayGoodsOrder;
import com.hodo.hdcloud.pay.entity.PayTradeOrder;
import com.hodo.hdcloud.pay.handler.PayOrderHandler;
import com.hodo.hdcloud.pay.mapper.PayGoodsOrderMapper;
import com.hodo.hdcloud.pay.utils.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hodo
 * @date 2019-05-31
 */
public abstract class AbstractPayOrderHandler implements PayOrderHandler {
	@Autowired
	private PayGoodsOrderMapper goodsOrderMapper;
	@Autowired
	private Sequence paySequence;


	/**
	 * 创建商品订单
	 *
	 * @param goodsOrder 商品订单
	 * @return
	 */
	@Override
	public void createGoodsOrder(PayGoodsOrder goodsOrder) {
		goodsOrder.setPayOrderId(paySequence.nextNo());
		goodsOrder.setStatus(OrderStatusEnum.INIT.getStatus());
		goodsOrderMapper.insert(goodsOrder);
	}

	/**
	 * 调用入口
	 *
	 * @return
	 */
	@Override
	public Object handle(PayGoodsOrder payGoodsOrder) {
		createGoodsOrder(payGoodsOrder);
		PayTradeOrder tradeOrder = createTradeOrder(payGoodsOrder);
		Object result = pay(payGoodsOrder, tradeOrder);
		updateOrder(payGoodsOrder, tradeOrder);
		return result;
	}
}
