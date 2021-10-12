public class SkierIDRange {
    private int start;
    private int end;

    public SkierIDRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public SkierIDRange(int phaseID, int numSkiers, int numThreads, int threadID) {
        int offset = this.calculateRangeOffset(phaseID, numSkiers);
        this.start = offset + (threadID * (numSkiers / numThreads)) + 1;
        this.end = offset + (threadID + 1) * (numSkiers / numThreads);
    }

    public int generateRandom() {
        return this.getRandomNumber(this.start, this.end);
    }

    private int calculateRangeOffset(int phaseID, int numSkiers) {
        return numSkiers*(phaseID-1);
    }

    @Override
    public String toString() {
        return "SkierIDRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
