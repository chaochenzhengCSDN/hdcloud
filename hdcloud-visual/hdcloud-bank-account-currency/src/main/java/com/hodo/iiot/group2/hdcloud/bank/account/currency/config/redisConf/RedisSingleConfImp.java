package com.hodo.iiot.group2.hdcloud.bank.account.currency.config.redisConf;

import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * 单实例的实现方法
 *
 */
public class RedisSingleConfImp implements RedisConfInterface
{


    @Override
    public Config getRedisConfig(String password,String master_adr,String slave_adr) {
        Config config=new Config();
        SingleServerConfig serversconfig=config.useSingleServer();
        serversconfig.setConnectionPoolSize(10000);
        serversconfig.setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        serversconfig.setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
        serversconfig.setTimeout(90000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
        serversconfig.setRetryInterval(3000);//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
        if(StringUtils.isNotBlank(master_adr))
        {
            String[] strs=master_adr.split(";");

            for(String ip:strs)
            {
                if(StringUtils.isNotBlank(ip))
                {
                    serversconfig.setAddress(ip);
                }
            }
            if(StringUtils.isNotBlank(password))
            {
                serversconfig.setPassword(password);
            }

        }

        return config;
    }
}