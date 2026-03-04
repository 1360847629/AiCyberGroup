package org.cyberwarriors.service.mapper;

import static org.cyberwarriors.domain.JobReportAsserts.*;
import static org.cyberwarriors.domain.JobReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobReportMapperTest {

    private JobReportMapper jobReportMapper;

    @BeforeEach
    void setUp() {
        jobReportMapper = new JobReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJobReportSample1();
        var actual = jobReportMapper.toEntity(jobReportMapper.toDto(expected));
        assertJobReportAllPropertiesEquals(expected, actual);
    }
}
