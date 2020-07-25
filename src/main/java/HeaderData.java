public class HeaderData {

    private String From;
    private String To;
    private String MessageID;
    private String MessageDate;
    private String TransactionGroup;
    private String Priority;
    private String Market;

    public HeaderData() {}

    public HeaderData(String from,
                                 String to,
                                 String messageID,
                                 String messageDate,
                                 String transactionGroup,
                                 String priority,
                                 String market) {
        From = from;
        To = to;
        MessageID = messageID;
        MessageDate = messageDate;
        TransactionGroup = transactionGroup;
        Priority = priority;
        Market = market;
    }

    // NOTE: we only need getters here, because JAXB auto generates and uses setters and getters with the
    // '' annotation. But we want to get data from this, se we want getters to use ourselves.

    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getMessageDate() {
        return MessageDate;
    }

    public String getTransactionGroup() {
        return TransactionGroup;
    }

    public String getPriority() {
        return Priority;
    }

    public String getMarket() {
        return Market;
    }
}

