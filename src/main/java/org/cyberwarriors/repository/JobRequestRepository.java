package org.cyberwarriors.repository;

import java.util.List;
import java.util.Optional;
import org.cyberwarriors.domain.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JobRequest entity.
 */
@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
    @Query("select jobRequest from JobRequest jobRequest where jobRequest.user.login = ?#{authentication.name}")
    List<JobRequest> findByUserIsCurrentUser();

    default Optional<JobRequest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<JobRequest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<JobRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jobRequest from JobRequest jobRequest left join fetch jobRequest.user",
        countQuery = "select count(jobRequest) from JobRequest jobRequest"
    )
    Page<JobRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jobRequest from JobRequest jobRequest left join fetch jobRequest.user")
    List<JobRequest> findAllWithToOneRelationships();

    @Query("select jobRequest from JobRequest jobRequest left join fetch jobRequest.user where jobRequest.id =:id")
    Optional<JobRequest> findOneWithToOneRelationships(@Param("id") Long id);
}
