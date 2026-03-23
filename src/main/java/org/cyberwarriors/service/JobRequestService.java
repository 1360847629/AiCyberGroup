package org.cyberwarriors.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.repository.JobRequestRepository;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.mapper.JobRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.cyberwarriors.domain.JobRequest}.
 */
@Service
@Transactional
public class JobRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(JobRequestService.class);

    private final JobRequestRepository jobRequestRepository;

    private final JobRequestMapper jobRequestMapper;

    public JobRequestService(JobRequestRepository jobRequestRepository, JobRequestMapper jobRequestMapper) {
        this.jobRequestRepository = jobRequestRepository;
        this.jobRequestMapper = jobRequestMapper;
    }

    /**
     * Save a jobRequest.
     *
     * @param jobRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public JobRequestDTO save(JobRequestDTO jobRequestDTO) {
        LOG.debug("Request to save JobRequest : {}", jobRequestDTO);
        JobRequest jobRequest = jobRequestMapper.toEntity(jobRequestDTO);
        jobRequest = jobRequestRepository.save(jobRequest);
        return jobRequestMapper.toDto(jobRequest);
    }

    /**
     * Update a jobRequest.
     *
     * @param jobRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public JobRequestDTO update(JobRequestDTO jobRequestDTO) {
        LOG.debug("Request to update JobRequest : {}", jobRequestDTO);
        JobRequest jobRequest = jobRequestMapper.toEntity(jobRequestDTO);
        jobRequest = jobRequestRepository.save(jobRequest);
        return jobRequestMapper.toDto(jobRequest);
    }

    /**
     * Partially update a jobRequest.
     *
     * @param jobRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobRequestDTO> partialUpdate(JobRequestDTO jobRequestDTO) {
        LOG.debug("Request to partially update JobRequest : {}", jobRequestDTO);

        return jobRequestRepository
            .findById(jobRequestDTO.getId())
            .map(existingJobRequest -> {
                jobRequestMapper.partialUpdate(existingJobRequest, jobRequestDTO);

                return existingJobRequest;
            })
            .map(jobRequestRepository::save)
            .map(jobRequestMapper::toDto);
    }

    /**
     * Get all the jobRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobRequestDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all JobRequests");
        return jobRequestRepository.findAll(pageable).map(jobRequestMapper::toDto);
    }

    /**
     * Get all the jobRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JobRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jobRequestRepository.findAllWithEagerRelationships(pageable).map(jobRequestMapper::toDto);
    }

    /**
     * Get all the jobRequests where JobReport is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobRequestDTO> findAllWhereJobReportIsNull() {
        LOG.debug("Request to get all jobRequests where JobReport is null");
        return StreamSupport.stream(jobRequestRepository.findAll().spliterator(), false)
            .filter(jobRequest -> jobRequest.getJobReport() == null)
            .map(jobRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the jobRequests where JobExecutionReport is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobRequestDTO> findAllWhereJobExecutionReportIsNull() {
        LOG.debug("Request to get all jobRequests where JobExecutionReport is null");
        return StreamSupport.stream(jobRequestRepository.findAll().spliterator(), false)
            .filter(jobRequest -> jobRequest.getJobExecutionReport() == null)
            .map(jobRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the jobRequests where SanitizationReport is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobRequestDTO> findAllWhereSanitizationReportIsNull() {
        LOG.debug("Request to get all jobRequests where SanitizationReport is null");
        return StreamSupport.stream(jobRequestRepository.findAll().spliterator(), false)
            .filter(jobRequest -> jobRequest.getSanitizationReport() == null)
            .map(jobRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one jobRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobRequestDTO> findOne(Long id) {
        LOG.debug("Request to get JobRequest : {}", id);
        return jobRequestRepository.findOneWithEagerRelationships(id).map(jobRequestMapper::toDto);
    }

    /**
     * Delete the jobRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete JobRequest : {}", id);
        jobRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<JobRequestDTO> findAllByUserId(Integer userId, Pageable pageable) {
        return jobRequestRepository.findAllByUserId(userId, pageable).map(jobRequestMapper::toDto);
    }
}
