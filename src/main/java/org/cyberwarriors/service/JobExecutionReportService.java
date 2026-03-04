package org.cyberwarriors.service;

import java.util.Optional;
import org.cyberwarriors.domain.JobExecutionReport;
import org.cyberwarriors.repository.JobExecutionReportRepository;
import org.cyberwarriors.service.dto.JobExecutionReportDTO;
import org.cyberwarriors.service.mapper.JobExecutionReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.cyberwarriors.domain.JobExecutionReport}.
 */
@Service
@Transactional
public class JobExecutionReportService {

    private static final Logger LOG = LoggerFactory.getLogger(JobExecutionReportService.class);

    private final JobExecutionReportRepository jobExecutionReportRepository;

    private final JobExecutionReportMapper jobExecutionReportMapper;

    public JobExecutionReportService(
        JobExecutionReportRepository jobExecutionReportRepository,
        JobExecutionReportMapper jobExecutionReportMapper
    ) {
        this.jobExecutionReportRepository = jobExecutionReportRepository;
        this.jobExecutionReportMapper = jobExecutionReportMapper;
    }

    /**
     * Save a jobExecutionReport.
     *
     * @param jobExecutionReportDTO the entity to save.
     * @return the persisted entity.
     */
    public JobExecutionReportDTO save(JobExecutionReportDTO jobExecutionReportDTO) {
        LOG.debug("Request to save JobExecutionReport : {}", jobExecutionReportDTO);
        JobExecutionReport jobExecutionReport = jobExecutionReportMapper.toEntity(jobExecutionReportDTO);
        jobExecutionReport = jobExecutionReportRepository.save(jobExecutionReport);
        return jobExecutionReportMapper.toDto(jobExecutionReport);
    }

    /**
     * Update a jobExecutionReport.
     *
     * @param jobExecutionReportDTO the entity to save.
     * @return the persisted entity.
     */
    public JobExecutionReportDTO update(JobExecutionReportDTO jobExecutionReportDTO) {
        LOG.debug("Request to update JobExecutionReport : {}", jobExecutionReportDTO);
        JobExecutionReport jobExecutionReport = jobExecutionReportMapper.toEntity(jobExecutionReportDTO);
        jobExecutionReport = jobExecutionReportRepository.save(jobExecutionReport);
        return jobExecutionReportMapper.toDto(jobExecutionReport);
    }

    /**
     * Partially update a jobExecutionReport.
     *
     * @param jobExecutionReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobExecutionReportDTO> partialUpdate(JobExecutionReportDTO jobExecutionReportDTO) {
        LOG.debug("Request to partially update JobExecutionReport : {}", jobExecutionReportDTO);

        return jobExecutionReportRepository
            .findById(jobExecutionReportDTO.getId())
            .map(existingJobExecutionReport -> {
                jobExecutionReportMapper.partialUpdate(existingJobExecutionReport, jobExecutionReportDTO);

                return existingJobExecutionReport;
            })
            .map(jobExecutionReportRepository::save)
            .map(jobExecutionReportMapper::toDto);
    }

    /**
     * Get all the jobExecutionReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobExecutionReportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all JobExecutionReports");
        return jobExecutionReportRepository.findAll(pageable).map(jobExecutionReportMapper::toDto);
    }

    /**
     * Get all the jobExecutionReports with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JobExecutionReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jobExecutionReportRepository.findAllWithEagerRelationships(pageable).map(jobExecutionReportMapper::toDto);
    }

    /**
     * Get one jobExecutionReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobExecutionReportDTO> findOne(Long id) {
        LOG.debug("Request to get JobExecutionReport : {}", id);
        return jobExecutionReportRepository.findOneWithEagerRelationships(id).map(jobExecutionReportMapper::toDto);
    }

    /**
     * Delete the jobExecutionReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete JobExecutionReport : {}", id);
        jobExecutionReportRepository.deleteById(id);
    }
}
