package org.cyberwarriors.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cyberwarriors.repository.JobExecutionReportRepository;
import org.cyberwarriors.service.JobExecutionReportService;
import org.cyberwarriors.service.dto.JobExecutionReportDTO;
import org.cyberwarriors.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
 * REST controller for managing {@link org.cyberwarriors.domain.JobExecutionReport}.
 */
@RestController
@RequestMapping("/api/job-execution-reports")
public class JobExecutionReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(JobExecutionReportResource.class);

    private static final String ENTITY_NAME = "jobExecutionReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobExecutionReportService jobExecutionReportService;

    private final JobExecutionReportRepository jobExecutionReportRepository;

    public JobExecutionReportResource(
        JobExecutionReportService jobExecutionReportService,
        JobExecutionReportRepository jobExecutionReportRepository
    ) {
        this.jobExecutionReportService = jobExecutionReportService;
        this.jobExecutionReportRepository = jobExecutionReportRepository;
    }

    /**
     * {@code POST  /job-execution-reports} : Create a new jobExecutionReport.
     *
     * @param jobExecutionReportDTO the jobExecutionReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobExecutionReportDTO, or with status {@code 400 (Bad Request)} if the jobExecutionReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JobExecutionReportDTO> createJobExecutionReport(@Valid @RequestBody JobExecutionReportDTO jobExecutionReportDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save JobExecutionReport : {}", jobExecutionReportDTO);
        if (jobExecutionReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobExecutionReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobExecutionReportDTO = jobExecutionReportService.save(jobExecutionReportDTO);
        return ResponseEntity.created(new URI("/api/job-execution-reports/" + jobExecutionReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobExecutionReportDTO.getId().toString()))
            .body(jobExecutionReportDTO);
    }

    /**
     * {@code PUT  /job-execution-reports/:id} : Updates an existing jobExecutionReport.
     *
     * @param id                    the id of the jobExecutionReportDTO to save.
     * @param jobExecutionReportDTO the jobExecutionReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobExecutionReportDTO,
     * or with status {@code 400 (Bad Request)} if the jobExecutionReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobExecutionReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobExecutionReportDTO> updateJobExecutionReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobExecutionReportDTO jobExecutionReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JobExecutionReport : {}, {}", id, jobExecutionReportDTO);
        if (jobExecutionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobExecutionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobExecutionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobExecutionReportDTO = jobExecutionReportService.update(jobExecutionReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobExecutionReportDTO.getId().toString()))
            .body(jobExecutionReportDTO);
    }

    /**
     * {@code PATCH  /job-execution-reports/:id} : Partial updates given fields of an existing jobExecutionReport, field will ignore if it is null
     *
     * @param id                    the id of the jobExecutionReportDTO to save.
     * @param jobExecutionReportDTO the jobExecutionReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated jobExecutionReportDTO, or with status {@code 400 (Bad Request)}
     * if the jobExecutionReportDTO is not valid, or with status
     * {@code 404 (Not Found)} if the jobExecutionReportDTO is not found, or with
     * status {@code 500 (Internal Server Error)} if the jobExecutionReportDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobExecutionReportDTO> partialUpdateJobExecutionReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobExecutionReportDTO jobExecutionReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JobExecutionReport partially : {}, {}", id, jobExecutionReportDTO);
        if (jobExecutionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobExecutionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobExecutionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobExecutionReportDTO> result = jobExecutionReportService.partialUpdate(jobExecutionReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobExecutionReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-execution-reports} : get all the jobExecutionReports.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @param filter    the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of jobExecutionReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JobExecutionReportDTO>> getAllJobExecutionReports(
        @RequestParam(name = "userId", required = false) Integer userId,
        @ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of JobExecutionReports");
        Page<JobExecutionReportDTO> page;

        if (eagerload) {
            if (userId != null) {
                page = jobExecutionReportService.findAllByUserId(userId, pageable);
            }
            page = jobExecutionReportService.findAllWithEagerRelationships(pageable);
        } else {
            page = jobExecutionReportService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-execution-reports/:id} : get the "id" jobExecutionReport.
     *
     * @param id the id of the jobExecutionReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the jobExecutionReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobExecutionReportDTO> getJobExecutionReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get JobExecutionReport : {}", id);
        Optional<JobExecutionReportDTO> jobExecutionReportDTO = jobExecutionReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobExecutionReportDTO);
    }

    /**
     * {@code DELETE  /job-execution-reports/:id} : delete the "id" jobExecutionReport.
     *
     * @param id the id of the jobExecutionReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobExecutionReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete JobExecutionReport : {}", id);
        jobExecutionReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
