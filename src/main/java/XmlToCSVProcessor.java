import javafx.util.Pair;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlToCSVProcessor {

    public void processFile(File file, String outputLocation) {

        printInfoMessage("Starting to process file.");

        try {
            printInfoMessage("Start parsing XML.");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();


            HeaderData headerData = getHeaderData(doc.getElementsByTagName("Header"));

            Validate.notNull(headerData, "Something went wrong with retreiving the header data from the XML file.");

            List<Transaction> transactions = getTransactionData(doc.getElementsByTagName("Transactions"), doc);

            Validate.notEmpty(transactions, "No transactions found.");

            List<Pair<String, String>> csvOutputs = generateCSVStrings(transactions);

            sendCSVs(csvOutputs, outputLocation);

            printInfoMessage("Parsing complete.");

        } catch (Exception e) {
            printDebugMessage(e.getMessage());
        }
    }

    private void sendCSVs(List<Pair<String, String>> csvStringPairs, String outputPath) throws IOException {

        Validate.notEmpty(csvStringPairs, "CSV list is empty.");

        BufferedWriter bw = null;

        for (Pair<String, String> nameCSVPair : csvStringPairs) {

            File outputCSVFile = null;
            if (outputPath.endsWith("\\")) {
                outputCSVFile = new File(outputPath + nameCSVPair.getKey() + ".csv");
            } else {
                outputCSVFile = new File(outputPath + "\\" + nameCSVPair.getKey() + ".csv");
            }

            Validate.notNull(outputCSVFile);

            try {
                bw = new BufferedWriter(new FileWriter(outputCSVFile));

                bw.write(nameCSVPair.getValue());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bw != null) {
                    bw.close();
                }
            }
        }
    }

    private List<Pair<String, String>> generateCSVStrings(List<Transaction> transactions) {

        // This is an interesting way of doing this, but means I can send back a list of pairs with the
        // name as the key, and string to be sent as a CSV as the value.
        // Then I can test this method without creating a file.
        List<Pair<String, String>> csvNameAndStringValuePair = new ArrayList<Pair<String, String>>();

        for (Transaction transaction : transactions) {

            // Split CSVIntervalData by commons, spaces, tabs and new lines
            String[] subStrings = transaction.getMeterDataNotification().getCSVIntervalData().split("\\n");

            Boolean hasHeader = false;
            Boolean hastrailer = false;

            String currentBlockName = null;
            StringBuilder blockBuilder = null;

            StringBuilder headerRow = getHeader(subStrings);
            StringBuilder tailerRow = getTailer(subStrings);

            Validate.notNull(headerRow.length() != 0, "No header row '100' provided in csv data.");
            Validate.notNull(tailerRow.length() != 0, "No tailing '900' provided in csv data.");

            for (String s : subStrings) {

                s.trim();

                // Split by comma, and get the first element. This will tell us what to do with the row
                String[] strings = s.split(",");
                String leadingStr = strings[0];

                // If 100, then this is the header row or is empty
                if (leadingStr.equals("100") || leadingStr.equals("")) {
                    continue;
                }

                // If 200, then we know we are dealing with a new block
                if (leadingStr.equals("200")) {

                    // If this is true, then we know we already have a block that needs to be written to a file
                    if (currentBlockName != null) {
                        csvNameAndStringValuePair.add(new Pair<>(currentBlockName, headerRow.toString()
                                + System.lineSeparator() + blockBuilder.toString()
                                + tailerRow.toString()));
                    }

                    // set the current block name and reset block builder
                    currentBlockName = strings[1];
                    blockBuilder = new StringBuilder();
                    blockBuilder.append(s).append(System.lineSeparator());
                    continue;
                }

                if (leadingStr.equals("900")) {
                    if (currentBlockName != null) {
                        csvNameAndStringValuePair.add(new Pair<>(currentBlockName, headerRow.toString()
                                + System.lineSeparator() + blockBuilder.toString()
                                + tailerRow.toString()));
                    }
                    continue;
                }

                // if we are here, non of the above conditions were meet
                // and assuming we have set our headers, we can start adding to the current block
                Validate.isTrue(currentBlockName != null, "Current block name is null.");
                Validate.isTrue(blockBuilder != null, "Current block is null.");

                blockBuilder.append(s).append(System.lineSeparator());
            }
        }

        return csvNameAndStringValuePair;
    }

    private StringBuilder getHeader(String[] subStrings) {
        for (String s : subStrings) {

            s.trim();

            // Split by comma, and get the first element. This will tell us what to do with the row
            String[] strings = s.split(",");
            String leadingStr = strings[0];

            if (leadingStr.equals("100")) {
                return new StringBuilder().append(s);
            }
        }

        return null;
    }

    private StringBuilder getTailer(String[] subStrings) {
        for (String s : subStrings) {

            s.trim();

            // Split by comma, and get the first element. This will tell us what to do with the row
            String[] strings = s.split(",");
            String leadingStr = strings[0];

            if (leadingStr.equals("900")) {
                return new StringBuilder().append(s);
            }
        }

        return null;
    }

    private List<Transaction> getTransactionData(NodeList nodeList, Document doc) throws Exception {

        List<Transaction> transactionsToBeReturned = new ArrayList<Transaction>();

        // NOTE: if there is mean't to only be 1 transactions element, the next line can be uncommented
//        Validate.isTrue(nodeList.getLength() == 1);

        printInfoMessage("Getting Transaction Data.");

        for (int i = 0; i < nodeList.getLength(); i++) {

            // This returns a few nodes, but we only care about the 'Transaction' nodes
            NodeList transactionList = nodeList.item(i).getChildNodes();

            // because there should only be 1 Transaction node, I have a counter here to validate on
            int transactionCounter = 0;
            Integer transactionLocation = null;

            for (int j = 0; j < transactionList.getLength(); j++) {
                if (transactionList.item(j).getNodeName().equals("Transaction")) {
                    transactionCounter++;
                    Validate.isTrue(transactionCounter == 1,
                            "There can only be one 'Transaction' element per Transactions element.");

                    transactionLocation = j;
                }
            }

            Validate.notNull(transactionLocation,
                    "Could find a 'Transaction' element inside of 'Transactions");

            Node transactionNode = transactionList.item(transactionLocation);

            if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element transactionElement = (Element) transactionNode;

                // We are using validates here instead of if statements, purely because the input should always
                // contain these elements, so if it doesn't, we need to throw an exception
                Validate.isTrue(transactionElement.getAttribute("transactionDate").length() != 0,
                        "Transaction must contain a transactionDate");

                Validate.isTrue(transactionElement.getAttribute("transactionID").length() != 0,
                        "Transaction must contain a transactionID");

                Validate.isTrue(transactionElement.getElementsByTagName("MeterDataNotification").getLength() != 0,
                        "Transaction must contain a MeterDataNotification element.");

                Transaction transaction = new Transaction();
                transaction.setTransactionDate(transactionElement.getAttribute("transactionDate"));
                transaction.setTransactionID(transactionElement.getAttribute("transactionID"));
                transaction.setMeterDataNotification(getMeterDataNotificationData(transactionElement.getElementsByTagName("MeterDataNotification")));

                Validate.notNull(transaction.getMeterDataNotification(),
                        "MeterDataNotification is null, something went wrong.");

                transactionsToBeReturned.add(transaction);

            } else {
                throw new Exception("Transaction node is not an element node.");
            }

        }

        return transactionsToBeReturned;
    }

    private MeterDataNotification getMeterDataNotificationData(NodeList nodeList) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {

            if (nodeList.item(i).getNodeName().equals("MeterDataNotification")) {

                // At this point, I'm not too bothered with validation. In the real world, I'd crack down all the
                // different possible inputs and validate but as the specifications don't mention anything other than
                // I should have 'CSVIntervalData' here, I'm just going to grab that and run

                Node meterDataNode = nodeList.item(i);

                if (meterDataNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element meterDataElement = (Element) meterDataNode;

                    Validate.isTrue(meterDataElement.getElementsByTagName("CSVIntervalData").getLength() != 0,
                            "CSVIntervalData must be present inside MeterDataNotification element.");

                    return new MeterDataNotification(
                            meterDataElement.getElementsByTagName("CSVIntervalData").item(0).getTextContent());

                } else {
                    throw new Exception("MeterDataNotification node is not an element node.");
                }

            }

        }

        return null;
    }

    /**
     * The idea with this function is to return an object with all the header info within
     * If there was a condition to say, validate that the header info always contains the
     * 'From' and 'To' element, but we didn't care to much about the rest, then it's really
     * easy just to add a validate statement at the end before returning the object
     *
     * @param nodeList
     * @return
     */
    private HeaderData getHeaderData(NodeList nodeList) throws Exception {

        Validate.isTrue(nodeList.getLength() == 1,
                "There should be one Header element within the input XML.");

        Node node = nodeList.item(0); // We know we can use 0 here, as we will only have one node in the nodelist

        HeaderData header = new HeaderData();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // There is probably a far nicer way of doing this,
            // but here we are checking that each element exists before adding it to the object
            if (element.getElementsByTagName("From").getLength() != 0) {
                header.setFrom(element.getElementsByTagName("From").item(0).getTextContent());
            }
            if (element.getElementsByTagName("To").getLength() != 0) {
                header.setTo(element.getElementsByTagName("To").item(0).getTextContent());
            }
            if (element.getElementsByTagName("MessageID").getLength() != 0) {
                header.setMessageID(element.getElementsByTagName("MessageID").item(0).getTextContent());
            }
            if (element.getElementsByTagName("MessageDate").getLength() != 0) {
                header.setMessageDate(element.getElementsByTagName("MessageDate").item(0).getTextContent());
            }
            if (element.getElementsByTagName("TransactionGroup").getLength() != 0) {
                header.setTransactionGroup(element.getElementsByTagName("TransactionGroup").item(0).getTextContent());
            }
            if (element.getElementsByTagName("Priority").getLength() != 0) {
                header.setPriority(element.getElementsByTagName("Priority").item(0).getTextContent());
            }
            if (element.getElementsByTagName("Market").getLength() != 0) {
                header.setMarket(element.getElementsByTagName("Market").item(0).getTextContent());
            }

            return header;

        } else {
            throw new Exception("Header node is not an element node.");
        }
    }

    private void printInfoMessage(String message) {
        System.out.println("INFO: " + message);
    }

    private void printDebugMessage(String message) {
        System.out.println("DEBUG: " + message);
    }

}
