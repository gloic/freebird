package com.gloic.freebird.services.wrapper;

import com.gloic.freebird.commons.enumerations.SiteStatus;
import com.gloic.freebird.persistence.model.Site;
import lombok.Data;

import java.time.ZoneId;
import java.util.Date;

/**
 * SiteVO contains a Site and its stats
 *
 * @author gloic
 */
@Data
public class SiteWrapper {
    private Long id;
    private String url;

    private Date lastScan;

    private SiteStatus siteStatus;

    private Integer numberEpisodeOnline;
    private Integer numberMovieOnline;
    private Integer numberUnknownOnline;

    private Integer numberEpisodeNew;
    private Integer numberMovieNew;
    private Integer numberUnknownNew;

    public SiteWrapper(Site site) {
        this.id = site.getId();
        this.url = site.getUrl();
        this.lastScan = site.getLastScan() != null ? Date.from(site.getLastScan().atZone(ZoneId.systemDefault()).toInstant()) : null;
        this.siteStatus = site.getSiteStatus();
    }
}
