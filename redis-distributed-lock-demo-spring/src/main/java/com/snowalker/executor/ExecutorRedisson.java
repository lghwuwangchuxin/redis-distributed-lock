package com.snowalker.executor;

//import com.snowalker.annotation.RedisLock;
//import com.snowalker.lock.RedisDistributedLock;
import com.snowalker.lock.redisson.RedissonLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author snowalker
 * @date 2018-7-9
 * @desc Redisson锁
 */
@Component
public class ExecutorRedisson {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorRedisson.class);

    @Autowired
    RedissonLock redissonLock;


    @Scheduled(cron = "${redis.lock.cron}")
    public void execute() throws InterruptedException {
        if (redissonLock.lock("snowalker", 5)) {
            LOGGER.info("[ExecutorRedisson]--执行定时任务开始，休眠三秒");
            Thread.sleep(3000);
            System.out.println("=======================业务逻辑=============================");
            LOGGER.info("[ExecutorRedisson]--执行定时任务结束，休眠三秒");
            redissonLock.release("snowalker");
        } else {
            LOGGER.info("[ExecutorRedisson]获取锁失败");
        }

    }

}
