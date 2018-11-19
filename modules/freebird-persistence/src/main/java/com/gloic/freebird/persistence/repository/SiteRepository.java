package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.commons.enumerations.FileStatus;
import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.persistence.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author gloic
 */
public interface SiteRepository extends JpaRepository<Site, Long> {
    @Query("SELECT count(l.id) FROM Link l JOIN l.site s WHERE s.id = ?1 AND l.category = ?2 AND l.fileStatus = ?3 GROUP BY s.id")
    Integer findBySiteNumberOfLinkByCategoryAndFileStatus(Long id, MediaCategory category, FileStatus fileStatus);

}
