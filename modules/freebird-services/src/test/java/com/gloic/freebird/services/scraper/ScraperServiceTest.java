package com.gloic.freebird.services.scraper;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.services.parser.ItemToParse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@SuppressWarnings("unused")
public class ScraperServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CommonScraperService commonScraperService;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldExtractTags() {

        ItemToParse item;

        item = new ItemToParse().toBuilder().filename("Le.Silence.des.Agneaux.1991.TRUEFRENCH.BRRiP.XViD.AC3-HuSh").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals((Integer) 1991, item.getYear());
        Assert.assertEquals(Language.FRENCH, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_XVID, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_BDRIP, item.getQuality());


        item = new ItemToParse().toBuilder().filename("Punishment Park.1970.VOSTFR.DVDRip.XviD.AC3").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals((Integer) 1970, item.getYear());
        Assert.assertEquals(Language.VOSTFR, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_XVID, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_DVDRIP, item.getQuality());

        item = new ItemToParse().toBuilder().filename("X-M.Apocalypse.2016.TRUEFRENCH.720p.BluRay.DTS.x264-WEEDS").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals((Integer) 2016, item.getYear());
        Assert.assertEquals(Language.FRENCH, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_x264, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_720p, item.getQuality());

        item = new ItemToParse().toBuilder().filename("Bienvenue.a.Marly.Gomont.FRENCH.BDRip.x264.MKV").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals(null, item.getYear());
        Assert.assertEquals(Language.FRENCH, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_x264, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_BDRIP, item.getQuality());

        item = new ItemToParse().toBuilder().filename("Game.Of.Thrones.S06E01.FRENCH.720p.AHDTV.x264").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals(null, item.getYear());
        Assert.assertEquals(Language.FRENCH, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_x264, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_720p, item.getQuality());

        item = new ItemToParse().toBuilder().filename("FutureWorld - (MondWest2) - 576p - H264 - DVDrip").build();
        commonScraperService.extractTags(item);
        Assert.assertEquals(null, item.getYear());
        Assert.assertEquals(Language.UNKNOWN, item.getLanguage());
        Assert.assertEquals(Codec.CODEC_x264, item.getCodec());
        Assert.assertEquals(Quality.QUALITY_DVDRIP, item.getQuality());
    }
}