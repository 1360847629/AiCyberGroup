package org.cyberwarriors.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JobExecutionReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobExecutionReport getJobExecutionReportSample1() {
        return new JobExecutionReport().id(1L).executionNode("executionNode1");
    }

    public static JobExecutionReport getJobExecutionReportSample2() {
        return new JobExecutionReport().id(2L).executionNode("executionNode2");
    }

    public static JobExecutionReport getJobExecutionReportRandomSampleGenerator() {
        return new JobExecutionReport().id(longCount.incrementAndGet()).executionNode(UUID.randomUUID().toString());
    }
}
