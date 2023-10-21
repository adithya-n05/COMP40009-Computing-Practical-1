import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by David on 28/03/2016.
 */
public class Main {

    public static void main(String[] args) {

    }

    double meanSizeInBytes(List<URL> urls) throws InterruptedException {
        ExecutorService e = Executors.newFixedThreadPool(4);
        CountDownLatch l = new CountDownLatch(urls.size());
        for (Iterator<URL> it = urls.iterator(); it.hasNext(); it.next()) {
            e.submit(new LatchedTask(it.next(), l));
        }
        l.await();
        System.out.println("Finished fetching all URLs concurrently.");
        return 0; // Dummy return value, we are not asked to do the reduce phase in this Q.
    }

    class LatchedTask implements Runnable {

        private URL url;
        private final CountDownLatch l;

        public LatchedTask(URL url, CountDownLatch l) {
            this.url = url;
            this.l = l;
        }

        @Override
        public void run() {
            Fetcher fetcher = new URLFetcher();
            byte[] content = fetcher.fetch(url);
            l.countDown();
        }
    }


}
