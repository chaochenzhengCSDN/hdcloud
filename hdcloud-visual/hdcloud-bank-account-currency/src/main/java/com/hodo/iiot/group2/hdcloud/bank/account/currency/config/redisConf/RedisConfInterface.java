package com.hodo.iiot.group2.hdcloud.bank.account.currency.config.redisConf;

import org.redisson.config.Config;

/**
 * 根据不同的redis部署方式实现不同的config
 *
 */
public interface RedisConfInterface {
    public Config getRedisConfig(String password, String masteradr, String slaveadr);
}
