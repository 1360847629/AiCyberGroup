package org.cyberwarriors.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.SanitizationReportAsserts.*;
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
import org.cyberwarriors.domain.SanitizationReport;
import org.cyberwarriors.repository.SanitizationReportRepository;
import org.cyberwarriors.repository.UserRepository;
import org.cyberwarriors.service.SanitizationReportService;
import org.cyberwarriors.service.dto.SanitizationReportDTO;
import org.cyberwarriors.service.mapper.SanitizationReportMapper;
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
 * Integration tests for the {@link SanitizationReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SanitizationReportResourceIT {

    private static final Instant DEFAULT_REPORT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REPORT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SUCCESSFUL = false;
    private static final Boolean UPDATED_IS_SUCCESSFUL = true;

    private static final String ENTITY_API_URL = "/api/sanitization-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SanitizationReportRepository sanitizationReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SanitizationReportRepository sanitizationReportRepositoryMock;

    @Autowired
    private SanitizationReportMapper sanitizationReportMapper;

    @Mock
    private SanitizationReportService sanitizationReportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanitizationReportMockMvc;

    private SanitizationReport sanitizationReport;

    private SanitizationReport insertedSanitizationReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SanitizationReport createEntity() {
        return new SanitizationReport().reportDate(DEFAULT_REPORT_DATE).details(DEFAULT_DETAILS).isSuccessful(DEFAULT_IS_SUCCESSFUL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SanitizationReport createUpdatedEntity() {
        return new SanitizationReport().reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);
    }

    @BeforeEach
    void initTest() {
        sanitizationReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSanitizationReport != null) {
            sanitizationReportRepository.delete(insertedSanitizationReport);
            insertedSanitizationReport = null;
        }
    }

    @Test
    @Transactional
    void createSanitizationReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);
        var returnedSanitizationReportDTO = om.readValue(
            restSanitizationReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sanitizationReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SanitizationReportDTO.class
        );

        // Validate the SanitizationReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSanitizationReport = sanitizationReportMapper.toEntity(returnedSanitizationReportDTO);
        assertSanitizationReportUpdatableFieldsEquals(
            returnedSanitizationReport,
            getPersistedSanitizationReport(returnedSanitizationReport)
        );

        insertedSanitizationReport = returnedSanitizationReport;
    }

    @Test
    @Transactional
    void createSanitizationReportWithExistingId() throws Exception {
        // Create the SanitizationReport with an existing ID
        sanitizationReport.setId(1L);
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanitizationReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sanitizationReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sanitizationReport.setReportDate(null);

        // Create the SanitizationReport, which fails.
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        restSanitizationReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sanitizationReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSanitizationReports() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        // Get all the sanitizationReportList
        restSanitizationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanitizationReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].isSuccessful").value(hasItem(DEFAULT_IS_SUCCESSFUL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSanitizationReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sanitizationReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSanitizationReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sanitizationReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSanitizationReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sanitizationReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSanitizationReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sanitizationReportRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSanitizationReport() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        // Get the sanitizationReport
        restSanitizationReportMockMvc
            .perform(get(ENTITY_API_URL_ID, sanitizationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanitizationReport.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.isSuccessful").value(DEFAULT_IS_SUCCESSFUL));
    }

    @Test
    @Transactional
    void getNonExistingSanitizationReport() throws Exception {
        // Get the sanitizationReport
        restSanitizationReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSanitizationReport() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sanitizationReport
        SanitizationReport updatedSanitizationReport = sanitizationReportRepository.findById(sanitizationReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSanitizationReport are not directly saved in db
        em.detach(updatedSanitizationReport);
        updatedSanitizationReport.reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(updatedSanitizationReport);

        restSanitizationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanitizationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sanitizationReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSanitizationReportToMatchAllProperties(updatedSanitizationReport);
    }

    @Test
    @Transactional
    void putNonExistingSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanitizationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sanitizationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sanitizationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sanitizationReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanitizationReportWithPatch() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sanitizationReport using partial update
        SanitizationReport partialUpdatedSanitizationReport = new SanitizationReport();
        partialUpdatedSanitizationReport.setId(sanitizationReport.getId());

        restSanitizationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanitizationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSanitizationReport))
            )
            .andExpect(status().isOk());

        // Validate the SanitizationReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSanitizationReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSanitizationReport, sanitizationReport),
            getPersistedSanitizationReport(sanitizationReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateSanitizationReportWithPatch() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sanitizationReport using partial update
        SanitizationReport partialUpdatedSanitizationReport = new SanitizationReport();
        partialUpdatedSanitizationReport.setId(sanitizationReport.getId());

        partialUpdatedSanitizationReport.reportDate(UPDATED_REPORT_DATE).details(UPDATED_DETAILS).isSuccessful(UPDATED_IS_SUCCESSFUL);

        restSanitizationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanitizationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSanitizationReport))
            )
            .andExpect(status().isOk());

        // Validate the SanitizationReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSanitizationReportUpdatableFieldsEquals(
            partialUpdatedSanitizationReport,
            getPersistedSanitizationReport(partialUpdatedSanitizationReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanitizationReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sanitizationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sanitizationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanitizationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sanitizationReport.setId(longCount.incrementAndGet());

        // Create the SanitizationReport
        SanitizationReportDTO sanitizationReportDTO = sanitizationReportMapper.toDto(sanitizationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanitizationReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sanitizationReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SanitizationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanitizationReport() throws Exception {
        // Initialize the database
        insertedSanitizationReport = sanitizationReportRepository.saveAndFlush(sanitizationReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sanitizationReport
        restSanitizationReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanitizationReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sanitizationReportRepository.count();
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

    protected SanitizationReport getPersistedSanitizationReport(SanitizationReport sanitizationReport) {
        return sanitizationReportRepository.findById(sanitizationReport.getId()).orElseThrow();
    }

    protected void assertPersistedSanitizationReportToMatchAllProperties(SanitizationReport expectedSanitizationReport) {
        assertSanitizationReportAllPropertiesEquals(expectedSanitizationReport, getPersistedSanitizationReport(expectedSanitizationReport));
    }

    protected void assertPersistedSanitizationReportToMatchUpdatableProperties(SanitizationReport expectedSanitizationReport) {
        assertSanitizationReportAllUpdatablePropertiesEquals(
            expectedSanitizationReport,
            getPersistedSanitizationReport(expectedSanitizationReport)
        );
    }
}
