package org.cyberwarriors.service.mapper;

import static org.cyberwarriors.domain.JobExecutionReportAsserts.*;
import static org.cyberwarriors.domain.JobExecutionReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobExecutionReportMapperTest {

    private JobExecutionReportMapper jobExecutionReportMapper;

    @BeforeEach
    void setUp() {
        jobExecutionReportMapper = new JobExecutionReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJobExecutionReportSample1();
        var actual = jobExecutionReportMapper.toEntity(jobExecutionReportMapper.toDto(expected));
        assertJobExecutionReportAllPropertiesEquals(expected, actual);
    }
}
