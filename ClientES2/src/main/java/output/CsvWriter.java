package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {
    private String csvOutputPath;
    private List<RequestInfo> responseInfoList;

    public CsvWriter(String csvOutputPath, List<RequestInfo> responseInfoList) {
        this.csvOutputPath = csvOutputPath;
        this.responseInfoList = responseInfoList;
    }

    public void write() {
        File csv = new File(this.csvOutputPath);
        try (PrintWriter printWriter = new PrintWriter(csv)) {
            printWriter.println("startTime, latency, requestType, statusCode");
            this.responseInfoList.stream()
                    .map(RequestInfo::toString)
                    .forEach(printWriter::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
