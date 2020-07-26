import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MeterDataNotification")
public class MeterDataNotification {

    private String version;
    private String CSVIntervalData;

    public MeterDataNotification() {
    }

    public MeterDataNotification(String version, String CSVIntervalData) {
        this.version = version;
        this.CSVIntervalData = CSVIntervalData;
    }

    @XmlAttribute(name = "version")
    public String getVersion() {
        return version;
    }

    @XmlElement(name = "CSVIntervalData")
    public String getCSVIntervalData() {
        return CSVIntervalData;
    }
}
