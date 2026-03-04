package org.cyberwarriors.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobExecutionReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobExecutionReportDTO.class);
        JobExecutionReportDTO jobExecutionReportDTO1 = new JobExecutionReportDTO();
        jobExecutionReportDTO1.setId(1L);
        JobExecutionReportDTO jobExecutionReportDTO2 = new JobExecutionReportDTO();
        assertThat(jobExecutionReportDTO1).isNotEqualTo(jobExecutionReportDTO2);
        jobExecutionReportDTO2.setId(jobExecutionReportDTO1.getId());
        assertThat(jobExecutionReportDTO1).isEqualTo(jobExecutionReportDTO2);
        jobExecutionReportDTO2.setId(2L);
        assertThat(jobExecutionReportDTO1).isNotEqualTo(jobExecutionReportDTO2);
        jobExecutionReportDTO1.setId(null);
        assertThat(jobExecutionReportDTO1).isNotEqualTo(jobExecutionReportDTO2);
    }
}
