package base.framework.distributed.impl.redis;

import redis.clients.jedis.*;

/**
 * Created by cl on 2018/4/9.
 * jedis操作整合接口
 */
public interface JedisExecutor extends JedisCommands, MultiKeyCommands,
        AdvancedJedisCommands, ScriptingCommands, BasicCommands, ClusterCommands, SentinelCommands {
}
