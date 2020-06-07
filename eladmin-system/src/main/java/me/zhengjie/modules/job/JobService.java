package me.zhengjie.modules.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.task.QuartzJob;
import me.zhengjie.api.repository.task.QuartzJobRepository;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SpringContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JobService {

    private final QuartzJobRepository quartzJobRepository;
    private final CronTask cronTask;
    private final RedisTemplate<String, String> redisTemplate;

    public Object queryAll(JobQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(quartzJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable));
    }

    public void execute(Long id) {
        QuartzJob job = quartzJobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(QuartzJob.class, "id", id + ""));
        @NotBlank String beanName = job.getBeanName();
        Object bean = SpringContextHolder.getBean(beanName);
        if (!(bean instanceof Task)) {
            throw new RuntimeException("任务需继承自cn.hutool.cron.task.Task");
        }
        String message = JSONUtil.toJsonStr(
                new JSONObject()
                        .set("job", job)
                        .set("requestId", DateUtil.currentSeconds())
        );
        redisTemplate.convertAndSend(JobPool.TOPIC_CRONJOB, message);
    }

    public void execute(QuartzJob job) {
        @NotBlank String beanName = job.getBeanName();
        Object bean = SpringContextHolder.getBean(beanName);
        if (!(bean instanceof Task)) {
            throw new RuntimeException("任务需继承自cn.hutool.cron.task.Task");
        }
        ThreadUtil.execute(((Task) bean)::execute);
    }

    @Transactional
    public void updateStatus(Long id) {
        QuartzJob job = quartzJobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(QuartzJob.class, "id", id + ""));
        @NotBlank String beanName = job.getBeanName();
        Object bean = SpringContextHolder.getBean(beanName);
        if (!(bean instanceof Task)) {
            return;
        }
        String message = JSONUtil.toJsonStr(
                new JSONObject()
                        .set("job", job)
                        .set("requestId", DateUtil.currentSeconds())
        );
        redisTemplate.convertAndSend(JobPool.TOPIC_UPDATE, message);
    }

    @Transactional
    public void updateStatus(QuartzJob job, Long requestId) {
        cronTask.setJob(job);
        if (job.getIsPause()) {
            String uuid = UUID.fastUUID().toString();
            JobPool.taskMap.put(uuid, job.getId());
            CronUtil.schedule(uuid, job.getCronExpression(), cronTask);
        } else {
            String uuid = JobPool.taskMap.inverse().get(job.getId());
            CronUtil.remove(uuid);
            JobPool.taskMap.remove(uuid);
        }

        if (job.getIsPause()) {
            job.setIsPause(false);
        } else {
            job.setIsPause(true);
        }
        @NotBlank String key = "lock:cronJob:update:" + job.getBeanName() + ":" + requestId;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1L) {
            quartzJobRepository.save(job);
        }
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);
    }

}
