package org.cyberwarriors.repository;

import java.util.List;
import java.util.Optional;
import org.cyberwarriors.domain.SanitizationReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SanitizationReport entity.
 */
@Repository
public interface SanitizationReportRepository extends JpaRepository<SanitizationReport, Long> {
    @Query(
        "select sanitizationReport from SanitizationReport sanitizationReport where sanitizationReport.user.login = ?#{authentication.name}"
    )
    List<SanitizationReport> findByUserIsCurrentUser();

    default Optional<SanitizationReport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SanitizationReport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SanitizationReport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sanitizationReport from SanitizationReport sanitizationReport left join fetch sanitizationReport.user",
        countQuery = "select count(sanitizationReport) from SanitizationReport sanitizationReport"
    )
    Page<SanitizationReport> findAllWithToOneRelationships(Pageable pageable);

    @Query("select sanitizationReport from SanitizationReport sanitizationReport left join fetch sanitizationReport.user")
    List<SanitizationReport> findAllWithToOneRelationships();

    @Query(
        "select sanitizationReport from SanitizationReport sanitizationReport left join fetch sanitizationReport.user where sanitizationReport.id =:id"
    )
    Optional<SanitizationReport> findOneWithToOneRelationships(@Param("id") Long id);
}
