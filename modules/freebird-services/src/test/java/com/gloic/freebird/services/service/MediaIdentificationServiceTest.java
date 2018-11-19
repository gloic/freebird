package com.gloic.freebird.services.service;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.LinkRepository;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.persistence.repository.SiteRepository;
import com.gloic.freebird.persistence.repository.UnknownMediaRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import com.gloic.freebird.services.scraper.ScraperService;
import com.gloic.freebird.services.vo.request.EpisodeIdentificationRequest;
import com.gloic.freebird.services.vo.request.MovieIdentificationRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@SuppressWarnings("unused")
public class MediaIdentificationServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MediaIdentificationService identificationService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private UnknownMediaRepository unknownMediaRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ScraperService scraperService;

    private ItemToParse itemToParse;

    @Before
    public void setUp() {
        itemToParse = new ItemToParse().toBuilder()
                .filename("AAAAAA123456789123456")
                .build();
    }

    @Test
    public void shouldFindUnknownMedia() {
        Assert.assertEquals(0, unknownMediaRepository.findAll().size());
        scraperService.scrape(itemToParse);

        Assert.assertEquals(1, unknownMediaRepository.findAll().size());
    }

    @Test
    public void shouldIdentifyAMovie() {
        scraperService.scrape(itemToParse);

        UnknownMedia media = unknownMediaRepository.findAll().get(0);
        MovieIdentificationRequest movieIdentificationRequest = new MovieIdentificationRequest();
        movieIdentificationRequest.setMediaId(media.getId());
        movieIdentificationRequest.setTitle("Fight Club");
        identificationService.identifyMovie(movieIdentificationRequest);

        Assert.assertNull("unknown media should not exist anymore", unknownMediaRepository.findOne(media.getId()));
        Assert.assertEquals("no episode should be found", 0, episodeRepository.findAll().size());
        List<Movie> movies = movieRepository.findAll();
        Assert.assertEquals("a movie should be found", 1, movies.size());

        Link movieLink = movies.get(0).getLinks().iterator().next();
        Link mediaLink = media.getLink();
        Assert.assertEquals("same id should be retrieve for the movie link", mediaLink.getId(), movieLink.getId());
        Assert.assertEquals("movie link category should be MOVIE", MediaCategory.MOVIE, movieLink.getCategory());
        Assert.assertEquals(1, linkRepository.findAll().size());
    }

    @Test
    public void shouldNotIdentifyAMovie() {
        scraperService.scrape(itemToParse);

        UnknownMedia media = unknownMediaRepository.findAll().get(0);
        MovieIdentificationRequest movieIdentificationRequest = new MovieIdentificationRequest();
        movieIdentificationRequest.setMediaId(media.getId());
        movieIdentificationRequest.setTitle("BBBBBBBBB123456789123456");
        identificationService.identifyMovie(movieIdentificationRequest);
        Assert.assertEquals(0, movieRepository.findAll().size());

        UnknownMedia mediaNew = unknownMediaRepository.findAll().get(0);
        Assert.assertEquals(media, mediaNew);
    }

    @Test
    public void shouldIdentifyAnEpisode() {
        scraperService.scrape(itemToParse);

        UnknownMedia media = unknownMediaRepository.findAll().iterator().next();
        EpisodeIdentificationRequest request = new EpisodeIdentificationRequest();
        request.setMediaId(media.getId());
        request.setEpisodeNum(1);
        request.setSeasonNum(1);
        request.setShowName("Game Of Thrones");
        identificationService.identifyEpisode(request);

        Assert.assertNull("unknown media should not exist anymore", unknownMediaRepository.findOne(media.getId()));
        Assert.assertEquals("no movie should be found", 0, movieRepository.findAll().size());
        List<Episode> episodes = episodeRepository.findAll();
        Assert.assertEquals("an episode should be found", 1, episodes.size());
        Link episodeLink = episodes.get(0).getLinks().get(0);
        Link mediaLink = media.getLink();
        Assert.assertEquals("same id should be retrieve for the episode link", mediaLink.getId(), episodeLink.getId());
        Assert.assertEquals("movie link category should be TV_SHOW", MediaCategory.TV_SHOW, episodeLink.getCategory());
        Assert.assertEquals(1, linkRepository.findAll().size());
    }

    @Test
    public void shouldSendAMovieToUnknowns() {
        itemToParse.setFilename("Fight Club");
        scraperService.scrape(itemToParse);
        Movie movie = movieRepository.findByTitle("Fight Club");
        identificationService.wrongMovieIdentification(movie.getId(), movie.getLinks().iterator().next().getId());

        Assert.assertEquals("no movie should be found", 0, movieRepository.findAll().size());
        Assert.assertEquals("1 unknown media should be found", 1, unknownMediaRepository.findAll().size());
        Assert.assertEquals(1, linkRepository.findAll().size());

    }

    @Test
    public void shouldRemoveOneLinkOfMovie() {
        itemToParse.setFilename("Fight Club");
        itemToParse.setUrl("http://localhost/movies/fightclub.avi");
        scraperService.scrape(itemToParse);
        itemToParse.setUrl("http://localhost/movies/fightclub-hd.mkv");
        scraperService.scrape(itemToParse);

        List<Movie> movies = movieRepository.findAll();
        Assert.assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        Assert.assertEquals(2, movie.getLinks().size());

        identificationService.wrongMovieIdentification(movie.getId(), movie.getLinks().iterator().next().getId());

        movies = movieRepository.findAll();
        Assert.assertEquals(1, movies.size());
        movie = movies.get(0);
        Assert.assertEquals(1, movie.getLinks().size());
    }

    //    @Test
    public void shouldSendAnEpisodeToUnknowns() {
        ItemToParse itemToParse = new ItemToParse().toBuilder()
                .filename("Game.Of.Thrones.S01E03.mkv")
                .build();
        scraperService.scrape(itemToParse);

        Episode episode = episodeRepository.findAll().get(0);
        Link episodeLink = episode.getLinks().get(0);
        identificationService.wrongEpisodeIdentification(episode.getId(), episodeLink.getId());

        Assert.assertEquals(0, episodeRepository.findAll().size());
        List<UnknownMedia> unknownMedias = unknownMediaRepository.findAll();
        Assert.assertEquals(1, unknownMedias.size());
        Assert.assertEquals(MediaCategory.UNKNOWN, unknownMedias.get(0).getLink().getCategory());
        Assert.assertEquals(1, linkRepository.findAll().size());

    }

    @Test
    public void shouldIgnoreAnUnknownMedia() {
        scraperService.scrape(itemToParse);
        UnknownMedia media = unknownMediaRepository.findAll().get(0);
        Assert.assertFalse(media.isIgnored());
        identificationService.ignoreMedia(media.getId());
        media = unknownMediaRepository.findAll().get(0);
        Assert.assertTrue(media.isIgnored());
    }

    //    @Test
    public void shouldIgnoreAFolder() {
        itemToParse.setParentUrl("http://localhost/toIgnore");
        scraperService.scrape(itemToParse);

        UnknownMedia media = unknownMediaRepository.findAll().get(0);
        Site site = new Site();
        site.setUrl("http://localhost/");
        siteRepository.save(site);
        media.getLink().setSite(site);
        identificationService.ignoreFolder(media.getId());

        Assert.assertEquals(0, unknownMediaRepository.findAll().size());
        Assert.assertEquals(0, linkRepository.findAll().size());

        Assert.assertEquals(1, siteRepository.findAll().get(0).getIgnoredUrls().size());
    }
}
