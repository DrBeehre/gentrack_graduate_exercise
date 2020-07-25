public class MeterDataNotification {

    private String version;
    private String CSVIntervalData;
    private String participantRole;

    public MeterDataNotification() {
    }

    public MeterDataNotification(String version, String CSVIntervalData, String participantRole) {
        this.version = version;
        this.CSVIntervalData = CSVIntervalData;
        this.participantRole = participantRole;
    }

    public String getVersion() {
        return version;
    }

    public String getCSVIntervalData() {
        return CSVIntervalData;
    }

    public String getParticipantRole() {
        return participantRole;
    }
}
