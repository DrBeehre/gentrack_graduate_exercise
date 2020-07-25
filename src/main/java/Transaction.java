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
}
