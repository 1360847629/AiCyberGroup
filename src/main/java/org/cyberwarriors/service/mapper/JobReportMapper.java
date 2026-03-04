package org.cyberwarriors.service.mapper;

import org.cyberwarriors.domain.JobReport;
import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.domain.User;
import org.cyberwarriors.service.dto.JobReportDTO;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobReport} and its DTO {@link JobReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobReportMapper extends EntityMapper<JobReportDTO, JobReport> {
    @Mapping(target = "jobRequest", source = "jobRequest", qualifiedByName = "jobRequestId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    JobReportDTO toDto(JobReport s);

    @Named("jobRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JobRequestDTO toDtoJobRequestId(JobRequest jobRequest);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
