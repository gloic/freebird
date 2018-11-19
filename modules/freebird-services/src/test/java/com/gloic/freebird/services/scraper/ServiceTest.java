package com.gloic.freebird.services.scraper;

import com.gloic.freebird.SpringTestConfiguration;
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
public class ServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ScraperService scraperService;

    @Before
    public void setUp() {
        Assert.assertNotNull(scraperService);
    }

    @Test
    public void shouldWork() {
        ItemToParse itemToParse = new ItemToParse().toBuilder()
                .filename("Kill.Bill.1.MULTI.720p.AiRLiNE.mkv")
                .build();
        scraperService.scrape(itemToParse);

    }
}