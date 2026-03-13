package org.cyberwarriors.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.cyberwarriors.domain.enumeration.FileType;
import org.cyberwarriors.domain.enumeration.Priority;
import org.cyberwarriors.domain.enumeration.RequestType;
import org.cyberwarriors.domain.enumeration.Status;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobRequest.
 */
@Entity
@Table(name = "job_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;

    @NotNull
    @Column(name = "file_content_content_type", nullable = false)
    private String fileContentContentType;

    @Column(name = "score")
    private Integer score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnoreProperties(value = { "jobRequest", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jobRequest")
    private JobReport jobReport;

    @JsonIgnoreProperties(value = { "jobRequest", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jobRequest")
    private JobExecutionReport jobExecutionReport;

    @JsonIgnoreProperties(value = { "jobRequest", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jobRequest")
    private SanitizationReport sanitizationReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public JobRequest fileContent(byte[] fileContent) {
        this.setFileContent(fileContent);
        return this;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return this.fileContentContentType;
    }

    public JobRequest fileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
        return this;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public Integer getScore() {
        return this.score;
    }

    public JobRequest score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Status getStatus() {
        return this.status;
    }

    public JobRequest status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public JobRequest fileType(FileType fileType) {
        this.setFileType(fileType);
        return this;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    public JobRequest requestType(RequestType requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public JobRequest priority(Priority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getFileName() {
        return this.fileName;
    }

    public JobRequest fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JobRequest user(User user) {
        this.setUser(user);
        return this;
    }

    public JobReport getJobReport() {
        return this.jobReport;
    }

    public void setJobReport(JobReport jobReport) {
        if (this.jobReport != null) {
            this.jobReport.setJobRequest(null);
        }
        if (jobReport != null) {
            jobReport.setJobRequest(this);
        }
        this.jobReport = jobReport;
    }

    public JobRequest jobReport(JobReport jobReport) {
        this.setJobReport(jobReport);
        return this;
    }

    public JobExecutionReport getJobExecutionReport() {
        return this.jobExecutionReport;
    }

    public void setJobExecutionReport(JobExecutionReport jobExecutionReport) {
        if (this.jobExecutionReport != null) {
            this.jobExecutionReport.setJobRequest(null);
        }
        if (jobExecutionReport != null) {
            jobExecutionReport.setJobRequest(this);
        }
        this.jobExecutionReport = jobExecutionReport;
    }

    public JobRequest jobExecutionReport(JobExecutionReport jobExecutionReport) {
        this.setJobExecutionReport(jobExecutionReport);
        return this;
    }

    public SanitizationReport getSanitizationReport() {
        return this.sanitizationReport;
    }

    public void setSanitizationReport(SanitizationReport sanitizationReport) {
        if (this.sanitizationReport != null) {
            this.sanitizationReport.setJobRequest(null);
        }
        if (sanitizationReport != null) {
            sanitizationReport.setJobRequest(this);
        }
        this.sanitizationReport = sanitizationReport;
    }

    public JobRequest sanitizationReport(SanitizationReport sanitizationReport) {
        this.setSanitizationReport(sanitizationReport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((JobRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRequest{" +
            "id=" + getId() +
            ", fileContent='" + getFileContent() + "'" +
            ", fileContentContentType='" + getFileContentContentType() + "'" +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", fileName='" + getFileName() + "'" +
            "}";
    }
}
