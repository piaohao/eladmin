package me.zhengjie.modules.job;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.zhengjie.api.domain.task.QuartzJob;

public class JobPool {

    public static BiMap<String, Long> taskMap = HashBiMap.create();

    public static final String TOPIC_CRONJOB = "cronJob";

    public static final String TOPIC_UPDATE = "updateJob";

}
