package org.cyberwarriors.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobReportTestSamples.*;
import static org.cyberwarriors.domain.JobRequestTestSamples.*;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobReport.class);
        JobReport jobReport1 = getJobReportSample1();
        JobReport jobReport2 = new JobReport();
        assertThat(jobReport1).isNotEqualTo(jobReport2);

        jobReport2.setId(jobReport1.getId());
        assertThat(jobReport1).isEqualTo(jobReport2);

        jobReport2 = getJobReportSample2();
        assertThat(jobReport1).isNotEqualTo(jobReport2);
    }

    @Test
    void jobRequestTest() {
        JobReport jobReport = getJobReportRandomSampleGenerator();
        JobRequest jobRequestBack = getJobRequestRandomSampleGenerator();

        jobReport.setJobRequest(jobRequestBack);
        assertThat(jobReport.getJobRequest()).isEqualTo(jobRequestBack);

        jobReport.jobRequest(null);
        assertThat(jobReport.getJobRequest()).isNull();
    }
}
