package org.cyberwarriors.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.cyberwarriors.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobRequestDTO.class);
        JobRequestDTO jobRequestDTO1 = new JobRequestDTO();
        jobRequestDTO1.setId(1L);
        JobRequestDTO jobRequestDTO2 = new JobRequestDTO();
        assertThat(jobRequestDTO1).isNotEqualTo(jobRequestDTO2);
        jobRequestDTO2.setId(jobRequestDTO1.getId());
        assertThat(jobRequestDTO1).isEqualTo(jobRequestDTO2);
        jobRequestDTO2.setId(2L);
        assertThat(jobRequestDTO1).isNotEqualTo(jobRequestDTO2);
        jobRequestDTO1.setId(null);
        assertThat(jobRequestDTO1).isNotEqualTo(jobRequestDTO2);
    }
}
