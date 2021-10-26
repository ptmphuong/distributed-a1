import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class MainSingleT {
    private final static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Manually create info
        InputInfo info = new InputInfo(
                1, 1, 40, 10, "127.0.0.1", "skier3"
        );;

        logger.info("Start main. Num threads = " + info.getNumThreads());
        logger.info("Start phase");

        long phaseStart = System.currentTimeMillis();

        Phase phase2 = new Phase(2, info, new CountDownLatch(0));
        phase2.execute();
        phase2.shutdown();

        long phaseEnd = System.currentTimeMillis();

        logger.info("End phase");

        String postReport = String.format("PhaseID: %d. Total success: %d, Total fail: %d", phase2.getPhaseID(), phase2.getTotalSuccessPost(), phase2.getTotalFailPost());
        System.out.println(postReport);

        long runTime = phaseEnd - phaseStart;
        int totalPost = phase2.getTotalSuccessPost() + phase2.getTotalFailPost();
        double throughPut = totalPost / (runTime / 1000);
        String timeReport = String.format("Num threads: %d. Total post: %d, Total time (millis): %d", info.getNumThreads(), totalPost, runTime);
        System.out.println(timeReport);
        System.out.println("throughput (req/sec): " + throughPut);
    }
}