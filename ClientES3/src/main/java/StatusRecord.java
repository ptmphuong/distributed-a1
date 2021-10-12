import output.RequestInfo;

import java.util.List;

public class StatusRecord {
    private int totalSuccess;
    private int totalFail;
    private List<RequestInfo> requestInfoList;

    public StatusRecord(int totalSuccess, int totalFail, List<RequestInfo> requestInfoList) {
        this.totalSuccess = totalSuccess;
        this.totalFail = totalFail;
        this.requestInfoList = requestInfoList;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public int getTotalFail() {
        return totalFail;
    }

    public List<RequestInfo> getRequestInfoList() {
        return requestInfoList;
    }

    @Override
    public String toString() {
        return "StatusRecord{" +
                "totalSuccess=" + totalSuccess +
                ", totalFail=" + totalFail +
                '}';
    }
}
