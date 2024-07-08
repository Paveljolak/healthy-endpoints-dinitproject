package com.pavelDinit.dinitProject.components;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
public class Scheduler {
}
