package com.gloic.freebird.commons.enumerations;

/**
 * Possibles qualities of a Link
 *
 * @author gloic
 */
public enum Quality {
    UNKNOWN,
    QUALITY_4K("4K"),
    QUALITY_720p("720p"),
    QUALITY_1080p("1080p"),
    QUALITY_DVDRIP("DVDRIP", "DVDSCR"),
    QUALITY_BDRIP("BDRIP", "WEBRIP", "BRRIP"),
    QUALITY_CAM("HDTS", "CAM", "DVDSCREEN", "DVDSCR");

    public String[] stringValues;

    Quality(String... s) {
        this.stringValues = s;
    }
}
