package com.gloic.freebird.services.parser.hostParser;

import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.parser.ItemToParse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

/**
 * Parser for unknown sites.
 * Retrieve data from link (a)
 *
 * @author gloic
 */
public class DefaultParser implements IParserMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public Elements getAllRow(Document document) {
        return document.select("a");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getLink(Element element) {
        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectory(Element element) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemToParse getItemToParse(Element element, Site site) {
        return new ItemToParse().toBuilder()
                .site(site)
                .filename(element.text())
                .url(element.absUrl("href"))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getLastModifiedDate(Element element) {
        return null;
    }
}
