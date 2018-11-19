package com.gloic.freebird.services.scraper;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
public class MovieScraperServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private MovieScraperService movieScraperService;

    @Before
    public void setUp() {
        Assert.assertNotNull(movieScraperService);
    }

    @Test
    public void shouldFind() {
        Collection<String> given = new HashSet<>(Arrays.asList(
                "Fight club",
                "Minority Report [1080p] MULTi 2002 BluRay x264-Pop"
        ));

        given.forEach(s -> {
            ItemToParse itemToParse = new ItemToParse().toBuilder().build();
            itemToParse.setFilename(s);
            movieScraperService.scrape(itemToParse);
            Assert.assertEquals("The movie should be found: " + s, 1, movieRepository.findAll().size());
            movieRepository.deleteAll();
        });
    }

    @Test
    public void shouldNotFind() {
        Collection<String> given = new HashSet<>(Arrays.asList(
                "A12Z3E4R5T6Y7",
                "20",
                "17"
        ));

        given.forEach(s -> {
            ItemToParse itemToParse = new ItemToParse().toBuilder().build();
            itemToParse.setFilename(s);
            movieScraperService.scrape(itemToParse);
            Assert.assertEquals("The movie should not be found: " + s, 0, movieRepository.findAll().size());
            movieRepository.deleteAll();
        });
    }

    @Test
    public void shouldPersistOnlyOneMovie() {
        String filename = "Fight Club";
        ItemToParse item = new ItemToParse().toBuilder()
                .filename(filename)
                .url("http://localhost/movies")
                .build();
        movieScraperService.scrape(item);

        item.setUrl("http://127.0.0.1/videos");
        movieScraperService.scrape(item);

        List<Movie> movies = movieRepository.findAll();
        Assert.assertEquals(1, movies.size());
        Assert.assertEquals(2, movies.get(0).getLinks().size());
    }

    @Test
    public void shouldExtractTitleAndTags() {
        ItemToParse item;

        item = new ItemToParse().toBuilder().filename("  Le.Silence.des.Agneaux . 1991.TRUEFRENCH.BRRiP.XViD.AC3-HuSh").build();
        movieScraperService.extractData(item);
        Assert.assertEquals("le silence des agneaux", item.getPotentialTitle());
        Assert.assertEquals((Integer) 1991, item.getYear());
        Assert.assertEquals(Language.FRENCH, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_XVID, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_BDRIP, item.getQuality());

    }
}