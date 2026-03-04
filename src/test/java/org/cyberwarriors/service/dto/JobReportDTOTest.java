package org.cyberwarriors.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobReportDTO.class);
        JobReportDTO jobReportDTO1 = new JobReportDTO();
        jobReportDTO1.setId(1L);
        JobReportDTO jobReportDTO2 = new JobReportDTO();
        assertThat(jobReportDTO1).isNotEqualTo(jobReportDTO2);
        jobReportDTO2.setId(jobReportDTO1.getId());
        assertThat(jobReportDTO1).isEqualTo(jobReportDTO2);
        jobReportDTO2.setId(2L);
        assertThat(jobReportDTO1).isNotEqualTo(jobReportDTO2);
        jobReportDTO1.setId(null);
        assertThat(jobReportDTO1).isNotEqualTo(jobReportDTO2);
    }
}
