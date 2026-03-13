package org.cyberwarriors.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class JobRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static JobRequest getJobRequestSample1() {
        return new JobRequest().id(1L).score(1).fileName("fileName1");
    }

    public static JobRequest getJobRequestSample2() {
        return new JobRequest().id(2L).score(2).fileName("fileName2");
    }

    public static JobRequest getJobRequestRandomSampleGenerator() {
        return new JobRequest().id(longCount.incrementAndGet()).score(intCount.incrementAndGet()).fileName(UUID.randomUUID().toString());
    }
}
