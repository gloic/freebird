package com.gloic.freebird.services.parser.hostParser;

import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.parser.ItemToParse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

/**
 * TODO
 * Parse for IIS sites
 *
 * @author gloic
 */
public class IISParser implements IParserMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public Elements getAllRow(Document document) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getLink(Element element) {
        return null;
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
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getLastModifiedDate(Element element) {
        return null;
    }

}
