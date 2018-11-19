package com.gloic.freebird.commons.enumerations;

/**
 * Possibles Codecs for a Link
 *
 * @author gloic
 */
public enum Codec {
    CODEC_x264("x264", "H264", "H.264"),
    CODEC_x265("x265", "H265", "H.265"),
    CODEC_XVID("XVID"),
    UNKNOWN("UNKNOWN");

    public String[] stringValues;

    Codec(String... value) {
        this.stringValues = value;
    }
}
