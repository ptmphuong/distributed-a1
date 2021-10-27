import okhttp3.*;
import org.json.JSONObject;
import output.RequestInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class PostTask implements Callable<StatusRecord> {
    private final static Logger logger = Logger.getLogger(Phase.class.getName());

    private OkHttpClient clientOk;
    private int numPost;
    private SkierIDRange skierIDRange;
    private int liftNum;
    private PhaseTime phaseTime;
    private String ipAddress;
    private String webAppName;
    private List<RequestInfo> requestInfoList = new ArrayList<>();

    public PostTask(int numPost, OkHttpClient clientOK, SkierIDRange skierIDRange, PhaseTime phaseTime, int numLifts, String ipAddress, String webAppName) {
//    public PostTask(int numPost, SkierIDRange skierIDRange, PhaseTime phaseTime, int numLifts, String ipAddress, String webAppName) {
        this.numPost = numPost;
        this.clientOk = clientOK;
        this.skierIDRange = skierIDRange;
        this.phaseTime = phaseTime;
        this.liftNum = numLifts;
        this.ipAddress = ipAddress;
        this.webAppName = webAppName;
    }

    public PostTask(int numPost) {
        this.numPost = numPost;
        this.clientOk = new OkHttpClient();
    }

    private boolean sendPostOk(String url) throws IOException {
        Request request = buildRequestOk(url);
        long start = System.currentTimeMillis();
        try(Response response = clientOk.newCall(request).execute()) {
            long end = System.currentTimeMillis();
            int statusCode = response.code();
//            logger.info(String.format("POST: %s --- statusCode: %d --- time: %d", url, statusCode, end - start));
            RequestInfo requestInfo = new RequestInfo(start, end - start, "POST", statusCode);
            this.requestInfoList.add(requestInfo);
            return statusCode == 200;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Request buildRequestOk(String url) {
        RequestBody formBody = makeRequestBody();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return request;
    }

    private RequestBody makeRequestBody() {
        int timeVal = this.phaseTime.generateRandom();
        int liftIDVal = this.generateRandomLiftNum();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", timeVal);
        jsonObject.put("liftID", liftIDVal);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        return body;
    }

    @Override
    public StatusRecord call() throws Exception {
        int totalSuccess = 0;
        int totalFail = 0;
        for (int i = 0; i < this.numPost; i++) {
            String url = this.generateUrl();
            boolean postSuccess = sendPostOk(url);
            if (postSuccess) {
                totalSuccess++;
            }
            else {
                totalFail++;
            }
        }
        return new StatusRecord(totalSuccess, totalFail, this.requestInfoList);
    }

    // build url
    private int generateRandomLiftNum() {
        return this.getRandomNumber(0, this.liftNum);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private String generateUrl() {
        int resortID = 12;
        int seasonID = 2019;
        int dayID = 1;

        UrlBuilder urlBuilder = new UrlBuilder(
                resortID,
                seasonID,
                dayID,
                this.skierIDRange.generateRandom(),
                this.ipAddress,
                this.webAppName
        );

        return urlBuilder.build();
    }
}
