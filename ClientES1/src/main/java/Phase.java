import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Phase {

    private final static Logger logger = Logger.getLogger(Phase.class.getName());

    private ExecutorService executorService;
    private CompletionService<StatusRecord> completionService;
    private int totalSuccessPost;
    private int totalFailPost;
    private int phaseID;
    private int numThreads;
    private PhaseTime phaseTime;
    private int numPostPerThread;
    private InputInfo inputInfo;
    private CountDownLatch signalCdl;


    // client
    private int NUM_THREAD_PER_CLIENT = 16;
    private List<OkHttpClient> clientList = new ArrayList<>();

    public Phase(int phaseID, InputInfo info, CountDownLatch cdlSignal) {
        this.phaseID = phaseID;
        this.setNumThreads(info.getNumThreads(), phaseID);
        this.phaseTime = new PhaseTime(phaseID);
        this.setNumPostPerThread(phaseID, info.getNumRuns(), info.getNumSkiers(), this.numThreads);
        this.inputInfo = info;
        this.setClientList();
        this.signalCdl = cdlSignal;
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.completionService = new ExecutorCompletionService<>(this.executorService);
    }

    private void setClientList() {
        int numClient = this.numThreads < NUM_THREAD_PER_CLIENT ? 1 : (this.numThreads/NUM_THREAD_PER_CLIENT);
        for (int i = 0; i < numClient; i++) {
            OkHttpClient client = new OkHttpClient();
            this.clientList.add(client);
        }
    }

    public void execute() throws ExecutionException, InterruptedException {
        logger.info("Phase " + this.phaseID + " start");
        submitTask();
        updateStatusRecord();
        shutdown();
        logger.info("Phase " + this.phaseID + " ends");
    }

    private void submitTask() throws InterruptedException {
//        logger.info("Phase " + this.phaseID + " submits Task");
        for (int i = 0; i < this.numThreads; i++) {
            SkierIDRange skierIDRange = new SkierIDRange(this.phaseID, inputInfo.getNumSkiers(), this.numThreads, i);
            int cliendInd = i == 0 ? 0 : i/NUM_THREAD_PER_CLIENT;
            PostTask postTask = new PostTask(
                    this.numPostPerThread,
                    this.clientList.get(cliendInd),
                    skierIDRange,
                    this.phaseTime,
                    inputInfo.getNumLifts(),
                    inputInfo.getIpAddress(),
                    inputInfo.getWebAppName()
            );
//            logger.info("Submit Phase " + this.phaseID + "Thread id: " + i);
            completionService.submit(postTask);
            signalCdl.countDown();
        }
//        logger.info("Phase " + this.phaseID + " submits Task DONE");
    }

    private void updateStatusRecord() throws InterruptedException, ExecutionException {
//        logger.info("Phase " + this.phaseID + " updates Task results");
        for (int i = 0; i < this.numThreads; i++) {
            Future<StatusRecord> statusRecordFuture = completionService.take();
            StatusRecord statusRecord = statusRecordFuture.get();
            this.totalSuccessPost += statusRecord.getTotalSuccess();
            this.totalFailPost += statusRecord.getTotalFail();
        }
//        logger.info("Phase " + this.phaseID + " updates Task results DONE");
    }

    private void shutdown() {
        executorService.shutdown();
        logger.info("Phase " + this.phaseID + " shuts down DONE");
    }

    public int getTotalSuccessPost() {
        return this.totalSuccessPost;
    }

    public int getTotalFailPost() {
        return this.totalFailPost;
    }

    public int getTotalPost() {
        return this.totalFailPost + this.totalSuccessPost;
    }

    public int getPhaseID() {
        return this.phaseID;
    }

    public int getNumThreads() {
        return this.numThreads;
    }

    public int getNumPostPerThread() {
        return this.numPostPerThread;
    }

    public PhaseTime getPhaseTime() {
        return phaseTime;
    }

    private void setNumThreads(int numThreads, int phaseID) {
        if (phaseID == 1 || phaseID == 3) {
            this.numThreads = numThreads/4;
        } else {
            this.numThreads = numThreads;
        }
    }

    private void setNumPostPerThread(int phaseID, int numRuns, int numSkiers, int thisNumThreads) {
        double multiplier = getNumRunsMultiplier(phaseID);
        double numPost = (numRuns * multiplier) * (numSkiers / thisNumThreads);
        this.numPostPerThread = (int) numPost;
    }

    private double getNumRunsMultiplier(int phaseID) {
        if (phaseID == 1) return 0.2;
        if (phaseID == 3) return 0.1;
        else return 0.6;
    }

    public int getNUM_THREAD_PER_CLIENT() {
        return NUM_THREAD_PER_CLIENT;
    }
}
