import java.util.Scanner;

public class InputInfo {
    private Integer numThreads;
    private Integer numSkiers;
    private Integer numLifts;
    private Integer numRuns;
    private String ipAddress;
    private String webAppName;

    //    private final static int INPUT_LENGTH = 5;
    private final static Integer NUM_THREADS_MIN = 0;
    private final static Integer NUM_THREADS_MAX = 265;
    private final static Integer NUM_SKIERS_MIN = 0;
    private final static Integer NUM_SKIERS_MAX = 100000;
    private final static Integer NUM_LIFTS_MIN = 5;
    private final static Integer NUM_LIFTS_MAX = 60;
    private final static Integer NUM_LIFTS_DEFAULT = 40;
    private final static Integer NUM_RUNS_MIN = 0;
    private final static Integer NUM_RUNS_MAX = 20;
    private final static Integer NUM_RUNS_DEFAULT = 10;


    /**
     * Used with ask() to construct the object via command line.
     */
    public InputInfo() {
    }

    /**
     * Manually construct the object for testing.
     * @param numThreads
     * @param numSkiers
     * @param numLifts
     * @param numRuns
     * @param ipAddress
     * @param webAppName
     */
    public InputInfo(Integer numThreads, Integer numSkiers, Integer numLifts, Integer numRuns, String ipAddress, String webAppName) {
        this.numThreads = numThreads;
        this.numSkiers = numSkiers;
        this.numLifts = numLifts;
        this.numRuns = numRuns;
        this.ipAddress = ipAddress;
        this.webAppName = webAppName;
    }

    public void ask() throws IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("numThreads: ");
        String numThreads = scanner.nextLine();
        this.validateNumThreads(numThreads);

        System.out.print("numSkiers: ");
        String numSkiers = scanner.nextLine();
        this.validateNumSkiers(numSkiers);

        System.out.print("numLifts: ");
        String numLifts = scanner.nextLine();
        this.validateNumLifts(numLifts);

        System.out.print("numRuns: ");
        String numRuns = scanner.nextLine();
        this.validateNumRuns(numRuns);

        System.out.print("Server IP address: ");
        String ipAddress = scanner.nextLine();
        this.setIpAddress(ipAddress);

        System.out.print("Webapp name: ");
        String webAppName = scanner.nextLine();
        this.setWebAppName(webAppName);
    }

    private void validateNumThreads(String arg) throws IllegalArgumentException {
        if (!isInteger(arg)) throw new IllegalArgumentException("numThreads must be an integer");
        Integer numThreads = Integer.parseInt(arg);
        if (numThreads < 0 || numThreads > NUM_THREADS_MAX) {
            throw new IllegalArgumentException(String.format("numThreads must be within %d-%d", NUM_THREADS_MIN, NUM_THREADS_MAX));
        }
        this.numThreads = numThreads;
    }

    private void validateNumSkiers(String arg) throws IllegalArgumentException {
        if (!isInteger(arg)) throw new IllegalArgumentException("numSkiers must be an integer");
        Integer numSkiers = Integer.parseInt(arg);
        if (numSkiers < 0 || numSkiers > NUM_SKIERS_MAX) {
            throw new IllegalArgumentException(String.format("numSkiers must be within %d-%d", NUM_SKIERS_MIN, NUM_SKIERS_MAX));
        }
        this.numSkiers = numSkiers;
    }

    private void validateNumLifts(String arg) {
        if (!isInteger(arg) || Integer.parseInt(arg) < NUM_LIFTS_MIN || Integer.parseInt(arg) > NUM_LIFTS_MAX) {
            System.out.println(String.format(
                    "numLifts must be an integer within %d-%d. Setting this to default: %d",
                    NUM_LIFTS_MIN, NUM_LIFTS_MAX, NUM_LIFTS_DEFAULT)
            );
            System.out.println("If you don't like it, press Ctrl+C to run this again");
            this.numLifts = NUM_LIFTS_DEFAULT;
        } else {
            this.numLifts = Integer.parseInt(arg);
        }
    }

    private void validateNumRuns(String arg) {
        if (!isInteger(arg) || Integer.parseInt(arg) < NUM_RUNS_MIN || Integer.parseInt(arg) > NUM_RUNS_MAX) {
            System.out.println(String.format(
                    "numRuns must be an integer within %d-%d. Setting this to default: %d",
                    NUM_RUNS_MIN, NUM_RUNS_MAX, NUM_RUNS_DEFAULT)
            );
            System.out.println("If you don't like it, press Ctrl+C to run this again");
            this.numRuns = NUM_RUNS_DEFAULT;
        } else {
            this.numRuns = Integer.parseInt(arg);
        }
    }

    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private void setWebAppName(String webAppName) {
        this.webAppName = webAppName;
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }


    public Integer getNumThreads() {
        return numThreads;
    }

    public Integer getNumSkiers() {
        return numSkiers;
    }

    public Integer getNumLifts() {
        return numLifts;
    }

    public Integer getNumRuns() {
        return numRuns;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getWebAppName() {
        return webAppName;
    }

    @Override
    public String toString() {
        return "InputInfo: " +
                "numThreads=" + numThreads +
                ", numSkiers=" + numSkiers +
                ", numLifts=" + numLifts +
                ", numRuns=" + numRuns +
                ", ipAddress='" + ipAddress + '\'' +
                ", webAppName='" + webAppName + '\'' +
                '.';
    }
}