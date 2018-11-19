package com.gloic.freebird.services.scraper;

import com.gloic.freebird.services.parser.ItemToParse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author gloic
 */
@Service
@Slf4j
public class ScraperWatcherServiceNew {
    private final ScraperService scraperService;
    private final ListeningExecutorService threads = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
    //    private List<ItemToParse> items = Collections.synchronizedList(new ArrayList<>());
    private boolean isProcessing;
    private Consumer consumer;
    private BlockingDeque<ItemToParse> queue;

    @Autowired
    public ScraperWatcherServiceNew(ScraperService scraperService) {
        this.scraperService = scraperService;
        queue = new LinkedBlockingDeque<>();
    }

    public void processItem(ItemToParse itemToParse) {
        String siteUrl = itemToParse.getSite().getUrl();

        if (consumer == null) {
            Consumer consumer = new Consumer(queue);
            queue.add(itemToParse);
            ListenableFuture<Boolean> listenableFuture = threads.submit(consumer);
            initCallBack(itemToParse, listenableFuture);
//            log.debug("Processor initialized for site: " + siteUrl + " by receiving itemToParse" + itemToParse.getFilename());
        } else {
            log.debug("Item " + itemToParse.getFilename() + " added in consumer (site: " + siteUrl + ").");
            queue.add(itemToParse);
        }

//        if (consumer.get(siteUrl) == null) {
//            log.debug("New Consumer for site: {}", siteUrl);
//            Consumer consumer = new Consumer();
//            Consumer oldConsumer = this.consumer.putIfAbsent(siteUrl, consumer);
//            if (oldConsumer != null) {
//                consumer = oldConsumer;
//            }
//            consumer.addEventSimulator(itemToParse);
//
//            //Pool execution with first event received
//            ListenableFuture<Boolean> listenableFuture = threads.submit(consumer);
//            initCallBack(itemToParse, listenableFuture);
//            log.debug("Processor initialized for site: " + siteUrl + " by receiving itemToParse" + itemToParse.getFilename());
//        } else {
//            log.debug("Item " + itemToParse.getFilename() + " added in consumer (site: " + siteUrl + ").");
//            //Consumer for that policy is already existing -> add the event in the queue
//            Consumer consumer = this.consumer.get(siteUrl);
//            consumer.addEventSimulator(itemToParse);
//        }
    }

    private void initCallBack(final ItemToParse itemToParse, ListenableFuture<Boolean> listenableFuture) {
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() { //Non-blocking callback
            @Override
            public void onSuccess(Boolean result) {
                //Process successfully terminated
                if (result) {
                    //If all items have been processed, remove the consumer from the map
//                    consumer.remove(itemToParse.getSite().getUrl());
                    log.info("All items have been processed for site " + itemToParse.getSite().getUrl() + ".");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //If anything went wrong, safely remove the consumer from the map
//                consumer.remove(itemToParse.getSite().getUrl());
                log.error("Consumer has been interrupted for policy " + itemToParse.getSite().getUrl() + ": ", t);
            }
        });
    }

    /**
     * Iterates over the list (FIFO) and scrape the items.
     */
//    private void process() {
//        isProcessing = true;
//
//        log.info("Starting scraping process");
//        while (items.size() > 0) {
//            ItemToParse itemToParse = items.get(0);
//            log.debug("Processing item {}", itemToParse.getFilename());
//            scraperService.scrape(itemToParse);
//            items.remove(0);
//            if (items.size() % 100 == 0) {
//                log.info("Number of items to scrape left : {}", items.size());
//            }
//        }
//        isProcessing = false;
//        log.info("Scraping finished.");
//    }
//
//    /**
//     * Add an item to the list of item to scrape.
//     * If the process is not started yet, starting it.
//     *
//     * @param itemToParse
//     */
//    @Async
//    public void addItem(ItemToParse itemToParse) {
//        log.debug("Adding item {}", itemToParse.getFilename());
//        items.add(itemToParse);
//        if (!isProcessing) {
//            process();
//        }
//    }

    private class Consumer implements Callable<Boolean> {
        private final long MAX_WAITING_TIME = Long.MAX_VALUE;
        private final long WAITING_TIME = 50;
        private BlockingDeque<ItemToParse> queue;
        private volatile boolean first = true;

        private Consumer(BlockingDeque<ItemToParse> queue) {
            this.queue = queue;
        }

        public Boolean call() throws Exception {
            return process();
        }

        private Boolean process() {
            for (; ; ) {
                ItemToParse itemToParse = null;
                try {
                    itemToParse = queue.poll(first ? WAITING_TIME : MAX_WAITING_TIME, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    log.error("Process interrupted. Could not retrieve any bill from queue!", e);
                }

                if (itemToParse == null) {
                    log.debug("No further event received after timeout");
                    return true;
                }

                log.debug("ItemToParse received: {}", itemToParse.getFilename());
                log.debug("Process is starting for site: " + itemToParse.getSite().getUrl() + " and item: " + itemToParse.getFilename());
                processItem(itemToParse);
                if (first) {
                    continue;
                }

                if (!queue.isEmpty()) {
                    continue;
                }

                log.debug("All items have been processed");
                return true;
            }

        }

        private void processItem(final ItemToParse itemToParse) {
            scraperService.scrape(itemToParse);
            log.error("Item done: {}", itemToParse.getFilename());
        }

//        private void addEventSimulator(ItemToParse itemToParse) {
//            queue.add(itemToParse);
//        }
    }
}