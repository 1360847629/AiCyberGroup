package org.cyberwarriors.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class JobReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobReport getJobReportSample1() {
        return new JobReport().id(1L);
    }

    public static JobReport getJobReportSample2() {
        return new JobReport().id(2L);
    }

    public static JobReport getJobReportRandomSampleGenerator() {
        return new JobReport().id(longCount.incrementAndGet());
    }
}
