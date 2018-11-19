package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.persistence.model.UnknownMedia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gloic
 */
public interface UnknownMediaRepository extends JpaRepository<UnknownMedia, Long> {

    List<UnknownMedia> findAllByLinksCategoryOrderByLinksFileNameAsc(MediaCategory mediaCategory, Pageable pageable);

    @Transactional
    void deleteByLinksParentUrl(String parentUrl);

    List<UnknownMedia> findDistinctByLinksSiteAndLinksParentUrl(Site site, String parentUrl);
}