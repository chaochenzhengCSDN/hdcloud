package com.hodo.iiot.group2.hdcloud.bank.account.currency.config.redisConf;


import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;

/**
 * 主从模式
 * @author tommy
 *
 */
public class RedisMasterSlaveConfImp implements RedisConfInterface {

    @Override
    public Config getRedisConfig(String password,String master_adr,String slave_adr) {
        Config config=new Config();
        MasterSlaveServersConfig serversconfig=config.useMasterSlaveServers();
        if(StringUtils.isNotBlank(master_adr)&& StringUtils.isNotBlank(slave_adr))
        {
            String[] masterstrs=master_adr.split(";");
            String[] slavestrs=slave_adr.split(";");
            for(String mip:masterstrs)
            {
                if(StringUtils.isNotBlank(mip))
                {
                    serversconfig.setMasterAddress(mip);
                }
            }
            for(String sip:slavestrs)
            {
                if(StringUtils.isNotBlank(sip))
                {
                    serversconfig.addSlaveAddress(sip);
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