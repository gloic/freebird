package com.gloic.freebird.commons.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class that cleans and adapt given strings.
 *
 * @author gloic
 */
public final class AppStringUtil {

    /**
     * Removes useless tags and unwanted characters from a string
     *
     * @param str
     * @return
     */
    public static final String cleanString(final String str) {
        if (StringUtils.isEmpty(str)) return null;

        // Remove everything between [ and ]
        String output = str.replaceAll("\\[.*?]", "");

        // Remove all unwanted character : _ - , . [ and ]
        output = output.replaceAll("[_|\\-|,|.|\\[|\\]]", " ");

        output = output.trim();
        output = output.toLowerCase();
        return output;
    }

    /**
     * Remove extension of a string
     *
     * @param str
     * @return
     */
    public static final String removeExtension(final String str) {
        if (StringUtils.isEmpty(str)) return null;

        int endIndex = str.lastIndexOf('.');
        return endIndex != -1 ? str.substring(0, endIndex) : str;
    }

    /**
     * Extract first numeric char of a string
     *
     * @param input
     * @return
     */
    public static final Integer findFirstNumeric(final String input) {
        String str = cleanString(input);

        String s = new String(str);
        Matcher matcher = Pattern.compile("\\d+").matcher(s);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group());
        }
        return null;
    }

    /**
     * Replace non ascii char from a string
     *
     * @param str
     * @return
     */
    public static String replaceNonAsciiChars(String str) {
        String result = str;

        if (result != null) {
            result = result.replace("%20", " ");
            result = result.replace("%c3%a9", "é");
            result = result.replace("%c3%a8", "è");
            result = result.replace("%e2%80%99", "'");
        }
        return result;
    }

}
