package org.cyberwarriors.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobRequestTestSamples.*;
import static org.cyberwarriors.domain.SanitizationReportTestSamples.*;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanitizationReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SanitizationReport.class);
        SanitizationReport sanitizationReport1 = getSanitizationReportSample1();
        SanitizationReport sanitizationReport2 = new SanitizationReport();
        assertThat(sanitizationReport1).isNotEqualTo(sanitizationReport2);

        sanitizationReport2.setId(sanitizationReport1.getId());
        assertThat(sanitizationReport1).isEqualTo(sanitizationReport2);

        sanitizationReport2 = getSanitizationReportSample2();
        assertThat(sanitizationReport1).isNotEqualTo(sanitizationReport2);
    }

    @Test
    void jobRequestTest() {
        SanitizationReport sanitizationReport = getSanitizationReportRandomSampleGenerator();
        JobRequest jobRequestBack = getJobRequestRandomSampleGenerator();

        sanitizationReport.setJobRequest(jobRequestBack);
        assertThat(sanitizationReport.getJobRequest()).isEqualTo(jobRequestBack);

        sanitizationReport.jobRequest(null);
        assertThat(sanitizationReport.getJobRequest()).isNull();
    }
}
