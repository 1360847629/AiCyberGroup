package org.cyberwarriors.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.cyberwarriors.domain.enumeration.Status;

/**
 * A DTO for the {@link org.cyberwarriors.domain.JobExecutionReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobExecutionReportDTO implements Serializable {

    private Long id;

    private Instant startTime;

    private Instant endTime;

    private String executionNode;

    @Lob
    private String executionLog;

    @NotNull
    private Status status;

    private JobRequestDTO jobRequest;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getExecutionNode() {
        return executionNode;
    }

    public void setExecutionNode(String executionNode) {
        this.executionNode = executionNode;
    }

    public String getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if (!(o instanceof JobExecutionReportDTO)) {
            return false;
        }

        JobExecutionReportDTO jobExecutionReportDTO = (JobExecutionReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobExecutionReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobExecutionReportDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", executionNode='" + getExecutionNode() + "'" +
            ", executionLog='" + getExecutionLog() + "'" +
            ", status='" + getStatus() + "'" +
            ", jobRequest=" + getJobRequest() +
            ", user=" + getUser() +
            "}";
    }
}
