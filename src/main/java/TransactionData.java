import java.util.List;

public class TransactionData {

    private HeaderData headerData;
    private List<Transaction> transactions;

    public TransactionData() {
    }

    public TransactionData(HeaderData headerData, List<Transaction> transactions) {
        this.headerData = headerData;
        this.transactions = transactions;
    }

    public HeaderData getHeaderData() {
        return headerData;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
