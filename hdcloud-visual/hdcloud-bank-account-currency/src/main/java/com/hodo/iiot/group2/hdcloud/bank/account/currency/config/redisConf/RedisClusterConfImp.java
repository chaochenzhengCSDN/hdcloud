package com.hodo.iiot.group2.hdcloud.bank.account.currency.config.redisConf;

import org.apache.commons.lang.StringUtils;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

public class RedisClusterConfImp implements RedisConfInterface {

    @Override
    public Config getRedisConfig(String password,String master_adr,String slave_adr) {
        Config config=new Config();
        ClusterServersConfig serversconfig=config.useClusterServers();
        serversconfig.setTimeout(500000);

        if(StringUtils.isNotBlank(master_adr))
        {
            serversconfig.setScanInterval(2000);
            String[] strs=master_adr.split(";");

            for(String ip:strs)
            {
                if(StringUtils.isNotBlank(ip))
                {
                    serversconfig.addNodeAddress(ip);
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
