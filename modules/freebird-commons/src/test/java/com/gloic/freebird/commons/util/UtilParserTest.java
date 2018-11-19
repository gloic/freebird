package com.gloic.freebird.commons.util;

import com.gloic.freebird.commons.SpringTestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@SuppressWarnings("unused")
public class UtilParserTest {

    @Test
    public void shouldReturnDate() {
        String str;

        str = "01-01-2015 12:24";
        Assert.assertEquals(LocalDateTime.of(2015, 1, 1, 12, 24), UtilsParser.dateStringToDate(str));
        str = "05-12-2001 07:46";
        Assert.assertEquals(LocalDateTime.of(2001, 12, 5, 7, 46), UtilsParser.dateStringToDate(str));
//        str = "12-01-2017 07:24";
//        Assert.assertEquals(LocalDateTime.of(2017, 12, 1, 7, 24), UtilsParser.dateStringToDate(str));
        str = "2017-12-01 07:24";
        Assert.assertEquals(LocalDateTime.of(2017, 12, 1, 7, 24), UtilsParser.dateStringToDate(str));
    }

    @Test
    public void shouldParsedDateBeNull() {
        Assert.assertNull(UtilsParser.dateStringToDate(null));
    }

    @Test
    public void shouldConvertedSizeBeNull() {
        Assert.assertEquals(null, UtilsParser.sizeStringToLong(null));
        Assert.assertEquals(null, UtilsParser.sizeStringToLong("fjfklsdjkfls"));
    }

    @Test
    public void shouldConvertSizeToLong() {
        String given;
        Long expected;

        given = "1K";
        expected = 1_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "1M";
        expected = 1_000_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "1G";
        expected = 1_000_000_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "655 M";
        expected = 655_000_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "1.6 G";
        expected = 1_600_000_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "26.21MB";
        expected = 26_210_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "696.20MB";
        expected = 696_200_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));

        given = "1.17GB";
        expected = 1_170_000_000L;
        Assert.assertEquals(expected, UtilsParser.sizeStringToLong(given));
    }

    @Test
    public void shouldReceiveMeta() {
        Assert.assertNotNull(UtilsParser.getMeta("http://www.google.com"));
    }

    @Test
    public void shouldNotReceiveMeta() {
        Assert.assertNull(UtilsParser.getMeta("http://www.an-non-existing-web-site.com"));
        Assert.assertNull(UtilsParser.getMeta("not_an_url"));
    }
}
