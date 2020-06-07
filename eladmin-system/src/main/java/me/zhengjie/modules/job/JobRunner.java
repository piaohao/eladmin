package me.zhengjie.modules.job;

import cn.hutool.core.lang.UUID;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.task.QuartzJob;
import me.zhengjie.api.repository.task.QuartzJobRepository;
import me.zhengjie.utils.SpringContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    private final QuartzJobRepository quartzJobRepository;
    private final CronTask cronTask;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<QuartzJob> jobs = quartzJobRepository.findByIsPauseIsFalse();
        jobs.forEach(job -> {
            @NotBlank String beanName = job.getBeanName();
            Object bean = SpringContextHolder.getBean(beanName);
            if (!(bean instanceof Task)) {
                return;
            }
            cronTask.setJob(job);
            String uuid = UUID.fastUUID().toString();
            JobPool.taskMap.put(uuid, job.getId());
            CronUtil.schedule(uuid, job.getCronExpression(), cronTask);
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
