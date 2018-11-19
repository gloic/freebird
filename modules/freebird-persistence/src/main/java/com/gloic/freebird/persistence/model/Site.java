package com.gloic.freebird.persistence.model;

import com.gloic.freebird.commons.enumerations.HostType;
import com.gloic.freebird.commons.enumerations.SiteStatus;
import lombok.Data;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gloic
 */
@Entity
@Data
public class Site {
    @Id
    @GeneratedValue
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    private SiteStatus siteStatus;

    private LocalDateTime lastScan;

    private HostType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ignoredUrls", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ignoredUrls")
    private List<String> ignoredUrls = new ArrayList<>();

    public void addIgnoredUrl(String url) {
        ignoredUrls.add(url);
    }
}
