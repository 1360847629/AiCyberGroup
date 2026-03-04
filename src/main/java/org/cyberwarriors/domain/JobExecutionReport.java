package org.cyberwarriors.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.cyberwarriors.domain.enumeration.Status;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobExecutionReport.
 */
@Entity
@Table(name = "job_execution_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobExecutionReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "execution_node")
    private String executionNode;

    @Lob
    @Column(name = "execution_log")
    private String executionLog;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @JsonIgnoreProperties(value = { "user", "jobReport", "jobExecutionReport" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private JobRequest jobRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobExecutionReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public JobExecutionReport startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public JobExecutionReport endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getExecutionNode() {
        return this.executionNode;
    }

    public JobExecutionReport executionNode(String executionNode) {
        this.setExecutionNode(executionNode);
        return this;
    }

    public void setExecutionNode(String executionNode) {
        this.executionNode = executionNode;
    }

    public String getExecutionLog() {
        return this.executionLog;
    }

    public JobExecutionReport executionLog(String executionLog) {
        this.setExecutionLog(executionLog);
        return this;
    }

    public void setExecutionLog(String executionLog) {
        this.executionLog = executionLog;
    }

    public Status getStatus() {
        return this.status;
    }

    public JobExecutionReport status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public JobRequest getJobRequest() {
        return this.jobRequest;
    }

    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public JobExecutionReport jobRequest(JobRequest jobRequest) {
        this.setJobRequest(jobRequest);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JobExecutionReport user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobExecutionReport)) {
            return false;
        }
        return getId() != null && getId().equals(((JobExecutionReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobExecutionReport{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", executionNode='" + getExecutionNode() + "'" +
            ", executionLog='" + getExecutionLog() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
