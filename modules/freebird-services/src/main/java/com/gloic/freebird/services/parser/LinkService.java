package com.gloic.freebird.services.parser;

import com.gloic.freebird.commons.enumerations.FileStatus;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.persistence.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Manage Links
 *
 * @author gloic
 */
@Service
public class LinkService {

    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    /**
     * Retrieve all file which have the given parent url for the given site
     *
     * @param site
     * @param url
     * @return a collection of links
     */
    public Collection<Link> findBySiteAndParentUrl(Site site, String url) {
        return linkRepository.findBySiteAndParentUrl(site, url);
    }

    /**
     * Mark the given media as ONLINE
     *
     * @param link
     */
    public void setOnline(Link link) {
        link.setFileStatus(FileStatus.ONLINE);
        linkRepository.save(link);
    }

    /**
     * Mark all files linked to given site as ONLINE for the given parent url
     *
     * @param site
     * @param parentUrl
     */
    public void flagAsOnlineBySiteAndParentUrls(Site site, String parentUrl) {
        linkRepository.flagAsOnlineBySiteAndParentUrl(site, parentUrl);
        ;
    }

    /**
     * Mark all files linked to the given site as PENDING
     *
     * @param site
     */
    public void flagAsPendingBySite(Site site) {
        linkRepository.flagAsPendingBySite(site);
    }

    public void deleteUnknownsBySite(Site site) {
        linkRepository.deleteUnknownsBySite(site);
    }

    /**
     * DELETE all PENDING files linked to the given site
     *
     * @param site
     */
    public void deletePendingBySite(Site site) {
        linkRepository.deletePendingBySite(site);
    }

    /**
     * DELETE all files linked to the given site
     *
     * @param site
     */
    public void deleteAllBySite(Site site) {
        linkRepository.deleteAllBySite(site);
    }
}
