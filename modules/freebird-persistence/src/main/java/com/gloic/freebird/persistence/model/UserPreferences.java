package com.gloic.freebird.persistence.model;

import com.gloic.freebird.commons.enumerations.PreferredLanguage;
import com.gloic.freebird.commons.enumerations.PreferredQuality;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gloic
 */
@Entity
@Data
public class UserPreferences {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection(targetClass = PreferredLanguage.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<PreferredLanguage> languagesMovies = new HashSet<>(Arrays.asList(PreferredLanguage.values()));

    @ElementCollection(targetClass = PreferredLanguage.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<PreferredLanguage> languagesTvShows = new HashSet<>(Arrays.asList(PreferredLanguage.values()));

    @ElementCollection(targetClass = PreferredQuality.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<PreferredQuality> qualities = new HashSet<>(Arrays.asList(PreferredQuality.values()));
}
