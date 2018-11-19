package com.gloic.freebird.commons.enumerations;

/**
 * Possibles status for a Link.
 * SuppressWarning added because PENDING is used through queries
 *
 * @author gloic
 */
@SuppressWarnings("unused")
public enum FileStatus {
    ONLINE,
    PENDING,
    NEW
}

