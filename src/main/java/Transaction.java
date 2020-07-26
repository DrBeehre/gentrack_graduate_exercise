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

    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public MeterDataNotification getMeterDataNotification() {
        return meterDataNotification;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setMeterDataNotification(MeterDataNotification meterDataNotification) {
        this.meterDataNotification = meterDataNotification;
    }
}
