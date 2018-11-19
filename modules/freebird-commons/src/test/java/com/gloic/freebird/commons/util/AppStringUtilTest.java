package com.gloic.freebird.commons.util;

import com.gloic.freebird.commons.SpringTestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
@SuppressWarnings("unused")
public class AppStringUtilTest {
    @Test
    public void shouldFindFirstNumeric() {
        String given = "abc12def456";
        Integer expected = 12;
        Assert.assertEquals("First numeric shoudld match", expected, AppStringUtil.findFirstNumeric(given));

        given = "[Kaerizaki-Fansub] One Piece 731 VOSTFR HD (1280x720)";
        expected = 731;
        Assert.assertEquals("First numeric shoudld match", expected, AppStringUtil.findFirstNumeric(given));

        given = "[Kaerizaki-Fansub-2017] One Piece 731 VOSTFR HD (1280x720)";
        expected = 731;
        Assert.assertEquals("First numeric shoudld match", expected, AppStringUtil.findFirstNumeric(given));

        given = "20";
        expected = 20;
        Assert.assertEquals("First numeric shoudld match", expected, AppStringUtil.findFirstNumeric(given));
    }
}
