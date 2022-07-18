import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(Producer.class);
    private BlockingQueue<File> blockingQueue;
    private String filePath;
    private AtomicBoolean atomicBoolean;
    private boolean state;

    public Producer(BlockingQueue<File> blockingQueue, String filePath, AtomicBoolean atomicBoolean) {
        this.blockingQueue = blockingQueue;
        this.filePath = filePath;
        this.atomicBoolean = atomicBoolean;
    }

    @Override
    public void run() {
        logger.info("Producer Thread started");
        File file = new File(filePath);
        addToQueue(file, blockingQueue);
        atomicBoolean.compareAndSet(true, false);
    }

    private void addToQueue(File file, BlockingQueue<File> blockingQueue) {
        if (file.isFile()) {
            try {
                logger.info("Adding file {} to queue", file);
                state = blockingQueue.offer(file, 4, TimeUnit.SECONDS);
                if (state) {
                    logger.info("Added file {} to queue", file);
                } else {
                    atomicBoolean.set(false);
                    logger.info("file not added");
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                try {
                    throw new InterruptedException();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            File[] files = file.listFiles();
            for (File childFile : files) {
                addToQueue(childFile, blockingQueue);
            }
        }
    }
}
