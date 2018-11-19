package com.gloic.freebird.services.vo.request;

import lombok.Data;

/**
 * Request sent by the movie identification
 *
 * @author gloic
 */
@Data
public final class MovieIdentificationRequest {
    private Long mediaId;
    private String title;
}
