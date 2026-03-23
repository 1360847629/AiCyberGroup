package org.cyberwarriors.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cyberwarriors.repository.SanitizationReportRepository;
import org.cyberwarriors.service.SanitizationReportService;
import org.cyberwarriors.service.dto.SanitizationReportDTO;
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
 * REST controller for managing {@link org.cyberwarriors.domain.SanitizationReport}.
 */
@RestController
@RequestMapping("/api/sanitization-reports")
public class SanitizationReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(SanitizationReportResource.class);

    private static final String ENTITY_NAME = "sanitizationReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanitizationReportService sanitizationReportService;

    private final SanitizationReportRepository sanitizationReportRepository;

    public SanitizationReportResource(
        SanitizationReportService sanitizationReportService,
        SanitizationReportRepository sanitizationReportRepository
    ) {
        this.sanitizationReportService = sanitizationReportService;
        this.sanitizationReportRepository = sanitizationReportRepository;
    }

    /**
     * {@code POST  /sanitization-reports} : Create a new sanitizationReport.
     *
     * @param sanitizationReportDTO the sanitizationReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanitizationReportDTO, or with status {@code 400 (Bad Request)} if the sanitizationReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SanitizationReportDTO> createSanitizationReport(@Valid @RequestBody SanitizationReportDTO sanitizationReportDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SanitizationReport : {}", sanitizationReportDTO);
        if (sanitizationReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new sanitizationReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sanitizationReportDTO = sanitizationReportService.save(sanitizationReportDTO);
        return ResponseEntity.created(new URI("/api/sanitization-reports/" + sanitizationReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sanitizationReportDTO.getId().toString()))
            .body(sanitizationReportDTO);
    }

    /**
     * {@code PUT  /sanitization-reports/:id} : Updates an existing sanitizationReport.
     *
     * @param id                    the id of the sanitizationReportDTO to save.
     * @param sanitizationReportDTO the sanitizationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanitizationReportDTO,
     * or with status {@code 400 (Bad Request)} if the sanitizationReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanitizationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SanitizationReportDTO> updateSanitizationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SanitizationReportDTO sanitizationReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SanitizationReport : {}, {}", id, sanitizationReportDTO);
        if (sanitizationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanitizationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanitizationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sanitizationReportDTO = sanitizationReportService.update(sanitizationReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanitizationReportDTO.getId().toString()))
            .body(sanitizationReportDTO);
    }

    /**
     * {@code PATCH  /sanitization-reports/:id} : Partial updates given fields of an existing sanitizationReport, field will ignore if it is null
     *
     * @param id                    the id of the sanitizationReportDTO to save.
     * @param sanitizationReportDTO the sanitizationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated sanitizationReportDTO, or with status {@code 400 (Bad Request)}
     * if the sanitizationReportDTO is not valid, or with status
     * {@code 404 (Not Found)} if the sanitizationReportDTO is not found, or with
     * status {@code 500 (Internal Server Error)} if the sanitizationReportDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SanitizationReportDTO> partialUpdateSanitizationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SanitizationReportDTO sanitizationReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SanitizationReport partially : {}, {}", id, sanitizationReportDTO);
        if (sanitizationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanitizationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanitizationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SanitizationReportDTO> result = sanitizationReportService.partialUpdate(sanitizationReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanitizationReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sanitization-reports} : get all the sanitizationReports.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @param filter    the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of sanitizationReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SanitizationReportDTO>> getAllSanitizationReports(
        @RequestParam(name = "userId", required = false) Integer userId,
        @ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SanitizationReports");
        Page<SanitizationReportDTO> page;
        if (eagerload) {
            if (userId != null) {
                page = sanitizationReportService.findAllByUserId(userId, pageable);
            } else page = sanitizationReportService.findAllWithEagerRelationships(pageable);
        } else {
            page = sanitizationReportService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanitization-reports/:id} : get the "id" sanitizationReport.
     *
     * @param id the id of the sanitizationReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the sanitizationReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SanitizationReportDTO> getSanitizationReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SanitizationReport : {}", id);
        Optional<SanitizationReportDTO> sanitizationReportDTO = sanitizationReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanitizationReportDTO);
    }

    /**
     * {@code DELETE  /sanitization-reports/:id} : delete the "id" sanitizationReport.
     *
     * @param id the id of the sanitizationReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanitizationReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SanitizationReport : {}", id);
        sanitizationReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
