package org.cyberwarriors.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobReportAsserts.*;
import static org.cyberwarriors.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.cyberwarriors.IntegrationTest;
import org.cyberwarriors.domain.JobReport;
import org.cyberwarriors.repository.JobReportRepository;
import org.cyberwarriors.repository.UserRepository;
import org.cyberwarriors.service.JobReportService;
import org.cyberwarriors.service.dto.JobReportDTO;
import org.cyberwarriors.service.mapper.JobReportMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobReportResourceIT {

    private static final Instant DEFAULT_REPORT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REPORT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SUCCESSFUL = false;
    private static final Boolean UPDATED_IS_SUCCESSFUL = true;

    private static final String ENTITY_API_URL = "/api/job-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobReportRepository jobReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private JobReportRepository jobReportRepositoryMock;

    @Autowired
    private JobReportMapper jobReportMapper;

    @Mock
    private JobReportService jobReportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobReportMockMvc;

    private JobReport jobReport;

    private JobReport insertedJobReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobReport createEntity() {
        return new JobReport().reportDate(DEFAULT_REPORT_DATE).details(DEFAULT_DETAILS).isSuccessful(DEFAULT_IS_SUCCESSFUL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobReport createUpdatedEntity() {
        return new JobReport().reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);
    }

    @BeforeEach
    void initTest() {
        jobReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJobReport != null) {
            jobReportRepository.delete(insertedJobReport);
            insertedJobReport = null;
        }
    }

    @Test
    @Transactional
    void createJobReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);
        var returnedJobReportDTO = om.readValue(
            restJobReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JobReportDTO.class
        );

        // Validate the JobReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJobReport = jobReportMapper.toEntity(returnedJobReportDTO);
        assertJobReportUpdatableFieldsEquals(returnedJobReport, getPersistedJobReport(returnedJobReport));

        insertedJobReport = returnedJobReport;
    }

    @Test
    @Transactional
    void createJobReportWithExistingId() throws Exception {
        // Create the JobReport with an existing ID
        jobReport.setId(1L);
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobReport.setReportDate(null);

        // Create the JobReport, which fails.
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        restJobReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobReports() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        // Get all the jobReportList
        restJobReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].isSuccessful").value(hasItem(DEFAULT_IS_SUCCESSFUL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobReportRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJobReport() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        // Get the jobReport
        restJobReportMockMvc
            .perform(get(ENTITY_API_URL_ID, jobReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobReport.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.isSuccessful").value(DEFAULT_IS_SUCCESSFUL));
    }

    @Test
    @Transactional
    void getNonExistingJobReport() throws Exception {
        // Get the jobReport
        restJobReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobReport() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobReport
        JobReport updatedJobReport = jobReportRepository.findById(jobReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJobReport are not directly saved in db
        em.detach(updatedJobReport);
        updatedJobReport.reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);
        JobReportDTO jobReportDTO = jobReportMapper.toDto(updatedJobReport);

        restJobReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobReportToMatchAllProperties(updatedJobReport);
    }

    @Test
    @Transactional
    void putNonExistingJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobReportWithPatch() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobReport using partial update
        JobReport partialUpdatedJobReport = new JobReport();
        partialUpdatedJobReport.setId(jobReport.getId());

        partialUpdatedJobReport.reportDate(UPDATED_REPORT_DATE).isSuccessful(UPDATED_IS_SUCCESSFUL);

        restJobReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobReport))
            )
            .andExpect(status().isOk());

        // Validate the JobReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobReport, jobReport),
            getPersistedJobReport(jobReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateJobReportWithPatch() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobReport using partial update
        JobReport partialUpdatedJobReport = new JobReport();
        partialUpdatedJobReport.setId(jobReport.getId());

        partialUpdatedJobReport.reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);

        restJobReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobReport))
            )
            .andExpect(status().isOk());

        // Validate the JobReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobReportUpdatableFieldsEquals(partialUpdatedJobReport, getPersistedJobReport(partialUpdatedJobReport));
    }

    @Test
    @Transactional
    void patchNonExistingJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobReport.setId(longCount.incrementAndGet());

        // Create the JobReport
        JobReportDTO jobReportDTO = jobReportMapper.toDto(jobReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jobReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobReport() throws Exception {
        // Initialize the database
        insertedJobReport = jobReportRepository.saveAndFlush(jobReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobReport
        restJobReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobReportRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected JobReport getPersistedJobReport(JobReport jobReport) {
        return jobReportRepository.findById(jobReport.getId()).orElseThrow();
    }

    protected void assertPersistedJobReportToMatchAllProperties(JobReport expectedJobReport) {
        assertJobReportAllPropertiesEquals(expectedJobReport, getPersistedJobReport(expectedJobReport));
    }

    protected void assertPersistedJobReportToMatchUpdatableProperties(JobReport expectedJobReport) {
        assertJobReportAllUpdatablePropertiesEquals(expectedJobReport, getPersistedJobReport(expectedJobReport));
    }
}
