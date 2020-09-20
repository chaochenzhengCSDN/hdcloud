package com.hodo.iiot.group2.hdcloudbankaccountshares.util;

import com.github.wxiaoqi.security.common.redisConf.RedisConfFactory;
import com.github.wxiaoqi.security.common.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RedissonUtil {

    //redis主从或者多节点的ip 多个用";"分割
    @Value("${redis.ipAndPort}")
    private String redisIpAndPort;
    @Value("${redis.password}")
    private String redisPwd;
    @Value("${redis.redis_mode}")
    private String redisMode;
    @Value("${redis.redis_slave_ip_port}")
    private String redisSlaveIpPort;

    public String getRedisIpAndPort() {
        return redisIpAndPort;
    }

    public void setRedisIpAndPort(String redisIpAndPort) {
        this.redisIpAndPort = redisIpAndPort;
    }

    public String getRedisPwd() {
        return redisPwd;
    }

    public void setRedisPwd(String redisPwd) {
        this.redisPwd = redisPwd;
    }

    public String getRedisMode() {
        return redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    public String getRedisSlaveIpPort() {
        return redisSlaveIpPort;
    }

    public void setRedisSlaveIpPort(String redisSlaveIpPort) {
        this.redisSlaveIpPort = redisSlaveIpPort;
    }

    public static  RedissonUtil redissonUtil;




    private RedissonClient redissonClient;


    public RedissonUtil() {
        redissonClient = createRedisson();
    }

    public static RedissonUtil getInstance() {


        if (redissonUtil == null) {
            synchronized (RedissonUtil.class) {
                if (redissonUtil == null) {
                    redissonUtil = new RedissonUtil();
                }
            }
        }
        return redissonUtil;
    }

    /**
     * @return 新的
     */
    public RedissonClient createRedisson() {
        RedissonClient  redisson = null;
        String ipAndPort = getRedisIpAndPort();
        String password = StringUtil.isEmpty(getRedisPwd())?null:getRedisPwd();
        //运行的mode
        String	redis_mode = StringUtil.isEmpty(getRedisMode())?"SINGLE":getRedisMode();
        //redis 主从模式下的ip地址，多个用";"分割
        String	REDIS_SLAVE_IP_PORT = StringUtil.isEmpty(getRedisSlaveIpPort())?"":getRedisSlaveIpPort();

        if(StringUtil.isNotEmpty(ipAndPort)) {
            Config config = RedisConfFactory.getRedisConf(redis_mode, password, ipAndPort, REDIS_SLAVE_IP_PORT);
            try {
                redisson = Redisson.create(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return redisson;
    }

    /**
     * 不关的,redis一直存在
     *
     * @return
     */
    public RedissonClient getRedissonClient() {
        if (null == redissonClient || redissonClient.isShutdown()) {
            redissonClient = createRedisson();
        }
        return redissonClient;
    }

}
