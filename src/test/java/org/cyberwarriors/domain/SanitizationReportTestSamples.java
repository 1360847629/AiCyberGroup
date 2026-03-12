package org.cyberwarriors.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SanitizationReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SanitizationReport getSanitizationReportSample1() {
        return new SanitizationReport().id(1L);
    }

    public static SanitizationReport getSanitizationReportSample2() {
        return new SanitizationReport().id(2L);
    }

    public static SanitizationReport getSanitizationReportRandomSampleGenerator() {
        return new SanitizationReport().id(longCount.incrementAndGet());
    }
}
