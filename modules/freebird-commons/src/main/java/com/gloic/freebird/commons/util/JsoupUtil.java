package com.gloic.freebird.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;

/**
 * Helper that access to a given url with Jsoup
 *
 * @author gloic
 */
@Slf4j
public class JsoupUtil {

    public static Document getDocument(String url) {
        Document doc = null;

        try {
            if (url != null) {
                doc = Jsoup.connect(URLDecoder.decode(url, "UTF-8")).timeout(30000).get();
            } else {
                log.error("Given URL is null");
            }
        } catch (HttpStatusException e) {
//            log.error("HttpStatusException for url: {}", url);
//            log.error("Stacktrace", e);
        } catch (SocketTimeoutException e) {
            log.error("A timeout occurred, the site is not reachable");
        } catch (IOException e) {
            log.error("IOException occurred", e);
        }
        return doc;
    }

}
