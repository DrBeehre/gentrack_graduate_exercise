import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Transaction")
public class Transaction {

    private String transactionID;
    private String transactionDate;
    private MeterDataNotification meterDataNotification;

    public Transaction() {
    }

    public Transaction(String transactionID, String transactionDate, MeterDataNotification meterDataNotification) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.meterDataNotification = meterDataNotification;
    }

    @XmlAttribute(name = "transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @XmlAttribute(name = "transactionDate")
    public String getTransactionDate() {
        return transactionDate;
    }

    @XmlElement(name = "MeterDataNotification")
    public MeterDataNotification getMeterDataNotification() {
        return meterDataNotification;
    }
}
