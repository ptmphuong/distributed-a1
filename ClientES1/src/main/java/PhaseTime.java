public class PhaseTime {
    private int start;
    private int end;

    public PhaseTime(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public PhaseTime(int phaseID) {
        if (phaseID == 1) {
            this.start = 1;
            this.end = 90;
        } else if (phaseID == 2) {
            this.start = 361;
            this.end = 420;
        } else {
            this.start = 91;
            this.end = 360;
        }
    }

    public int generateRandom() {
        return this.getRandomNumber(this.start, this.end);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "PhaseTime{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }


    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
