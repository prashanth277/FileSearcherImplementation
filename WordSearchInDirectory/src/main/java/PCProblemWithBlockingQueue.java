import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PCProblemWithBlockingQueue implements Searcher {
    private static Logger logger = LoggerFactory.getLogger(PCProblemWithBlockingQueue.class);
    private AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    private BlockingQueue<File> blockingQueue = new ArrayBlockingQueue<>(5);
    static Set<SearchResult> finalSearchResults = new HashSet<>();

    @Override
    public Set<SearchResult> search(String filePath, String searchString) {
        Thread producerThread = new Thread(new Producer(blockingQueue, filePath, atomicBoolean));
        Thread consumerThread = new Thread(new Consumer(blockingQueue, searchString, atomicBoolean, finalSearchResults));
        Thread consumerThread2 = new Thread(new Consumer(blockingQueue, searchString, atomicBoolean, finalSearchResults));
        producerThread.start();
        consumerThread.start();
        consumerThread2.start();
        try {
            producerThread.join();
            logger.info("Producer thread broken");
            consumerThread.join();
            consumerThread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return finalSearchResults;
    }

}
