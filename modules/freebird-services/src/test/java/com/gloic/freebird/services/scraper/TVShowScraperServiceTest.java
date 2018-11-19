package com.gloic.freebird.services.scraper;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@Transactional
@SuppressWarnings("unused")
public class TVShowScraperServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    private TvShowScraperService tvShowScraperService;

    @Test
    public void shouldFindEpisode() {
        String filename = "Arrow.S02E01.FRENCH.720p.AHDTV.x264";
        ItemToParse item = new ItemToParse().toBuilder().filename(filename).build();
        tvShowScraperService.scrape(item);

        List<Episode> episodes = episodeRepository.findAll();
        Assert.assertEquals(1, episodes.size());
        Assert.assertEquals(1, episodes.get(0).getLinks().size());
    }

    @Test
    public void shouldPersistOnlyOneEpisode() {
        String filename = "Game.Of.Thrones.S06E01.FRENCH.720p.AHDTV.x264";
        ItemToParse item = new ItemToParse().toBuilder()
                .filename(filename)
                .url("http://localhost/tv-show/")
                .build();
        tvShowScraperService.scrape(item);

        item.setUrl("http://127.0.0.1/videos/");
        tvShowScraperService.scrape(item);

        List<Episode> episodes = episodeRepository.findAll();
        Assert.assertEquals(1, episodes.size());
        Assert.assertEquals(2, episodes.get(0).getLinks().size());
    }

    @Test
    public void shouldExtractTitleAndTags() {
        ItemToParse item, reference;

        item = new ItemToParse().toBuilder().filename("Doctor.Who.S02E01.FRENCH.720p.AHDTV.x264").build();
        tvShowScraperService.extractData(item);
        reference = new ItemToParse().toBuilder()
                .filename(item.getFilename())
                .language(Language.FRENCH)
                .codec(Codec.CODEC_x264)
                .quality(Quality.QUALITY_720p)
                .potentialTitle("doctor who")
                .seasonNum(2).episodeNum(1).build();
        Assert.assertEquals(item, reference);

        item = new ItemToParse().toBuilder().filename("Arrow.S05E06.720p.HDTV.x265.ShAaNiG").build();
        tvShowScraperService.extractData(item);
        reference = new ItemToParse().toBuilder()
                .filename(item.getFilename())
                .language(Language.UNKNOWN)
                .codec(Codec.CODEC_x265)
                .quality(Quality.QUALITY_720p)
                .potentialTitle("arrow")
                .seasonNum(5).episodeNum(6).build();
        Assert.assertEquals(item, reference);

        item = new ItemToParse().toBuilder().filename("Arrow.2015.S03E02.FRENCH.720p.AHDTV.x264").build();
        tvShowScraperService.extractData(item);
        reference = new ItemToParse().toBuilder()
                .filename(item.getFilename())
                .language(Language.FRENCH)
                .codec(Codec.CODEC_x264)
                .quality(Quality.QUALITY_720p)
                .year(2015)
                .potentialTitle("arrow")
                .seasonNum(3).episodeNum(2).build();
        Assert.assertEquals(item, reference);
    }
}