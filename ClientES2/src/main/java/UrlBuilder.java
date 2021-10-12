public class UrlBuilder {
    private Integer resortID;
    private Integer seasonID;
    private Integer dayID;
    private Integer skierID;
    private String ipAddress;
    private String webAppName;

    public UrlBuilder(Integer resortID, Integer seasonID, Integer dayID, Integer skierID, String ipAddress, String webAppName) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.ipAddress = ipAddress;
        this.webAppName = webAppName;
    }

    public String build() {
        final String HTTP = "http://";
        final String TOMCAT_PORT = "8080";
        final String SKIERS = "skiers";
        final String SEASONS = "seasons";
        final String DAYS = "days";
        String domain = HTTP + this.ipAddress + ":" + TOMCAT_PORT + "/" + webAppName;
        String tail =  "/" + SKIERS + "/" + this.resortID + "/" + SEASONS + "/" + this.seasonID + "/" + DAYS + "/" + this.dayID + "/" + SKIERS + "/" + this.skierID;
        return domain + tail;
    }

}
