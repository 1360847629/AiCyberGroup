package org.cyberwarriors.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cyberwarriors.repository.JobReportRepository;
import org.cyberwarriors.service.JobReportService;
import org.cyberwarriors.service.dto.JobReportDTO;
import org.cyberwarriors.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.cyberwarriors.domain.JobReport}.
 */
@RestController
@RequestMapping("/api/job-reports")
public class JobReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(JobReportResource.class);

    private static final String ENTITY_NAME = "jobReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobReportService jobReportService;

    private final JobReportRepository jobReportRepository;

    public JobReportResource(JobReportService jobReportService, JobReportRepository jobReportRepository) {
        this.jobReportService = jobReportService;
        this.jobReportRepository = jobReportRepository;
    }

    /**
     * {@code POST  /job-reports} : Create a new jobReport.
     *
     * @param jobReportDTO the jobReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobReportDTO, or with status {@code 400 (Bad Request)} if the jobReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JobReportDTO> createJobReport(@Valid @RequestBody JobReportDTO jobReportDTO) throws URISyntaxException {
        LOG.debug("REST request to save JobReport : {}", jobReportDTO);
        if (jobReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobReportDTO = jobReportService.save(jobReportDTO);
        return ResponseEntity.created(new URI("/api/job-reports/" + jobReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobReportDTO.getId().toString()))
            .body(jobReportDTO);
    }

    /**
     * {@code PUT  /job-reports/:id} : Updates an existing jobReport.
     *
     * @param id the id of the jobReportDTO to save.
     * @param jobReportDTO the jobReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobReportDTO,
     * or with status {@code 400 (Bad Request)} if the jobReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobReportDTO> updateJobReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobReportDTO jobReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JobReport : {}, {}", id, jobReportDTO);
        if (jobReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobReportDTO = jobReportService.update(jobReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobReportDTO.getId().toString()))
            .body(jobReportDTO);
    }

    /**
     * {@code PATCH  /job-reports/:id} : Partial updates given fields of an existing jobReport, field will ignore if it is null
     *
     * @param id the id of the jobReportDTO to save.
     * @param jobReportDTO the jobReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobReportDTO,
     * or with status {@code 400 (Bad Request)} if the jobReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobReportDTO> partialUpdateJobReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobReportDTO jobReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JobReport partially : {}, {}", id, jobReportDTO);
        if (jobReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobReportDTO> result = jobReportService.partialUpdate(jobReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-reports} : get all the jobReports.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JobReportDTO>> getAllJobReports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of JobReports");
        Page<JobReportDTO> page;
        if (eagerload) {
            page = jobReportService.findAllWithEagerRelationships(pageable);
        } else {
            page = jobReportService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-reports/:id} : get the "id" jobReport.
     *
     * @param id the id of the jobReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobReportDTO> getJobReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get JobReport : {}", id);
        Optional<JobReportDTO> jobReportDTO = jobReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobReportDTO);
    }

    /**
     * {@code DELETE  /job-reports/:id} : delete the "id" jobReport.
     *
     * @param id the id of the jobReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete JobReport : {}", id);
        jobReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
