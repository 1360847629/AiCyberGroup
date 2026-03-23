package org.cyberwarriors.service;

import java.util.Optional;
import org.cyberwarriors.domain.JobReport;
import org.cyberwarriors.repository.JobReportRepository;
import org.cyberwarriors.service.dto.JobReportDTO;
import org.cyberwarriors.service.mapper.JobReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.cyberwarriors.domain.JobReport}.
 */
@Service
@Transactional
public class JobReportService {

    private static final Logger LOG = LoggerFactory.getLogger(JobReportService.class);

    private final JobReportRepository jobReportRepository;

    private final JobReportMapper jobReportMapper;

    public JobReportService(JobReportRepository jobReportRepository, JobReportMapper jobReportMapper) {
        this.jobReportRepository = jobReportRepository;
        this.jobReportMapper = jobReportMapper;
    }

    /**
     * Save a jobReport.
     *
     * @param jobReportDTO the entity to save.
     * @return the persisted entity.
     */
    public JobReportDTO save(JobReportDTO jobReportDTO) {
        LOG.debug("Request to save JobReport : {}", jobReportDTO);
        JobReport jobReport = jobReportMapper.toEntity(jobReportDTO);
        jobReport = jobReportRepository.save(jobReport);
        return jobReportMapper.toDto(jobReport);
    }

    /**
     * Update a jobReport.
     *
     * @param jobReportDTO the entity to save.
     * @return the persisted entity.
     */
    public JobReportDTO update(JobReportDTO jobReportDTO) {
        LOG.debug("Request to update JobReport : {}", jobReportDTO);
        JobReport jobReport = jobReportMapper.toEntity(jobReportDTO);
        jobReport = jobReportRepository.save(jobReport);
        return jobReportMapper.toDto(jobReport);
    }

    /**
     * Partially update a jobReport.
     *
     * @param jobReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobReportDTO> partialUpdate(JobReportDTO jobReportDTO) {
        LOG.debug("Request to partially update JobReport : {}", jobReportDTO);

        return jobReportRepository
            .findById(jobReportDTO.getId())
            .map(existingJobReport -> {
                jobReportMapper.partialUpdate(existingJobReport, jobReportDTO);

                return existingJobReport;
            })
            .map(jobReportRepository::save)
            .map(jobReportMapper::toDto);
    }

    /**
     * Get all the jobReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobReportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all JobReports");
        return jobReportRepository.findAll(pageable).map(jobReportMapper::toDto);
    }

    /**
     * Get all the jobReports with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JobReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jobReportRepository.findAllWithEagerRelationships(pageable).map(jobReportMapper::toDto);
    }

    /**
     * Get one jobReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobReportDTO> findOne(Long id) {
        LOG.debug("Request to get JobReport : {}", id);
        return jobReportRepository.findOneWithEagerRelationships(id).map(jobReportMapper::toDto);
    }

    /**
     * Delete the jobReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete JobReport : {}", id);
        jobReportRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<JobReportDTO> findAllByUserId(Integer userId, Pageable pageable) {
        return jobReportRepository.findAllByUserId(userId, pageable).map(jobReportMapper::toDto);
    }
}
