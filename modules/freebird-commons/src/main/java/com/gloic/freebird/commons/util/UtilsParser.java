package com.gloic.freebird.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class convert string into proper data
 *
 * @author gloic
 */
@Slf4j
public final class UtilsParser {

    /**
     * Converts a date in string format into a Calendar object.
     * It tries some patterns and Locales.
     *
     * @param dateInString
     * @return
     */
    public static LocalDateTime dateStringToDate(String dateInString) {
        if (StringUtils.isEmpty(dateInString)) return null;

        LocalDateTime formattedDate = null;

        Map<Pattern, String> patterns = new HashMap<>();
        patterns.put(Pattern.compile("\\d{2}-\\w{3}-\\d{4} \\d{2}:\\d{2}"), "dd-MMM-yyyy HH:mm");
        patterns.put(Pattern.compile("\\d{2}-\\w{3}-\\d{4} \\d{2}:\\d{2}"), "dd-MMM-yyyy hh:mm");
        patterns.put(Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"), "yyyy-MM-dd hh:mm");
        patterns.put(Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"), "yyyy-MM-dd HH:mm");
        patterns.put(Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}"), "dd-MM-yyyy hh:mm");
        patterns.put(Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}"), "dd-MM-yyyy HH:mm");
        patterns.put(Pattern.compile("\\d{4}-\\w{3}-\\d{2} \\d{2}:\\d{2}"), "yyyy-MMM-dd hh:mm");
        patterns.put(Pattern.compile("\\d{4}-\\w{3}-\\d{2} \\d{2}:\\d{2}"), "yyyy-MMM-dd HH:mm");
        patterns.put(Pattern.compile("\\d{4}-\\w{3}-\\d{2} \\d{2}:\\d{2}:\\d{2}"), "yyyy-MMM-dd HH:mm:ss");

        Set<Locale> locales = new HashSet<>();
        locales.add(Locale.ENGLISH);
        locales.add(Locale.FRENCH);

        DateTimeFormatter formatter;

        for (Map.Entry<Pattern, String> item : patterns.entrySet()) {
            if (item.getKey().matcher(dateInString).find()) {
                for (Locale locale : locales) {
                    formatter = DateTimeFormatter.ofPattern(item.getValue(), locale);
                    try {
                        formattedDate = LocalDateTime.parse(dateInString, formatter);
                    } catch (DateTimeParseException e) {
                        // Uncatched try in order to try all possibility for parsing
                    }
                }
            }
        }

        return formattedDate;
    }


    /**
     * Converts a given string into a long value representing a size in bytes
     *
     * @param sizeInString
     * @return
     */
    public static Long sizeStringToLong(String sizeInString) {
        if (StringUtils.isEmpty(sizeInString) || sizeInString.contains("-")) return null;

        Long size = null;

        Pattern pattern = Pattern.compile("([0-9]+)[.]?([0-9]{0,2})?[ ]?([KMG])?[B]?");
        Matcher matcher = pattern.matcher(sizeInString);

        if (matcher.find()) {
            String value = matcher.group(1);
            String decimal = matcher.group(2);
            String unit = matcher.group(3);

            if (value == null) {
                return null;
            }

            size = Long.valueOf(value);

            Long scaleValue = 1L;
            if (unit != null) {
                switch (unit) {
                    case "K":
                        scaleValue = 1_000L;
                        break;
                    case "M":
                        scaleValue = 1_000_000L;
                        break;
                    case "G":
                        scaleValue = 1_000_000_000L;
                        break;
                    default:
                        break;
                }
            }

            size = size * scaleValue;

            Long decimalValue = 0L;
            // Add the decimal part
            if (!StringUtils.isEmpty(decimal)) {
                Long decimalL = Long.valueOf(decimal);
                int divider = decimalL < 10 ? 10 : 100;
                decimalValue = decimalL * (scaleValue / divider);
            }

            size = size + decimalValue;
        }

        return size;
    }

    public static HttpURLConnection getMeta(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            return !conn.getHeaderFields().isEmpty() ? conn : null;
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Get last modification of a file of a given url.
     * Takes time to retrieve data. Not yet used.
     *
     * @param url
     * @return
     */
    public static LocalDateTime getLastModification(String url) {
        HttpURLConnection meta = getMeta(url);
        if (meta != null) {
            return Instant.ofEpochMilli(meta.getLastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    /**
     * Decode a string to UTF
     *
     * @param url
     * @return
     */
    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("Unable to decode url to UTF-8", e);
            return url;
        }
    }
}

