package org.cyberwarriors.service.mapper;

import static org.cyberwarriors.domain.SanitizationReportAsserts.*;
import static org.cyberwarriors.domain.SanitizationReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SanitizationReportMapperTest {

    private SanitizationReportMapper sanitizationReportMapper;

    @BeforeEach
    void setUp() {
        sanitizationReportMapper = new SanitizationReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSanitizationReportSample1();
        var actual = sanitizationReportMapper.toEntity(sanitizationReportMapper.toDto(expected));
        assertSanitizationReportAllPropertiesEquals(expected, actual);
    }
}
