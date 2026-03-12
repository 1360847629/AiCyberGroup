package org.cyberwarriors.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SanitizationReport.
 */
@Entity
@Table(name = "sanitization_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanitizationReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_date", nullable = false)
    private Instant reportDate;

    @Lob
    @Column(name = "details")
    private String details;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    @JsonIgnoreProperties(value = { "user", "jobReport", "jobExecutionReport", "sanitizationReport" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private JobRequest jobRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SanitizationReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReportDate() {
        return this.reportDate;
    }

    public SanitizationReport reportDate(Instant reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(Instant reportDate) {
        this.reportDate = reportDate;
    }

    public String getDetails() {
        return this.details;
    }

    public SanitizationReport details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getIsSuccessful() {
        return this.isSuccessful;
    }

    public SanitizationReport isSuccessful(Boolean isSuccessful) {
        this.setIsSuccessful(isSuccessful);
        return this;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public JobRequest getJobRequest() {
        return this.jobRequest;
    }

    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public SanitizationReport jobRequest(JobRequest jobRequest) {
        this.setJobRequest(jobRequest);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SanitizationReport user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SanitizationReport)) {
            return false;
        }
        return getId() != null && getId().equals(((SanitizationReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanitizationReport{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", isSuccessful='" + getIsSuccessful() + "'" +
            "}";
    }
}
