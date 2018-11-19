package com.gloic.freebird.commons.enumerations;

/**
 * Possibles Language for a Link
 *
 * @author gloic
 */
public enum Language {
    MULTI("MULTI"),
    VOSTFR("VOSTFR"),
    FRENCH("TRUEFRENCH", "FRENCH", "VFF", "VFI", " FR ", ".FR."),
    VFQ("VFQ"),
    ENGLISH("ENGLISH", "ENG"),
    UNKNOWN;

    public String[] stringValues;

    Language(String... value) {
        this.stringValues = value;
    }
}
