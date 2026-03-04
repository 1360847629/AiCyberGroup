package org.cyberwarriors.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobExecutionReportTestSamples.*;
import static org.cyberwarriors.domain.JobRequestTestSamples.*;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobExecutionReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobExecutionReport.class);
        JobExecutionReport jobExecutionReport1 = getJobExecutionReportSample1();
        JobExecutionReport jobExecutionReport2 = new JobExecutionReport();
        assertThat(jobExecutionReport1).isNotEqualTo(jobExecutionReport2);

        jobExecutionReport2.setId(jobExecutionReport1.getId());
        assertThat(jobExecutionReport1).isEqualTo(jobExecutionReport2);

        jobExecutionReport2 = getJobExecutionReportSample2();
        assertThat(jobExecutionReport1).isNotEqualTo(jobExecutionReport2);
    }

    @Test
    void jobRequestTest() {
        JobExecutionReport jobExecutionReport = getJobExecutionReportRandomSampleGenerator();
        JobRequest jobRequestBack = getJobRequestRandomSampleGenerator();

        jobExecutionReport.setJobRequest(jobRequestBack);
        assertThat(jobExecutionReport.getJobRequest()).isEqualTo(jobRequestBack);

        jobExecutionReport.jobRequest(null);
        assertThat(jobExecutionReport.getJobRequest()).isNull();
    }
}
