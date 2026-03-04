package org.cyberwarriors.service.mapper;

import org.cyberwarriors.domain.JobExecutionReport;
import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.domain.User;
import org.cyberwarriors.service.dto.JobExecutionReportDTO;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobExecutionReport} and its DTO {@link JobExecutionReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobExecutionReportMapper extends EntityMapper<JobExecutionReportDTO, JobExecutionReport> {
    @Mapping(target = "jobRequest", source = "jobRequest", qualifiedByName = "jobRequestId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    JobExecutionReportDTO toDto(JobExecutionReport s);

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
