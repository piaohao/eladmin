package me.zhengjie.modules.job.redis;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.api.domain.task.QuartzJob;
import me.zhengjie.modules.job.JobPool;
import me.zhengjie.modules.job.JobService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisSubscriber extends MessageListenerAdapter {

    private final RedisTemplate<String, String> redisTemplate;
    private final JobService jobService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String msg = redisTemplate.getStringSerializer().deserialize(body);
        String topic = redisTemplate.getStringSerializer().deserialize(channel);

        JSONObject json = JSONUtil.parseObj(msg);
        Long requestId = json.getLong("requestId");
        QuartzJob job = json.get("job", QuartzJob.class);

        if (JobPool.TOPIC_CRONJOB.equals(topic)) {
//            log.info("监听到topic为" + topic + "的消息：" + msg);
            @NotBlank String key = "lock:cronJob:run:" + job.getBeanName() + ":" + requestId;
            Long count = redisTemplate.opsForValue().increment(key);
            if (count == 1L) {
                jobService.execute(job);
            }
            redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        } else if (JobPool.TOPIC_UPDATE.equals(topic)) {
            jobService.updateStatus(job, requestId);
        }
    }

}
