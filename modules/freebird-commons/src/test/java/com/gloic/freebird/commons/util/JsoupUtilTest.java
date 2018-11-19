package com.gloic.freebird.commons.util;

import com.gloic.freebird.commons.SpringTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gloic
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
public class JsoupUtilTest {

    @Test
    public void shouldAccessToUrl() {
        // TODO
        /*
        Collection<String> urls = new HashSet<>(Arrays.asList(
                "",
                ""
        ));

        urls.forEach(url -> {
            Assert.notNull(JsoupUtil.getDocument(url), "A document should be retrieve for url:" + url);
        });
        */
    }
}
