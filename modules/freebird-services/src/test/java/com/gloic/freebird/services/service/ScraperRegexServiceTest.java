package com.gloic.freebird.services.service;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.services.scraper.ScraperConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@SuppressWarnings("unused")
public class ScraperRegexServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Before
    public void setUp() {
    }

    @Test
    public void shouldMatchWithMedia() {
        Pattern pattern = ScraperConstants.patternMedia;
        Collection<String> shouldMatch = new HashSet<>(Arrays.asList(
                "A.random.file.mkv",
                "A random file.mkv",
                "A.random.file...avi",
                "A.random.file.avi"
        ));

        shouldMatch.forEach(s -> Assert.assertTrue("Should match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shoulNotMatchWithMedia() {
        Pattern pattern = ScraperConstants.patternMedia;
        Collection<String> shouldNotMatch = new HashSet<>(Arrays.asList(
                "A.random.file.exe",
                "A random file.nfo",
                "A.random.file.fr.srt",
                "A.random.file.avi.com"
        ));

        shouldNotMatch.forEach(s -> Assert.assertFalse("Should not match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldMatchWithTvShow() {
        Pattern pattern = ScraperConstants.patternTvShow;
        Collection<String> shouldMatch = new HashSet<>(Arrays.asList(
                "A.random.file.S01E01.mkv",
                "A.random.file.01x01.mkv",
                "A.random.file.s01e01.mkv",
                "A.random.file.S1x1.mkv",
                "A.random.file.S1x12.mkv",
                "A.random.file.S01x123.mkv",
                "A.random.file.1x123.mkv",
                "filename.1x123.mkv",
                "Arrow.S05E06.720p.HDTV.x265.ShAaNiG"
        ));

        shouldMatch.forEach(s -> Assert.assertTrue("Should match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldNotMatchWithTvShow() {
        Pattern pattern = ScraperConstants.patternTvShow;
        Collection<String> shouldNotMatch = new HashSet<>(Arrays.asList(
                "A.random.file.01.02.mkv",
                "A.random.file.800x600.mkv",
                "A.random.file.s.3.x.3.mkv"
//                "S01E01.A.random.file.mkv",
//                "01x01.A.random.file.mkv",
//                "1x01.A.random.file.mkv",
//                "1x101.A.random.file.mkv"
//                "s1e01.A.random.file.mkv", TODO
//                "s1e101.A.random.file.mkv"
        ));

        shouldNotMatch.forEach(s -> Assert.assertFalse("Should not match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldMatchWithTvShowStart() {
        Pattern pattern = ScraperConstants.patternTvShowStart;
        Collection<String> shouldMatch = new HashSet<>(Arrays.asList(
                "S01E01.A.random.file.mkv",
                "01x01.A.random.file.mkv",
                "1x01.A.random.file.mkv",
                "1x101.A.random.file.mkv",
                "s1e01.A.random.file.mkv",
                "s1e101.A.random.file.mkv"
        ));
        shouldMatch.forEach(s -> Assert.assertTrue("Should match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldNotMatchWithTvShowStart() {
        Pattern pattern = ScraperConstants.patternTvShowStart;
        Collection<String> shouldNotMatch = new HashSet<>(Arrays.asList(
                "A.random.file.S01E01.mkv",
                "A.random.file.01x01.mkv",
                "A.random.file.s01e01.mkv",
                "A.random.file.S1x1.mkv",
                "A.random.file.S1x12.mkv",
                "A.random.file.S01x123.mkv",
                "A.random.file.1x123.mkv"
        ));

        shouldNotMatch.forEach(s -> Assert.assertFalse("Should not match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldMatchWithTitleWithYear() {
        Pattern pattern = ScraperConstants.patternTitleWithYear;
        Collection<String> shouldMatch = new HashSet<>(Arrays.asList(
                "A Random Movie Title 2005 DVDRIP XVID AC3.mkv",
                "Mad.Max.Fury.Road.2015.FRENCH.720p.WEB-DL.X264.AC3.mkv",
                "Deja.Vu.2006.TRUEFRENCH.BRRip.x264.1920x1080.AC3-FREAKY.avi",
                "Drag.Me.To.Hell.2009.MULTi.720p.800x600.BluRay.x264.mkv",
                "Reservoir Dogs (1992) [1080p] VFF Hdlight x264.mkv"
        ));

        shouldMatch.forEach(s -> Assert.assertTrue("Should match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldNotdMatchWithTitleWithYear() {
        Pattern pattern = ScraperConstants.patternTitleWithYear;
        Collection<String> shouldNotMatch = new HashSet<>(Arrays.asList(
                "Kill.Bill.2.MULTI.720p.mkv",
                "Metallica - Through The Never.mkv",
                "Metallica - 1920x1080 Through The Never.mkv",
                "Pink Floyd The Wall VostFR.avi"
        ));

        shouldNotMatch.forEach(s -> Assert.assertFalse("Should not match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void shouldMatchWithTitleStartWithYear() {
        Pattern pattern = ScraperConstants.patternTitleStartWithYear;
        Collection<String> shouldMatch = new HashSet<>(Arrays.asList(
                "1981 - Mad Max 2  The Road Warrior - George Miller.avi",
                "2016.Dont.Breathe.FRENCH.BRRiP.XviD.avi"
        ));

        shouldMatch.forEach(s -> Assert.assertTrue("Should match with pattern:" + s, pattern.matcher(s).find()));
    }

    @Test
    public void testPatternTitleStartWithYear() {
        Pattern pattern = ScraperConstants.patternTitleStartWithYear;
        Collection<String> shouldNotMatch = new HashSet<>(Arrays.asList(
                "A Random Movie Title 2005 DVDRIP XVID AC3.mkv",
                "Mad.Max.Fury.Road.2015.FRENCH.720p.WEB-DL.X264.AC3.mkv",
                "Deja.Vu.2006.TRUEFRENCH.BRRip.x264.1920x1080.AC3-FREAKY.avi",
                "Drag.Me.To.Hell.2009.MULTi.720p.800x600.BluRay.x264.mkv",
                "Reservoir Dogs (1992) [1080p] VFF Hdlight x264.mkv"
        ));

        shouldNotMatch.forEach(s -> Assert.assertFalse("Should not match with pattern:" + s, pattern.matcher(s).find()));
    }
}