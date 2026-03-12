package org.cyberwarriors.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.cyberwarriors.domain.SanitizationReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanitizationReportDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant reportDate;

    @Lob
    private String details;

    private Boolean isSuccessful;

    private JobRequestDTO jobRequest;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReportDate() {
        return reportDate;
    }

    public void setReportDate(Instant reportDate) {
        this.reportDate = reportDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public JobRequestDTO getJobRequest() {
        return jobRequest;
    }

    public void setJobRequest(JobRequestDTO jobRequest) {
        this.jobRequest = jobRequest;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SanitizationReportDTO)) {
            return false;
        }

        SanitizationReportDTO sanitizationReportDTO = (SanitizationReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sanitizationReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanitizationReportDTO{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", isSuccessful='" + getIsSuccessful() + "'" +
            ", jobRequest=" + getJobRequest() +
            ", user=" + getUser() +
            "}";
    }
}
