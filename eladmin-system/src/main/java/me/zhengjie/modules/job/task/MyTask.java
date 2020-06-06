package me.zhengjie.modules.job.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.cron.task.Task;
import org.springframework.stereotype.Component;

@Component
public class MyTask implements Task {
    @Override
    public void execute() {
        System.out.println("mytask run, time: " + DateTime.now());
    }
}
