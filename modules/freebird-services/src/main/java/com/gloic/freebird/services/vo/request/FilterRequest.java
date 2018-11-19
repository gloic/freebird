package com.gloic.freebird.services.vo.request;

import lombok.Data;

/**
 * Request sent when the user filter in Movies or TvShows
 *
 * @author gloic
 */
@Data
public class FilterRequest {
    private Long genreId;
    private String orderBy;
}
