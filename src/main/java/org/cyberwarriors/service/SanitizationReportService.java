package org.cyberwarriors.service;

import java.util.Optional;
import org.cyberwarriors.domain.SanitizationReport;
import org.cyberwarriors.repository.SanitizationReportRepository;
import org.cyberwarriors.service.dto.SanitizationReportDTO;
import org.cyberwarriors.service.mapper.SanitizationReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.cyberwarriors.domain.SanitizationReport}.
 */
@Service
@Transactional
public class SanitizationReportService {

    private static final Logger LOG = LoggerFactory.getLogger(SanitizationReportService.class);

    private final SanitizationReportRepository sanitizationReportRepository;

    private final SanitizationReportMapper sanitizationReportMapper;

    public SanitizationReportService(
        SanitizationReportRepository sanitizationReportRepository,
        SanitizationReportMapper sanitizationReportMapper
    ) {
        this.sanitizationReportRepository = sanitizationReportRepository;
        this.sanitizationReportMapper = sanitizationReportMapper;
    }

    /**
     * Save a sanitizationReport.
     *
     * @param sanitizationReportDTO the entity to save.
     * @return the persisted entity.
     */
    public SanitizationReportDTO save(SanitizationReportDTO sanitizationReportDTO) {
        LOG.debug("Request to save SanitizationReport : {}", sanitizationReportDTO);
        SanitizationReport sanitizationReport = sanitizationReportMapper.toEntity(sanitizationReportDTO);
        sanitizationReport = sanitizationReportRepository.save(sanitizationReport);
        return sanitizationReportMapper.toDto(sanitizationReport);
    }

    /**
     * Update a sanitizationReport.
     *
     * @param sanitizationReportDTO the entity to save.
     * @return the persisted entity.
     */
    public SanitizationReportDTO update(SanitizationReportDTO sanitizationReportDTO) {
        LOG.debug("Request to update SanitizationReport : {}", sanitizationReportDTO);
        SanitizationReport sanitizationReport = sanitizationReportMapper.toEntity(sanitizationReportDTO);
        sanitizationReport = sanitizationReportRepository.save(sanitizationReport);
        return sanitizationReportMapper.toDto(sanitizationReport);
    }

    /**
     * Partially update a sanitizationReport.
     *
     * @param sanitizationReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SanitizationReportDTO> partialUpdate(SanitizationReportDTO sanitizationReportDTO) {
        LOG.debug("Request to partially update SanitizationReport : {}", sanitizationReportDTO);

        return sanitizationReportRepository
            .findById(sanitizationReportDTO.getId())
            .map(existingSanitizationReport -> {
                sanitizationReportMapper.partialUpdate(existingSanitizationReport, sanitizationReportDTO);

                return existingSanitizationReport;
            })
            .map(sanitizationReportRepository::save)
            .map(sanitizationReportMapper::toDto);
    }

    /**
     * Get all the sanitizationReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SanitizationReportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SanitizationReports");
        return sanitizationReportRepository.findAll(pageable).map(sanitizationReportMapper::toDto);
    }

    /**
     * Get all the sanitizationReports with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SanitizationReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sanitizationReportRepository.findAllWithEagerRelationships(pageable).map(sanitizationReportMapper::toDto);
    }

    /**
     * Get one sanitizationReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SanitizationReportDTO> findOne(Long id) {
        LOG.debug("Request to get SanitizationReport : {}", id);
        return sanitizationReportRepository.findOneWithEagerRelationships(id).map(sanitizationReportMapper::toDto);
    }

    /**
     * Delete the sanitizationReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SanitizationReport : {}", id);
        sanitizationReportRepository.deleteById(id);
    }
}
