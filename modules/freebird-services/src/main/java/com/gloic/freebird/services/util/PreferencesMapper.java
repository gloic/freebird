package com.gloic.freebird.services.util;

import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.PreferredLanguage;
import com.gloic.freebird.commons.enumerations.PreferredQuality;
import com.gloic.freebird.commons.enumerations.Quality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mapper from UserPreferences to UserPreferencesVO.
 * A special mapping has to be done for enums in order to have readable and ready-to-use strings
 *
 * @author gloic
 */
public class PreferencesMapper {

    public static List<Language> getLanguagesMoviesFromPreferences() {
        List<Language> languages = new ArrayList<>();
        for (PreferredLanguage l : SecurityHelper.getUserPreferences().getLanguagesMovies()) {
            mapLanguages(languages, l);
        }

        return languages;
    }

    public static List<Language> getLanguagesTvShowsFromPreferences() {
        List<Language> languages = new ArrayList<>();
        for (PreferredLanguage l : SecurityHelper.getUserPreferences().getLanguagesTvShows()) {
            mapLanguages(languages, l);
        }

        return languages;
    }

    public static List<Quality> getQualitiesFromPreferences() {
        List<Quality> qualities = new ArrayList<>();
        for (PreferredQuality q : SecurityHelper.getUserPreferences().getQualities()) {
            switch (q) {
                case QUALITY_4K:
                    qualities.add(Quality.QUALITY_4K);
                    break;
                case QUALITY_1080p:
                    qualities.add(Quality.QUALITY_1080p);
                    break;
                case QUALITY_720p:
                    qualities.add(Quality.QUALITY_720p);
                    break;
                case QUALITY_BDRIP:
                    qualities.add(Quality.QUALITY_BDRIP);
                    break;
                case QUALITY_DVDRIP:
                    qualities.add(Quality.QUALITY_DVDRIP);
                    break;
                case QUALITY_CAM:
                    qualities.add(Quality.QUALITY_CAM);
                    break;
                case QUALITY_UNKNOWN:
                    qualities.add(Quality.UNKNOWN);
                    break;
            }
        }
        return qualities;
    }

    private static void mapLanguages(List<Language> languages, PreferredLanguage l) {
        switch (l) {
            case FRENCH:
                languages.addAll(Arrays.asList(new Language[]{Language.FRENCH, Language.MULTI, Language.VFQ}));
                break;
            case VOSTFR:
                languages.add(Language.VOSTFR);
                break;
            case ENGLISH:
                languages.add(Language.ENGLISH);
                break;
            case UNKNOWN:
                languages.add(Language.UNKNOWN);
                break;
            default:
                break;
        }
    }
}
