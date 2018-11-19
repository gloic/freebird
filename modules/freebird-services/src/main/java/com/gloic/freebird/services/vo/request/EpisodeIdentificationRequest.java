package com.gloic.freebird.services.vo.request;

import lombok.Data;

/**
 * Request sent when the user identifies a single Episode
 *
 * @author gloic
 */
@Data
public final class EpisodeIdentificationRequest {
    private Long mediaId;
    private String showName;
    private int seasonNum;
    private int episodeNum;

    public EpisodeIdentificationRequest() {
    }

    public EpisodeIdentificationRequest(Long mediaId, String showName, int seasonNum, int episodeNum) {
        this.mediaId = mediaId;
        this.showName = showName;
        this.seasonNum = seasonNum;
        this.episodeNum = episodeNum;
    }
}
