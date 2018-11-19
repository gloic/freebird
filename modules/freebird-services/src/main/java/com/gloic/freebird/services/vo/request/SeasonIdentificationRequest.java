package com.gloic.freebird.services.vo.request;

import lombok.Data;

/**
 * Request object sent by the media identification for a complete season
 *
 * @author gloic
 */
@Data
public final class SeasonIdentificationRequest {
    private Long mediaId;
    private String showName;
    private Integer seasonNum;
}
