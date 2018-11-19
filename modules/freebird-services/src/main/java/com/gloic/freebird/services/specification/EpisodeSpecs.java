package com.gloic.freebird.services.specification;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Episode_;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Link_;
import com.gloic.freebird.services.util.PreferencesMapper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Specification for Episodes
 *
 * @author gloic
 */
public class EpisodeSpecs {

    /**
     * Map the user's preference to a specification
     *
     * @return a specification
     */
    public static Specification<Episode> preferenceSpecs() {
        return (root, query, cb) -> {
            final Collection<Predicate> predicates = new ArrayList<>();

            ListJoin<Episode, Link> linkListJoin = root.join(Episode_.links);

            final Predicate languagePreference = linkListJoin.get(Link_.language).in(PreferencesMapper.getLanguagesTvShowsFromPreferences());
            predicates.add(languagePreference);

            final Predicate qualityPreference = linkListJoin.get(Link_.quality).in(PreferencesMapper.getQualitiesFromPreferences());
            predicates.add(qualityPreference);

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
