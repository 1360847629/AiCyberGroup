package org.cyberwarriors.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cyberwarriors.domain.JobRequestAsserts.*;
import static org.cyberwarriors.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.cyberwarriors.IntegrationTest;
import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.domain.enumeration.FileType;
import org.cyberwarriors.domain.enumeration.Priority;
import org.cyberwarriors.domain.enumeration.RequestType;
import org.cyberwarriors.domain.enumeration.Status;
import org.cyberwarriors.repository.JobRequestRepository;
import org.cyberwarriors.repository.UserRepository;
import org.cyberwarriors.service.JobRequestService;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.mapper.JobRequestMapper;
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
 * Integration tests for the {@link JobRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobRequestResourceIT {

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.QUEUED;

    private static final FileType DEFAULT_FILE_TYPE = FileType.LOG;
    private static final FileType UPDATED_FILE_TYPE = FileType.TXT;

    private static final RequestType DEFAULT_REQUEST_TYPE = RequestType.BATCH;
    private static final RequestType UPDATED_REQUEST_TYPE = RequestType.REALTIME;

    private static final Priority DEFAULT_PRIORITY = Priority.LOW;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private JobRequestRepository jobRequestRepositoryMock;

    @Autowired
    private JobRequestMapper jobRequestMapper;

    @Mock
    private JobRequestService jobRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobRequestMockMvc;

    private JobRequest jobRequest;

    private JobRequest insertedJobRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRequest createEntity() {
        return new JobRequest()
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE)
            .score(DEFAULT_SCORE)
            .status(DEFAULT_STATUS)
            .fileType(DEFAULT_FILE_TYPE)
            .requestType(DEFAULT_REQUEST_TYPE)
            .priority(DEFAULT_PRIORITY)
            .fileName(DEFAULT_FILE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRequest createUpdatedEntity() {
        return new JobRequest()
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .fileType(UPDATED_FILE_TYPE)
            .requestType(UPDATED_REQUEST_TYPE)
            .priority(UPDATED_PRIORITY)
            .fileName(UPDATED_FILE_NAME);
    }

    @BeforeEach
    void initTest() {
        jobRequest = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJobRequest != null) {
            jobRequestRepository.delete(insertedJobRequest);
            insertedJobRequest = null;
        }
    }

    @Test
    @Transactional
    void createJobRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);
        var returnedJobRequestDTO = om.readValue(
            restJobRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JobRequestDTO.class
        );

        // Validate the JobRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJobRequest = jobRequestMapper.toEntity(returnedJobRequestDTO);
        assertJobRequestUpdatableFieldsEquals(returnedJobRequest, getPersistedJobRequest(returnedJobRequest));

        insertedJobRequest = returnedJobRequest;
    }

    @Test
    @Transactional
    void createJobRequestWithExistingId() throws Exception {
        // Create the JobRequest with an existing ID
        jobRequest.setId(1L);
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobRequest.setStatus(null);

        // Create the JobRequest, which fails.
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobRequest.setFileType(null);

        // Create the JobRequest, which fails.
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobRequest.setRequestType(null);

        // Create the JobRequest, which fails.
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobRequest.setPriority(null);

        // Create the JobRequest, which fails.
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobRequests() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        // Get all the jobRequestList
        restJobRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJobRequest() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        // Get the jobRequest
        restJobRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, jobRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobRequest.getId().intValue()))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64.getEncoder().encodeToString(DEFAULT_FILE_CONTENT)))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingJobRequest() throws Exception {
        // Get the jobRequest
        restJobRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobRequest() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobRequest
        JobRequest updatedJobRequest = jobRequestRepository.findById(jobRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJobRequest are not directly saved in db
        em.detach(updatedJobRequest);
        updatedJobRequest
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .fileType(UPDATED_FILE_TYPE)
            .requestType(UPDATED_REQUEST_TYPE)
            .priority(UPDATED_PRIORITY)
            .fileName(UPDATED_FILE_NAME);
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(updatedJobRequest);

        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobRequestToMatchAllProperties(updatedJobRequest);
    }

    @Test
    @Transactional
    void putNonExistingJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .score(UPDATED_SCORE)
            .fileType(UPDATED_FILE_TYPE)
            .requestType(UPDATED_REQUEST_TYPE)
            .priority(UPDATED_PRIORITY)
            .fileName(UPDATED_FILE_NAME);

        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobRequest))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobRequest, jobRequest),
            getPersistedJobRequest(jobRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .fileType(UPDATED_FILE_TYPE)
            .requestType(UPDATED_REQUEST_TYPE)
            .priority(UPDATED_PRIORITY)
            .fileName(UPDATED_FILE_NAME);

        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobRequest))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobRequestUpdatableFieldsEquals(partialUpdatedJobRequest, getPersistedJobRequest(partialUpdatedJobRequest));
    }

    @Test
    @Transactional
    void patchNonExistingJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobRequest.setId(longCount.incrementAndGet());

        // Create the JobRequest
        JobRequestDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jobRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobRequest() throws Exception {
        // Initialize the database
        insertedJobRequest = jobRequestRepository.saveAndFlush(jobRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobRequest
        restJobRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobRequestRepository.count();
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

    protected JobRequest getPersistedJobRequest(JobRequest jobRequest) {
        return jobRequestRepository.findById(jobRequest.getId()).orElseThrow();
    }

    protected void assertPersistedJobRequestToMatchAllProperties(JobRequest expectedJobRequest) {
        assertJobRequestAllPropertiesEquals(expectedJobRequest, getPersistedJobRequest(expectedJobRequest));
    }

    protected void assertPersistedJobRequestToMatchUpdatableProperties(JobRequest expectedJobRequest) {
        assertJobRequestAllUpdatablePropertiesEquals(expectedJobRequest, getPersistedJobRequest(expectedJobRequest));
    }
}
