package com.hodo.iiot.group2.hdcloud.bank.account.currency.config.redisConf;

import org.redisson.config.Config;


public class RedisConfFactory {
    /**
     * redis 的config工厂 默认为single模式
     * @param mode
     * @param password
     * @param masteradr
     * @param slaveadr
     * @return
     */
    public static Config getRedisConf(String mode,String password,String masteradr,String slaveadr)
    {
        //CLUSTER,ELASTICACHE,SINGLE,SENTINEL,MASTER_SLAVE
        RedisConfInterface redisConfInterface=null;
        switch (mode) {
            case "CLUSTER":
                redisConfInterface =new RedisClusterConfImp();
                break;
            case "SINGLE":
                redisConfInterface=new RedisSingleConfImp();
                break;
            case "SENTINEL":
                redisConfInterface =new RedisSentinelConfImp();
                break;
            case "MASTER_SLAVE":
                redisConfInterface=new RedisMasterSlaveConfImp();
                break;
            default:
                redisConfInterface=new RedisSingleConfImp();
                break;
        }
        return redisConfInterface.getRedisConfig(password, masteradr, slaveadr);
    }
}