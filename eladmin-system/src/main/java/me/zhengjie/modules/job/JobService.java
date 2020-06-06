package me.zhengjie.modules.job;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.task.QuartzJob;
import me.zhengjie.api.repository.task.QuartzJobRepository;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.quartz.service.dto.JobQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SpringContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class JobService {

    private final QuartzJobRepository quartzJobRepository;

    public Object queryAll(JobQueryCriteria criteria, Pageable pageable){
        return PageUtil.toPage(quartzJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable));
    }

    public void execute(Long id) {
        QuartzJob job = quartzJobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(QuartzJob.class, "id", id + ""));
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
        Task task = (Task) bean;
        if (job.getIsPause()) {
            String uuid = UUID.fastUUID().toString();
            JobPool.taskMap.put(uuid, job.getId());
            CronUtil.schedule(uuid, job.getCronExpression(), task);

            job.setIsPause(false);
            quartzJobRepository.save(job);
        } else {
            String uuid = JobPool.taskMap.inverse().get(job.getId());
            CronUtil.remove(uuid);
            JobPool.taskMap.remove(uuid);

            job.setIsPause(true);
            quartzJobRepository.save(job);
        }
    }

}
