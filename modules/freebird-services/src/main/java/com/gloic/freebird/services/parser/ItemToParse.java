package com.gloic.freebird.services.parser;

import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.persistence.model.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ItemToParse is built from parsing and be used in scraping.
 * Contains all extracted data
 *
 * @author gloic
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemToParse {
    private String filename;
    private String url;
    private Long size;
    private Site site;
    private String parentUrl;
    private LocalDateTime lastModified;

    private Integer year;
    private Codec codec = Codec.UNKNOWN;
    private Language language = Language.UNKNOWN;
    private Quality quality = Quality.UNKNOWN;
    private String potentialTitle;

    private int episodeNum;
    private int seasonNum;

    private boolean isIdentification;
}