package org.cyberwarriors.service.mapper;

import static org.cyberwarriors.domain.JobRequestAsserts.*;
import static org.cyberwarriors.domain.JobRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobRequestMapperTest {

    private JobRequestMapper jobRequestMapper;

    @BeforeEach
    void setUp() {
        jobRequestMapper = new JobRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJobRequestSample1();
        var actual = jobRequestMapper.toEntity(jobRequestMapper.toDto(expected));
        assertJobRequestAllPropertiesEquals(expected, actual);
    }
}
