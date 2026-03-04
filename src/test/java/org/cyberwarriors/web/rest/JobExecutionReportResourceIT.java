package org.cyberwarriors.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobExecutionReportAsserts.*;
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
import org.cyberwarriors.domain.JobExecutionReport;
import org.cyberwarriors.domain.enumeration.Status;
import org.cyberwarriors.repository.JobExecutionReportRepository;
import org.cyberwarriors.repository.UserRepository;
import org.cyberwarriors.service.JobExecutionReportService;
import org.cyberwarriors.service.dto.JobExecutionReportDTO;
import org.cyberwarriors.service.mapper.JobExecutionReportMapper;
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
 * Integration tests for the {@link JobExecutionReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobExecutionReportResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EXECUTION_NODE = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTION_NODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXECUTION_LOG = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTION_LOG = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/job-execution-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobExecutionReportRepository jobExecutionReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private JobExecutionReportRepository jobExecutionReportRepositoryMock;

    @Autowired
    private JobExecutionReportMapper jobExecutionReportMapper;

    @Mock
    private JobExecutionReportService jobExecutionReportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobExecutionReportMockMvc;

    private JobExecutionReport jobExecutionReport;

    private JobExecutionReport insertedJobExecutionReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobExecutionReport createEntity() {
        return new JobExecutionReport()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .executionNode(DEFAULT_EXECUTION_NODE)
            .executionLog(DEFAULT_EXECUTION_LOG)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobExecutionReport createUpdatedEntity() {
        return new JobExecutionReport()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .executionNode(UPDATED_EXECUTION_NODE)
            .executionLog(UPDATED_EXECUTION_LOG)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        jobExecutionReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJobExecutionReport != null) {
            jobExecutionReportRepository.delete(insertedJobExecutionReport);
            insertedJobExecutionReport = null;
        }
    }

    @Test
    @Transactional
    void createJobExecutionReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);
        var returnedJobExecutionReportDTO = om.readValue(
            restJobExecutionReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobExecutionReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JobExecutionReportDTO.class
        );

        // Validate the JobExecutionReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJobExecutionReport = jobExecutionReportMapper.toEntity(returnedJobExecutionReportDTO);
        assertJobExecutionReportUpdatableFieldsEquals(
            returnedJobExecutionReport,
            getPersistedJobExecutionReport(returnedJobExecutionReport)
        );

        insertedJobExecutionReport = returnedJobExecutionReport;
    }

    @Test
    @Transactional
    void createJobExecutionReportWithExistingId() throws Exception {
        // Create the JobExecutionReport with an existing ID
        jobExecutionReport.setId(1L);
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobExecutionReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobExecutionReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobExecutionReport.setStatus(null);

        // Create the JobExecutionReport, which fails.
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        restJobExecutionReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobExecutionReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobExecutionReports() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        // Get all the jobExecutionReportList
        restJobExecutionReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobExecutionReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].executionNode").value(hasItem(DEFAULT_EXECUTION_NODE)))
            .andExpect(jsonPath("$.[*].executionLog").value(hasItem(DEFAULT_EXECUTION_LOG)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobExecutionReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobExecutionReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobExecutionReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobExecutionReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobExecutionReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobExecutionReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobExecutionReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobExecutionReportRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJobExecutionReport() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        // Get the jobExecutionReport
        restJobExecutionReportMockMvc
            .perform(get(ENTITY_API_URL_ID, jobExecutionReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobExecutionReport.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.executionNode").value(DEFAULT_EXECUTION_NODE))
            .andExpect(jsonPath("$.executionLog").value(DEFAULT_EXECUTION_LOG))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJobExecutionReport() throws Exception {
        // Get the jobExecutionReport
        restJobExecutionReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobExecutionReport() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobExecutionReport
        JobExecutionReport updatedJobExecutionReport = jobExecutionReportRepository.findById(jobExecutionReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJobExecutionReport are not directly saved in db
        em.detach(updatedJobExecutionReport);
        updatedJobExecutionReport
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .executionNode(UPDATED_EXECUTION_NODE)
            .executionLog(UPDATED_EXECUTION_LOG)
            .status(UPDATED_STATUS);
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(updatedJobExecutionReport);

        restJobExecutionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobExecutionReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobExecutionReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobExecutionReportToMatchAllProperties(updatedJobExecutionReport);
    }

    @Test
    @Transactional
    void putNonExistingJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobExecutionReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobExecutionReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobExecutionReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobExecutionReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobExecutionReportWithPatch() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobExecutionReport using partial update
        JobExecutionReport partialUpdatedJobExecutionReport = new JobExecutionReport();
        partialUpdatedJobExecutionReport.setId(jobExecutionReport.getId());

        partialUpdatedJobExecutionReport
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .executionLog(UPDATED_EXECUTION_LOG)
            .status(UPDATED_STATUS);

        restJobExecutionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobExecutionReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobExecutionReport))
            )
            .andExpect(status().isOk());

        // Validate the JobExecutionReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobExecutionReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobExecutionReport, jobExecutionReport),
            getPersistedJobExecutionReport(jobExecutionReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateJobExecutionReportWithPatch() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobExecutionReport using partial update
        JobExecutionReport partialUpdatedJobExecutionReport = new JobExecutionReport();
        partialUpdatedJobExecutionReport.setId(jobExecutionReport.getId());

        partialUpdatedJobExecutionReport
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .executionNode(UPDATED_EXECUTION_NODE)
            .executionLog(UPDATED_EXECUTION_LOG)
            .status(UPDATED_STATUS);

        restJobExecutionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobExecutionReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobExecutionReport))
            )
            .andExpect(status().isOk());

        // Validate the JobExecutionReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobExecutionReportUpdatableFieldsEquals(
            partialUpdatedJobExecutionReport,
            getPersistedJobExecutionReport(partialUpdatedJobExecutionReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobExecutionReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobExecutionReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobExecutionReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobExecutionReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobExecutionReport.setId(longCount.incrementAndGet());

        // Create the JobExecutionReport
        JobExecutionReportDTO jobExecutionReportDTO = jobExecutionReportMapper.toDto(jobExecutionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobExecutionReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jobExecutionReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobExecutionReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobExecutionReport() throws Exception {
        // Initialize the database
        insertedJobExecutionReport = jobExecutionReportRepository.saveAndFlush(jobExecutionReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobExecutionReport
        restJobExecutionReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobExecutionReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobExecutionReportRepository.count();
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

    protected JobExecutionReport getPersistedJobExecutionReport(JobExecutionReport jobExecutionReport) {
        return jobExecutionReportRepository.findById(jobExecutionReport.getId()).orElseThrow();
    }

    protected void assertPersistedJobExecutionReportToMatchAllProperties(JobExecutionReport expectedJobExecutionReport) {
        assertJobExecutionReportAllPropertiesEquals(expectedJobExecutionReport, getPersistedJobExecutionReport(expectedJobExecutionReport));
    }

    protected void assertPersistedJobExecutionReportToMatchUpdatableProperties(JobExecutionReport expectedJobExecutionReport) {
        assertJobExecutionReportAllUpdatablePropertiesEquals(
            expectedJobExecutionReport,
            getPersistedJobExecutionReport(expectedJobExecutionReport)
        );
    }
}
