package org.cyberwarriors.repository;

import java.util.List;
import java.util.Optional;
import org.cyberwarriors.domain.JobExecutionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JobExecutionReport entity.
 */
@Repository
public interface JobExecutionReportRepository extends JpaRepository<JobExecutionReport, Long> {
    @Query(
        "select jobExecutionReport from JobExecutionReport jobExecutionReport where jobExecutionReport.user.login = ?#{authentication.name}"
    )
    List<JobExecutionReport> findByUserIsCurrentUser();

    default Optional<JobExecutionReport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<JobExecutionReport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<JobExecutionReport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jobExecutionReport from JobExecutionReport jobExecutionReport left join fetch jobExecutionReport.user",
        countQuery = "select count(jobExecutionReport) from JobExecutionReport jobExecutionReport"
    )
    Page<JobExecutionReport> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jobExecutionReport from JobExecutionReport jobExecutionReport left join fetch jobExecutionReport.user")
    List<JobExecutionReport> findAllWithToOneRelationships();

    @Query(
        "select jobExecutionReport from JobExecutionReport jobExecutionReport left join fetch jobExecutionReport.user where jobExecutionReport.id =:id"
    )
    Optional<JobExecutionReport> findOneWithToOneRelationships(@Param("id") Long id);
}
