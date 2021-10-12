package output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestInfoList {
    private List<RequestInfo> requestInfoList;
    private List<Long> responseTimeList;

    private static final Double PERCENTILE_99TH = 99.0;

    public RequestInfoList(List<RequestInfo> requestInfos) {
        this.requestInfoList = requestInfos;
        this.responseTimeList = this.makeSortedResponseTimeList(); // this list is sorted
    }

    private List<Long> makeSortedResponseTimeList() {
        List<Long> responseTimeList = new ArrayList<>();
        for (RequestInfo r: this.requestInfoList) {
            responseTimeList.add(r.getLatency());
        }
        Collections.sort(responseTimeList);
        return responseTimeList;
    }

    public long getMedianResponseTime() {
        int middle = this.responseTimeList.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        return this.responseTimeList.get(middle);
    }

    public double getAverageResponseTime() {
        double average = this.responseTimeList.stream().mapToInt(val -> Math.toIntExact(val)).average().orElse(0.0);
        return (double) Math.round(average*100) / 100;
    }

    public double getMaxResponseTime() {
        return this.responseTimeList.get(this.responseTimeList.size() - 1);
//        return Collections.max(this.responseTimeList);
    }

    public long get99thPercentileResponseTime() {
        return this.getPercentileResponseTime(PERCENTILE_99TH);
    }

    private long getPercentileResponseTime(double percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * this.responseTimeList.size());
        return this.responseTimeList.get(index-1);
    }

    public void writeCsv(String csvPath) {
        CsvWriter writer = new CsvWriter(csvPath, this.requestInfoList);
        writer.write();
    }

    public void printReport() {
        String responseTime = "Response Time in millis --- ";
        String average = "AVERAGE: " +  this.getAverageResponseTime();
        String median = ". MEDIAN: " + this.getMedianResponseTime();
        String max = ". MAX: " + this.getMaxResponseTime();
        String percentile99 = ". 99th PERCENTILE: " + this.get99thPercentileResponseTime();
        System.out.println(responseTime + average + median + max + percentile99);
    }
}
