package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author gloic
 */
public interface LinkRepository extends JpaRepository<Link, Long> {

    Collection<Link> findBySiteAndParentUrl(Site site, String url);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Link l SET l.fileStatus = com.gloic.freebird.commons.enumerations.FileStatus.PENDING WHERE l.site =:site")
    void flagAsPendingBySite(@Param("site") Site site);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Link l SET l.fileStatus = com.gloic.freebird.commons.enumerations.FileStatus.ONLINE WHERE l.site =:site AND l.parentUrl = :parentUrl")
    void flagAsOnlineBySiteAndParentUrl(@Param("site") Site site, @Param("parentUrl") String parentUrl);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Link l WHERE l.site =:site AND l.fileStatus = com.gloic.freebird.commons.enumerations.FileStatus.PENDING")
    void deletePendingBySite(@Param("site") Site site);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Link l WHERE l.site =:site")
    void deleteAllBySite(@Param("site") Site site);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Link l WHERE l.site =:site AND l.category = com.gloic.freebird.commons.enumerations.MediaCategory.UNKNOWN")
    void deleteUnknownsBySite(@Param("site") Site site);
}
