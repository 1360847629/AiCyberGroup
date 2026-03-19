package org.cyberwarriors.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cyberwarriors.repository.JobRequestRepository;
import org.cyberwarriors.service.JobRequestService;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.cyberwarriors.domain.JobRequest}.
 */
@RestController
@RequestMapping("/api/job-requests")
public class JobRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(JobRequestResource.class);

    private static final String ENTITY_NAME = "jobRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    public JobRequestResource(JobRequestService jobRequestService, JobRequestRepository jobRequestRepository) {
        this.jobRequestService = jobRequestService;
        this.jobRequestRepository = jobRequestRepository;
    }

    /**
     * {@code POST  /job-requests} : Create a new jobRequest.
     *
     * @param jobRequestDTO the jobRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new jobRequestDTO, or with status {@code 400 (Bad Request)}
     *         if the jobRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JobRequestDTO> createJobRequest(@Valid @RequestBody JobRequestDTO jobRequestDTO) throws URISyntaxException {
        LOG.debug("REST request to save JobRequest : {}", jobRequestDTO);
        if (jobRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        jobRequestDTO = jobRequestService.save(jobRequestDTO);
        return ResponseEntity.created(new URI("/api/job-requests/" + jobRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, jobRequestDTO.getId().toString()))
            .body(jobRequestDTO);
    }

    /**
     * {@code PUT  /job-requests/:id} : Updates an existing jobRequest.
     *
     * @param id            the id of the jobRequestDTO to save.
     * @param jobRequestDTO the jobRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated jobRequestDTO, or with status {@code 400 (Bad Request)}
     *         if the jobRequestDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the jobRequestDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobRequestDTO> updateJobRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobRequestDTO jobRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JobRequest : {}, {}", id, jobRequestDTO);
        if (jobRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        jobRequestDTO = jobRequestService.update(jobRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRequestDTO.getId().toString()))
            .body(jobRequestDTO);
    }

    /**
     * {@code PATCH  /job-requests/:id} : Partial updates given fields of an
     * existing jobRequest, field will ignore if it is null
     *
     * @param id            the id of the jobRequestDTO to save.
     * @param jobRequestDTO the jobRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated jobRequestDTO, or with status {@code 400 (Bad Request)}
     *         if the jobRequestDTO is not valid, or with status
     *         {@code 404 (Not Found)} if the jobRequestDTO is not found, or with
     *         status {@code 500 (Internal Server Error)} if the jobRequestDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobRequestDTO> partialUpdateJobRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobRequestDTO jobRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JobRequest partially : {}, {}", id, jobRequestDTO);
        if (jobRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobRequestDTO> result = jobRequestService.partialUpdate(jobRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-requests} : get all the jobRequests.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @param filter    the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of jobRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JobRequestDTO>> getAllJobRequests(
        @RequestParam(name = "userId", required = false) Integer userId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of JobRequests for user : {}", userId);
        if ("jobreport-is-null".equals(filter)) {
            LOG.debug("REST request to get all JobRequests where jobReport is null");
            return new ResponseEntity<>(jobRequestService.findAllWhereJobReportIsNull(), HttpStatus.OK);
        }

        if ("jobexecutionreport-is-null".equals(filter)) {
            LOG.debug("REST request to get all JobRequests where jobExecutionReport is null");
            return new ResponseEntity<>(jobRequestService.findAllWhereJobExecutionReportIsNull(), HttpStatus.OK);
        }

        if ("sanitizationreport-is-null".equals(filter)) {
            LOG.debug("REST request to get all JobRequests where sanitizationReport is null");
            return new ResponseEntity<>(jobRequestService.findAllWhereSanitizationReportIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of JobRequests");
        Page<JobRequestDTO> page;

        if (eagerload) {
            if (userId != null) {
                page = jobRequestService.findAllByUserId(userId, pageable);
            } else page = jobRequestService.findAllWithEagerRelationships(pageable);
        } else {
            page = jobRequestService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-requests/:id} : get the "id" jobRequest.
     *
     * @param id the id of the jobRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the jobRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobRequestDTO> getJobRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get JobRequest : {}", id);
        Optional<JobRequestDTO> jobRequestDTO = jobRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobRequestDTO);
    }

    /**
     * {@code DELETE  /job-requests/:id} : delete the "id" jobRequest.
     *
     * @param id the id of the jobRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete JobRequest : {}", id);
        jobRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
