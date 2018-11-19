package com.gloic.freebird.persistence;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.repository.GenreRepository;
import com.gloic.freebird.persistence.repository.TvShowRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@Transactional
@SuppressWarnings("unused")
public class GenreRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TvShowRepository tvShowRepository;

    @Test
    public void shouldKeepGenre() {
        Genre genre = new Genre();
        genre.setName("Dummy");
        genre.setId(1);
        genreRepository.save(genre);

        TvShow tvShow = new TvShow();
        tvShow.setTitle("Dummy Show");
        tvShow.setIdShow(1);
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre);
        tvShow.setGenres(genreList);
        tvShowRepository.save(tvShow);

        Assert.assertEquals("A tv show should be persisted", 1, tvShowRepository.count());
        Assert.assertEquals("A genre should be persisted", 1, genreRepository.count());

        TvShow byIdShow = tvShowRepository.findByIdShow(1);
        tvShowRepository.delete(byIdShow);

        Assert.assertEquals("No tv show should be persisted", 0, tvShowRepository.count());
        Assert.assertEquals("A genre should be still persisted", 1, genreRepository.count());

    }
}