package org.cyberwarriors.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.cyberwarriors.domain.enumeration.FileType;
import org.cyberwarriors.domain.enumeration.Priority;
import org.cyberwarriors.domain.enumeration.RequestType;
import org.cyberwarriors.domain.enumeration.Status;

/**
 * A DTO for the {@link org.cyberwarriors.domain.JobRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobRequestDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;

    private Integer score;

    @NotNull
    private Status status;

    @NotNull
    private FileType fileType;

    @NotNull
    private RequestType requestType;

    @NotNull
    private Priority priority;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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
        if (!(o instanceof JobRequestDTO)) {
            return false;
        }

        JobRequestDTO jobRequestDTO = (JobRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRequestDTO{" +
            "id=" + getId() +
            ", fileContent='" + getFileContent() + "'" +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
