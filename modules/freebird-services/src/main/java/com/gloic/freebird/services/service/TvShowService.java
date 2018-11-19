package com.gloic.freebird.services.service;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.model.TvShow_;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.SeasonRepository;
import com.gloic.freebird.persistence.repository.TvShowRepository;
import com.gloic.freebird.services.specification.MovieSpecs;
import com.gloic.freebird.services.specification.SeasonSpecs;
import com.gloic.freebird.services.specification.TvShowSpecs;
import com.gloic.freebird.services.vo.request.FilterRequest;
import com.gloic.freebird.services.wrapper.TvShowWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service dedicated to manage the TvShows
 *
 * @author gloic
 */
@Service
public class TvShowService {

    private final TvShowRepository tvShowRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    public TvShowService(TvShowRepository tvShowRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository) {
        this.tvShowRepository = tvShowRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
    }

    /**
     * @param filterRequest
     * @return tv show filtered by user's preferences and given filter request
     */
    public List<TvShow> getFilteredTvShows(FilterRequest filterRequest) {
        Specifications<TvShow> specifications = Specifications.where(TvShowSpecs.preferenceSpecs()).and(TvShowSpecs.byFilters(filterRequest));
        Sort sort;
        if (filterRequest.getOrderBy() != null) {
            switch (filterRequest.getOrderBy()) {
                case "alphabetic":
                    sort = TvShowSpecs.SORT_BY_TITLE_ASC;
                    break;
                case "addingDate":
                    sort = new Sort(Sort.Direction.DESC, TvShow_.updated.getName());
                    break;
                case "popularity":
                    sort = new Sort(Sort.Direction.DESC, TvShow_.popularity.getName());
                    break;
                default:
                    sort = TvShowSpecs.SORT_BY_TITLE_ASC;
                    break;
            }
        } else {
            sort = MovieSpecs.SORT_BY_TITLE_ASC;
        }
        return tvShowRepository.findAll(specifications, sort);
    }


    /**
     * @param id
     * @return TvShowDetail with filtered seasons regarding user's prefs
     */
    public TvShowWrapper getDetailById(Long id) {
        TvShow tvShow = tvShowRepository.findOne(id);
        List<Season> seasons = seasonRepository.findAll(Specifications.where(SeasonSpecs.preferenceSpecs()).and(SeasonSpecs.byTvShowId(id)));
        return new TvShowWrapper(tvShow, seasons);
    }

    /**
     * @param id
     * @return season details
     */
    public Season getSeasonDetail(Long id) {
        return seasonRepository.findOne(id);
    }

    /**
     * @param id
     * @return episode details
     */
    public Episode getEpisodeDetail(Long id) {
        return episodeRepository.findOne(id);
    }

    /**
     * @return all genres used by at least one Tv Show
     */
    public List<Genre> getGenres() {
        return tvShowRepository.findAllGenres();
    }
}
