package com.gloic.freebird.services.util;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.SeasonRepository;
import com.gloic.freebird.persistence.repository.TvShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SynchronizedPersistence {

    private ConcurrentMap<String, TvShow> tvShows = new ConcurrentHashMap<>();

    private List<Season> seasons = Collections.synchronizedList(new ArrayList<>());
    private List<Episode> episodes = Collections.synchronizedList(new ArrayList<>());

    private final TvShowRepository tvShowRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    public SynchronizedPersistence(TvShowRepository tvShowRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository) {
        this.tvShowRepository = tvShowRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
    }

    public static void save(JpaRepository repo, Object item) {
        repo.save(item);
    }

    public void push(String key, TvShow tvShow) {
        tvShows.putIfAbsent(key, tvShow);
    }

    public void push(Season season) {
        seasons.add(season);
    }

    public void push(Episode episode) {
        episodes.add(episode);
    }

    public void persistAll() {
        tvShowRepository.save(tvShows.values());
        tvShows.clear();

        seasonRepository.save(seasons);
        seasons.clear();

        episodeRepository.save(episodes);
        episodes.clear();
    }

    public TvShow getCachedTvShow(final String key) {
        return tvShows.get(key);
    }
}
