import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
    private BlockingQueue<File> blockingQueue;
    private String searchString;
    private AtomicBoolean atomicBoolean;
    private Set<SearchResult> overallSearchResults;
    FileSearcher fileSearcher = new FileSearcher();

    public Consumer(BlockingQueue<File> blockingQueue, String searchString, AtomicBoolean atomicBoolean, Set<SearchResult> searchResults) {
        this.blockingQueue = blockingQueue;
        this.searchString = searchString;
        this.atomicBoolean = atomicBoolean;
        this.overallSearchResults = searchResults;
    }

    @Override
    public void run() {
        try {

            while (true) {
                logger.info("Consumer thread started ");
                Thread.sleep(3000);
                File file = blockingQueue.poll(5, TimeUnit.SECONDS);
                logger.info("Consumed file {} ", file);
                overallSearchResults.addAll(fileSearcher.search(String.valueOf(file), searchString));
                logger.info("result {} ", overallSearchResults);
                logger.info("atomicBoolean value ---------------------{}", atomicBoolean.get());
                if (!atomicBoolean.get()) {
                    logger.info("Consumer thread stopped");
                    break;
                }
            }
        } catch (InterruptedException e) {
            logger.info("{}", e);
        }
    }
}
