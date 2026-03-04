package org.cyberwarriors.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobExecutionReportTestSamples.*;
import static org.cyberwarriors.domain.JobReportTestSamples.*;
import static org.cyberwarriors.domain.JobRequestTestSamples.*;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobRequest.class);
        JobRequest jobRequest1 = getJobRequestSample1();
        JobRequest jobRequest2 = new JobRequest();
        assertThat(jobRequest1).isNotEqualTo(jobRequest2);

        jobRequest2.setId(jobRequest1.getId());
        assertThat(jobRequest1).isEqualTo(jobRequest2);

        jobRequest2 = getJobRequestSample2();
        assertThat(jobRequest1).isNotEqualTo(jobRequest2);
    }

    @Test
    void jobReportTest() {
        JobRequest jobRequest = getJobRequestRandomSampleGenerator();
        JobReport jobReportBack = getJobReportRandomSampleGenerator();

        jobRequest.setJobReport(jobReportBack);
        assertThat(jobRequest.getJobReport()).isEqualTo(jobReportBack);
        assertThat(jobReportBack.getJobRequest()).isEqualTo(jobRequest);

        jobRequest.jobReport(null);
        assertThat(jobRequest.getJobReport()).isNull();
        assertThat(jobReportBack.getJobRequest()).isNull();
    }

    @Test
    void jobExecutionReportTest() {
        JobRequest jobRequest = getJobRequestRandomSampleGenerator();
        JobExecutionReport jobExecutionReportBack = getJobExecutionReportRandomSampleGenerator();

        jobRequest.setJobExecutionReport(jobExecutionReportBack);
        assertThat(jobRequest.getJobExecutionReport()).isEqualTo(jobExecutionReportBack);
        assertThat(jobExecutionReportBack.getJobRequest()).isEqualTo(jobRequest);

        jobRequest.jobExecutionReport(null);
        assertThat(jobRequest.getJobExecutionReport()).isNull();
        assertThat(jobExecutionReportBack.getJobRequest()).isNull();
    }
}
