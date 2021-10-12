public class StatusRecord {
    private int totalSuccess;
    private int totalFail;

    public StatusRecord(int totalSuccess, int totalFail) {
        this.totalSuccess = totalSuccess;
        this.totalFail = totalFail;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public int getTotalFail() {
        return totalFail;
    }

    @Override
    public String toString() {
        return "StatusRecord{" +
                "totalSuccess=" + totalSuccess +
                ", totalFail=" + totalFail +
                '}';
    }
}
