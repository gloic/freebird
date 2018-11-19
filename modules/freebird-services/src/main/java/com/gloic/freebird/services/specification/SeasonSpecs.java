package com.gloic.freebird.services.specification;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Episode_;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Link_;
import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.Season_;
import com.gloic.freebird.persistence.model.TvShow_;
import com.gloic.freebird.services.util.PreferencesMapper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Specification for Seasons
 *
 * @author gloic
 */
public class SeasonSpecs {

    /**
     * Map the user's preference to a Spec
     *
     * @return a specification
     */
    public static Specification<Season> preferenceSpecs() {
        return (root, query, cb) -> {
            final Collection<Predicate> predicates = new ArrayList<>();

            ListJoin<Episode, Link> linkListJoin = root.join(Season_.episodes).join(Episode_.links);

            final Predicate languagePreference = linkListJoin.get(Link_.language).in(PreferencesMapper.getLanguagesTvShowsFromPreferences());
            predicates.add(languagePreference);

            final Predicate qualityPreference = linkListJoin.get(Link_.quality).in(PreferencesMapper.getQualitiesFromPreferences());
            predicates.add(qualityPreference);

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Find the season for a given TvShow Id. can be combined with other specs
     *
     * @param tvShowId
     * @return a specification
     */
    public static Specification<Season> byTvShowId(final Long tvShowId) {
        return (root, query, cb) -> cb.equal(root.join(Season_.tvShow).get(TvShow_.id), tvShowId);
    }

}
