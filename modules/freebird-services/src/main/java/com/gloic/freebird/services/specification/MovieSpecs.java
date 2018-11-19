package com.gloic.freebird.services.specification;

import com.gloic.freebird.persistence.model.Genre_;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Link_;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Movie_;
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
 * Specification for Movie
 *
 * @author gloic
 */
public class MovieSpecs {

    // Default sort order
    public static final Sort SORT_BY_TITLE_ASC = new Sort(Sort.Direction.ASC, Movie_.title.getName());

    /**
     * Map the user's preference to a specification
     *
     * @return a specification
     */
    public static Specification<Movie> preferenceSpecs() {
        return (root, query, cb) -> {
            final Collection<Predicate> predicates = new ArrayList<>();

            ListJoin<Movie, Link> linkListJoin = root.join(Movie_.links);
            final Predicate languagePreference = linkListJoin.get(Link_.language).in(PreferencesMapper.getLanguagesMoviesFromPreferences());
            predicates.add(languagePreference);

            final Predicate qualityPreference = linkListJoin.get(Link_.quality).in(PreferencesMapper.getQualitiesFromPreferences());
            predicates.add(qualityPreference);

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Filter a movie by its title. can be combined to other specs
     *
     * @param title
     * @return a specification
     */
    public static Specification<Movie> byTitle(final String title) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(Movie_.title)), "%" + StringUtils.upperCase(title) + "%");
    }

    /**
     * Filter movies by given filters (sent by web app).
     * Can be combined with other specs
     *
     * @param filterRequest
     * @return a specification
     */
    public static Specification<Movie> byFilters(final FilterRequest filterRequest) {
        return (root, query, cb) -> {
            if (filterRequest.getGenreId() != null) {
                return cb.equal(root.join(Movie_.genres).get(Genre_.id), filterRequest.getGenreId());
            }
            return null;
        };
    }

}
