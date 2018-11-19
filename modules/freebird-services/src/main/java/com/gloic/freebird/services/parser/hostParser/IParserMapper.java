package com.gloic.freebird.services.parser.hostParser;

import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.parser.ItemToParse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

/**
 * Interface for site parsers
 *
 * @author gloic
 */
public interface IParserMapper {

    /**
     * Return all rows of the document
     *
     * @return
     */
    Elements getAllRow(Document document);

    /**
     * Extract the link from a given element
     *
     * @param element
     * @return a element that is a link (a)
     */
    Element getLink(Element element);

    /**
     * Check if the given element is a directory
     *
     * @param element
     * @return true if the given element is a directory
     */
    boolean isDirectory(Element element);

    /**
     * Build an ItemToParse filled with all available data on the row
     *
     * @param element
     * @param site
     * @return
     */
    ItemToParse getItemToParse(Element element, Site site);

    /**
     * Retrieve the last modified date if possible
     *
     * @param element
     * @return
     */
    LocalDateTime getLastModifiedDate(Element element);
}
