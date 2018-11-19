package com.gloic.freebird.services.parser.hostParser;

import com.gloic.freebird.commons.util.AppStringUtil;
import com.gloic.freebird.commons.util.UtilsParser;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.parser.ItemToParse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

/**
 * Parser for Lighttp sites
 *
 * @author gloic
 */
public class LighttpdParser implements IParserMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public Elements getAllRow(Document document) {
        return document.select("tr:has(td a)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getLink(Element row) {
        return row.select("td a").first();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectory(Element row) {
        return getLink(row).parent().text().endsWith("/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemToParse getItemToParse(Element element, Site site) {
        Elements columns = getColumnsFromRow(element);
        if (columns.size() == 0) {
            return null;
        }

        String filename = columns.get(0).select("a").text();

        if (filename.substring(filename.length() - 3, filename.length()).equals("..>")) {
            filename = AppStringUtil.replaceNonAsciiChars(columns.get(0).select("a").first().attr("href"));
        }

        String link = columns.get(0).select("a").attr("abs:href");
        String size = columns.get(2).text();

        return new ItemToParse().toBuilder()
                .filename(filename)
                .size(UtilsParser.sizeStringToLong(size))
                .url(link)
                .site(site)
                .parentUrl(UtilsParser.decodeURL(element.baseUri()))
                .lastModified(getLastModifiedDate(element))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getLastModifiedDate(Element element) {
        Elements columns = getColumnsFromRow(element);

        if (columns.size() == 0) {
            return null;
        }

        return UtilsParser.dateStringToDate(columns.get(1).text());
    }

    /**
     * {@inheritDoc}
     */
    public Elements getColumnsFromRow(Element row) {
        return row.getElementsByTag("td");
    }
}
