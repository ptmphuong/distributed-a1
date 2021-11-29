import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class CheckPost {
    private final static Logger logger = Logger.getLogger(CheckPost.class.getName());
    private static final int CHECK_ID = -1;
    public static int send(InputInfo info) {
        OkHttpClient client = new OkHttpClient();
        String url = generateUrl(info);
        Request request = buildRequestOk(url);
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            logger.info(String.format("CHECK POST: %s --- statusCode: %d", url, statusCode));
            return statusCode;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private static RequestBody makeRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", 1);
        jsonObject.put("liftID", 1);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        return body;
    }

    private static String generateUrl(InputInfo info) {
        int resortID = 12;
        int seasonID = 2019;
        int dayID = 1;

        UrlBuilder urlBuilder = new UrlBuilder(
                resortID,
                seasonID,
                dayID,
                CHECK_ID,
                info.getIpAddress(),
                info.getWebAppName()
        );

        return urlBuilder.build();
    }

    private static Request buildRequestOk(String url) {
        RequestBody formBody = makeRequestBody();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return request;
    }

}
