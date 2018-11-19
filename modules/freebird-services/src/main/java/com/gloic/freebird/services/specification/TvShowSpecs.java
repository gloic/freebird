package com.gloic.freebird.services.specification;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Episode_;
import com.gloic.freebird.persistence.model.Genre_;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Link_;
import com.gloic.freebird.persistence.model.Season_;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.model.TvShow_;
import com.gloic.freebird.services.util.PreferencesMapper;
import com.gloic.freebird.services.vo.request.FilterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Specifications for TVShows
 *
 * @author gloic
 */
public class TvShowSpecs {

    // Most common order for TVShows
    public static final Sort SORT_BY_TITLE_ASC = new Sort(Sort.Direction.ASC, TvShow_.title.getName());

    /**
     * Map the user's preferences to a Specification to pre-filter data by query
     *
     * @return a specification
     */
    public static Specification<TvShow> preferenceSpecs() {
        return (root, query, cb) -> {
            final Collection<Predicate> predicates = new ArrayList<>();

            ListJoin<Episode, Link> linkListJoin = root.join(TvShow_.seasons).join(Season_.episodes).join(Episode_.links);

            final Predicate languagePreference = linkListJoin.get(Link_.language).in(PreferencesMapper.getLanguagesTvShowsFromPreferences());
            predicates.add(languagePreference);

            final Predicate qualityPreference = linkListJoin.get(Link_.quality).in(PreferencesMapper.getQualitiesFromPreferences());
            predicates.add(qualityPreference);

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Find movie by its title. Can be combined with other specification
     *
     * @param title : movie title
     * @return a specification
     */
    public static Specification<TvShow> byTitle(final String title) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(TvShow_.title)), "%" + StringUtils.upperCase(title) + "%");
    }

    /**
     * Map the given filter to a specification. Can be combined with other specs
     *
     * @param filterRequest : filter request sent by the web application
     * @return a specification
     */
    public static Specification<TvShow> byFilters(final FilterRequest filterRequest) {
        return (root, query, cb) -> {
            if (filterRequest.getGenreId() != null) {
                return cb.equal(root.join(TvShow_.genres).get(Genre_.id), filterRequest.getGenreId());
            }
            return null;
        };
    }

}
