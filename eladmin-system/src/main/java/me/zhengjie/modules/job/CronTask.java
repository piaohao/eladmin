package me.zhengjie.modules.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Setter;
import me.zhengjie.api.domain.task.QuartzJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CronTask implements Task {

    @Setter
    private QuartzJob job;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void execute() {
        String message = JSONUtil.toJsonStr(
                new JSONObject()
                        .set("job", job)
                        .set("requestId", DateUtil.currentSeconds())
        );
        redisTemplate.convertAndSend(JobPool.TOPIC_CRONJOB, message);
    }
}
