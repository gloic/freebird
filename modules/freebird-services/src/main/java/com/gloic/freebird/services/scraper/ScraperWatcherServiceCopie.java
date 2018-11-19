package com.gloic.freebird.services.scraper;

import com.gloic.freebird.services.parser.ItemToParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gloic
 */
@Service
@Slf4j
public class ScraperWatcherServiceCopie {
    private final ScraperService scraperService;

    private Boolean isProcessing = false;
    private List<ItemToParse> items = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    public ScraperWatcherServiceCopie(ScraperService scraperService) {
        this.scraperService = scraperService;
    }


    /**
     * Iterates over the list (FIFO) and scrape the items.
     */
    private void process() {

        log.info("Starting scraping process");
//        synchronized(items) {
        isProcessing = true;

            /*
            Iterator<ItemToParse> iterator = items.iterator();
            while(iterator.hasNext()) {
                ItemToParse itemToParse = iterator.next();
                log.info("Processing item {}", itemToParse.getFilename());
                scraperService.scrape(itemToParse);
                iterator.remove();
                if (items.size() % 100 == 0) {
                    log.info("Number of items to scrape left : {}", items.size());
                }
            }
            */
        while (!items.isEmpty()) {
            ItemToParse itemToParse = items.get(0);
            log.info("Processing item {}", itemToParse.getFilename());
            scraperService.scrape(itemToParse);
            items.remove(0);
            if (items.size() % 100 == 0) {
                log.info("Number of items to scrape left : {}", items.size());
            }
        }
        isProcessing = false;
//        }

    }

    /**
     * Add an item to the list of item to scrape.
     * If the process is not started yet, starting it.
     *
     * @param itemToParse
     */
    @Async
    public void addItem(ItemToParse itemToParse) {
        log.debug("Adding item {}", itemToParse.getFilename());
        items.add(itemToParse);
        if (!isProcessing) {
            process();
        }
    }

}