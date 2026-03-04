package org.cyberwarriors.service.mapper;

import org.cyberwarriors.domain.JobRequest;
import org.cyberwarriors.domain.User;
import org.cyberwarriors.service.dto.JobRequestDTO;
import org.cyberwarriors.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobRequest} and its DTO {@link JobRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobRequestMapper extends EntityMapper<JobRequestDTO, JobRequest> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    JobRequestDTO toDto(JobRequest s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
