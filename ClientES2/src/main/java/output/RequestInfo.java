package output;

public class RequestInfo {
    private long startTime;
    private long latency;
    private String requestType;
    private int statusCode;

    public RequestInfo(long startTime, long latency, String requestType, int statusCode) {
        this.startTime = startTime;
        this.latency = latency;
        this.requestType = requestType;
        this.statusCode = statusCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLatency() {
        return latency;
    }

    public String getRequestType() {
        return requestType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return startTime + ", " + latency + ", " + requestType + ", " + statusCode;
    }
}
