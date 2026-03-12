package org.cyberwarriors.service.mapper;

import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.domain.SanitizationReport;
import org.cyberwarriors.domain.User;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.dto.SanitizationReportDTO;
import org.cyberwarriors.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SanitizationReport} and its DTO {@link SanitizationReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface SanitizationReportMapper extends EntityMapper<SanitizationReportDTO, SanitizationReport> {
    @Mapping(target = "jobRequest", source = "jobRequest", qualifiedByName = "jobRequestId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    SanitizationReportDTO toDto(SanitizationReport s);

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
