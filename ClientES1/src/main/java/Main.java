import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {
    private final static Logger logger =
            Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        logger.info("Prompt Input Info");
        // Ask for parameters via command line
//        InputInfo info = new InputInfo();
//        info.ask();

        // Manually create info
        InputInfo info = new InputInfo(
                16, 80, 40, 10, "23.22.233.19", "skier3"
        );

        int numThreads = info.getNumThreads();
        int numThreadsQuarter = numThreads/4;


        CountDownLatch cld1 = new CountDownLatch(divideRoundUp(numThreadsQuarter, 10));
        CountDownLatch cld2 = new CountDownLatch(divideRoundUp(numThreads, 10));

        logger.info("Start main. Num threads = " + numThreads);
        logger.info("Start phase");

        Phase phase1 = new Phase(1, info, cld1);
        Phase phase2 = new Phase(2, info, cld2);
        Phase phase3 = new Phase(3, info, new CountDownLatch(0));

        long start = System.currentTimeMillis();

        phase1.execute();
        cld1.await();
        phase2.execute();
        cld2.await();
        phase3.execute();

        phase1.shutdown();
        phase2.shutdown();
        phase3.shutdown();

        long end = System.currentTimeMillis();

        logger.info("End phase");

        List<Phase> allPhases = new ArrayList<>(Arrays.asList(phase1, phase2, phase3));
        int totalPost = 0;

        System.out.println(info);
        System.out.println(String.format("Client setting: %d threads share 1 client", phase1.getNUM_THREAD_PER_CLIENT()));

        for (Phase p: allPhases) {
            String postReport = String.format("PhaseID: %d. Total success: %d, Total fail: %d", p.getPhaseID(), p.getTotalSuccessPost(), p.getTotalFailPost());
            System.out.println(postReport);
            totalPost += p.getTotalSuccessPost();
            totalPost += p.getTotalFailPost();
        }

        long runTime = end - start;
        double throughPut = totalPost / (runTime/1000);
        String timeReport = String.format("Total post: %d, Total time (millis): %d", totalPost, runTime);
        System.out.println(timeReport);
        System.out.println("throughput (req/sec): " + throughPut);

    }

    private static int divideRoundUp(int num, int divider) {
        if (num % divider != 0) return num/divider + 1;
        else return num/divider;
    }
}
