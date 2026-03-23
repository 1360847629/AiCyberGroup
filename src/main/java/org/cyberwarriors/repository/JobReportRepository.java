package org.cyberwarriors.repository;

import java.util.List;
import java.util.Optional;
import org.cyberwarriors.domain.JobReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JobReport entity.
 */
@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Long> {
    @Query("select jobReport from JobReport jobReport where jobReport.user.login = ?#{authentication.name}")
    List<JobReport> findByUserIsCurrentUser();

    default Optional<JobReport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<JobReport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<JobReport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jobReport from JobReport jobReport left join fetch jobReport.user",
        countQuery = "select count(jobReport) from JobReport jobReport"
    )
    Page<JobReport> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jobReport from JobReport jobReport left join fetch jobReport.user")
    List<JobReport> findAllWithToOneRelationships();

    @Query("select jobReport from JobReport jobReport left join fetch jobReport.user where jobReport.id =:id")
    Optional<JobReport> findOneWithToOneRelationships(@Param("id") Long id);

    Page<JobReport> findAllByUserId(Integer userId, Pageable pageable);
}
