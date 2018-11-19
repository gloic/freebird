package com.gloic.freebird.services.scraper;

import java.util.regex.Pattern;

/**
 * Regex for filenames
 *
 * @author gloic
 */
public final class ScraperConstants {

    /**
     * <p>Given string match with pattern of a TVShow:</p>
     * <p>For example, it contains S01E01 or 1x1/p>
     */
    public static final Pattern patternTvShow = Pattern.compile("[Ss]?[^0-9](?<season>[0-9]{1,2})[EeXx](?<episode>[0-9]{1,3})");

    public static final Pattern patternTvShowStart = Pattern.compile("^[Ss]?(?<season>[0-9]{1,2})[EeXx](?<episode>[0-9]{1,3})");

    /**
     * <p>Given string ends with a video extension</p>
     */
    public static final Pattern patternMedia = Pattern.compile("(?i)\\.(avi|mkv|mp4)$");

    /**
     * <p>Given string contains a year</p>
     */
    public static final Pattern patternTitleWithYear = Pattern.compile("(?<year>(?:19|20)+[0-9]{2})[^xX]");

    /**
     * <p>Given string start with a year</p>
     */
    public static final Pattern patternTitleStartWithYear = Pattern.compile("^(?<year>(?:19|20)+[0-9]{2}).*");
}
