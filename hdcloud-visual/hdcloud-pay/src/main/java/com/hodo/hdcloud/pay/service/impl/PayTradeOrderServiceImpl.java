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
package com.hodo.hdcloud.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.pay.entity.PayTradeOrder;
import com.hodo.hdcloud.pay.mapper.PayTradeOrderMapper;
import com.hodo.hdcloud.pay.service.PayTradeOrderService;
import org.springframework.stereotype.Service;

/**
 * 支付
 *
 * @author hodo
 * @date 2019-05-28 23:58:18
 */
@Service
public class PayTradeOrderServiceImpl extends ServiceImpl<PayTradeOrderMapper, PayTradeOrder> implements PayTradeOrderService {

}
