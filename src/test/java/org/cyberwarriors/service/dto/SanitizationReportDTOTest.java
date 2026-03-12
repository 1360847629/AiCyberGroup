package org.cyberwarriors.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanitizationReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SanitizationReportDTO.class);
        SanitizationReportDTO sanitizationReportDTO1 = new SanitizationReportDTO();
        sanitizationReportDTO1.setId(1L);
        SanitizationReportDTO sanitizationReportDTO2 = new SanitizationReportDTO();
        assertThat(sanitizationReportDTO1).isNotEqualTo(sanitizationReportDTO2);
        sanitizationReportDTO2.setId(sanitizationReportDTO1.getId());
        assertThat(sanitizationReportDTO1).isEqualTo(sanitizationReportDTO2);
        sanitizationReportDTO2.setId(2L);
        assertThat(sanitizationReportDTO1).isNotEqualTo(sanitizationReportDTO2);
        sanitizationReportDTO1.setId(null);
        assertThat(sanitizationReportDTO1).isNotEqualTo(sanitizationReportDTO2);
    }
}
