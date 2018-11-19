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
 * Parse for Apache sites
 *
 * @author gloic
 */
public class ApacheParser implements IParserMapper {

    public ApacheParser() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Elements getAllRow(Document doc) {
        return doc.select("tr:has(td a)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getLink(Element element) {
        return element.select("td a").first();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectory(Element element) {
        return getLink(element).attr("href").endsWith("/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemToParse getItemToParse(Element element, Site site) {
        Elements columns = element.getElementsByTag("td");
        if (columns.size() == 0) {
            return null;
        }

		/*
        Example :
			[0] = {org.jsoup.nodes.element@938}"<td valign="top"><img src="/icons/unknown.gif" alt="[   ]" /></td>"
			[1] = {org.jsoup.nodes.element@939}"<td><a href="9aea4ec0be0decc71baa..&gt1a129e7f19cb94f282da.meta">9aea4ec0be0decc71baa1a129e7f19cb94f282da.meta</a></td>"
			[2] = {org.jsoup.nodes.element@940}"<td align="right">05-mar-2013 05:53 </td>"
			[3] = {org.jsoup.nodes.element@941}"<td align="right"> 26k</td>"
			[4] = {org.jsoup.nodes.Element@942}"<td>&nbsp;</td>"
		*/

        String filename = columns.get(1).select("a").text();
        if (filename.equals("Parent Directory")) {
            return null;
        }

        if (filename.substring(filename.length() - 3, filename.length()).equals("..>")) {
            filename = AppStringUtil.replaceNonAsciiChars(columns.get(1).select("a").first().attr("href"));
        }

        String link = columns.get(1).select("a").attr("abs:href");
        String size = columns.get(3).text();

        return new ItemToParse().toBuilder()
                .filename(filename)
                .size(UtilsParser.sizeStringToLong(size))
                .url(link)
                .site(site)
                .parentUrl(UtilsParser.decodeURL(element.baseUri()))
                .lastModified(getLastModifiedDate(element))
                .build();
    }


    private Elements getColumnsFromRow(Element row) {
        return row.getElementsByTag("td");
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

        return UtilsParser.dateStringToDate(columns.get(2).text());
    }
}
