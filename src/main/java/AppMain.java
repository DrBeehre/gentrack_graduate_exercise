import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AppMain {

    private static String help = "-h";
    private static String debug = "-d";

    private static List<String> options = Arrays.asList("-d", "-h");

    private static boolean debugMode = false;

    private static String fileRequestString = "Please provide an XML file path:";

    public static void main(String[] args) throws IOException {

        File inputFile = null;

        // if user has passed in some args, lets see what they are
        if(args.length > 0){
            for (String arg: args) {
                // for each arg, check if its an option or file path
                if(arg.startsWith("-")){
                    // if here, then we are dealing with a potential option
                    if(options.contains(arg)){
                        callOption(arg);
                    }else{
                        callOption(help);
                    }
                }else{
                    // if here, then we aren't dealing with an option and can see if its a file
                    inputFile = new File(arg);
                }
            }
        }

        if(inputFile == null){
            // if input file here is null, then we do not have an input file and
            // therefore we need to request one

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(fileRequestString);

            try {
                inputFile = new File(reader.readLine());
            }catch (IOException e){
                //TODO: think of a better way
                e.printStackTrace();
            }
        }

        validateInputFile(inputFile);

        processFile(inputFile);
    }

    private static void processFile(File file) {

        printInfoMessage("Starting to process file.");

        try {
            printInfoMessage("Start unmarshalling.");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();


            HeaderData headerData = getHeaderData(doc.getElementsByTagName("Header"));

            Validate.notNull(headerData, "Something went wrong with retreiving the header data from the XML file.");

            List<Transaction> transactions = getTransactionData(doc.getElementsByTagName("Transactions"), doc);

            Validate.notEmpty(transactions, "No transactions found.");

            List<File> csvOutputs = generateCSVFiles(transactions);

            printInfoMessage("Unmarshalling complete.");

        }catch (Exception e){
            printDebugMessage(e.getMessage());
        }

    }

    private static List<File> generateCSVFiles(List<Transaction> transactions){
        //TODO: complete generateCSVFiles method
        return null;
    }

    private static List<Transaction> getTransactionData(NodeList nodeList, Document doc) throws Exception {

        List<Transaction> transactionsToBeReturned = new ArrayList<Transaction>();

        // NOTE: if there is mean't to only be 1 transactions element, the next line can be uncommented
//        Validate.isTrue(nodeList.getLength() == 1);

        printInfoMessage("Getting Transaction Data.");

        for(int i = 0; i < nodeList.getLength(); i++){

            // This returns a few nodes, but we only care about the 'Transaction' nodes
            NodeList transactionList = nodeList.item(i).getChildNodes();

            // because there should only be 1 Transaction node, I have a counter here to validate on
            int transactionCounter = 0;
            Integer transactionLocation = null;

            for (int j = 0; j < transactionList.getLength(); j++) {
                if(transactionList.item(j).getNodeName().equals("Transaction")){
                    transactionCounter++;
                    Validate.isTrue(transactionCounter == 1,
                            "There can only be one 'Transaction' element per Transactions element.");

                    transactionLocation = j;
                }
            }

            Validate.notNull(transactionLocation,
                    "Could find a 'Transaction' element inside of 'Transactions");

            Node transactionNode = transactionList.item(transactionLocation);

            if(transactionNode.getNodeType() == Node.ELEMENT_NODE){
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

            }else{
                throw new Exception("Transaction node is not an element node.");
            }

        }

        return transactionsToBeReturned;
    }

    private static MeterDataNotification getMeterDataNotificationData(NodeList nodeList) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {

            if(nodeList.item(i).getNodeName().equals("MeterDataNotification")){

                // At this point, I'm not too bothered with validation. In the real world, I'd crack down all the
                // different possible inputs and validate but as the specifications don't mention anything other than
                // I should have 'CSVIntervalData' here, I'm just going to grab that and run

                Node meterDataNode = nodeList.item(i);

                if(meterDataNode.getNodeType() == Node.ELEMENT_NODE){
                    Element meterDataElement = (Element) meterDataNode;

                    Validate.isTrue(meterDataElement.getElementsByTagName("CSVIntervalData").getLength() != 0,
                            "CSVIntervalData must be present inside MeterDataNotification element.");

                    return new MeterDataNotification(
                            meterDataElement.getElementsByTagName("CSVIntervalData").item(0).getTextContent());

                }else{
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
    private static HeaderData getHeaderData(NodeList nodeList) throws Exception {

        Validate.isTrue(nodeList.getLength() == 1,
                "There should be one Header element within the input XML.");

        Node node = nodeList.item(0); // We know we can use 0 here, as we will only have one node in the nodelist

        HeaderData header = new HeaderData();

        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;

            // There is probably a far nicer way of doing this,
            // but here we are checking that each element exists before adding it to the object
            if(element.getElementsByTagName("From").getLength() != 0){
                header.setFrom(element.getElementsByTagName("From").item(0).getTextContent());
            }
            if(element.getElementsByTagName("To").getLength() != 0){
                header.setFrom(element.getElementsByTagName("To").item(0).getTextContent());
            }
            if(element.getElementsByTagName("MessageID").getLength() != 0){
                header.setFrom(element.getElementsByTagName("MessageID").item(0).getTextContent());
            }
            if(element.getElementsByTagName("MessageDate").getLength() != 0){
                header.setFrom(element.getElementsByTagName("MessageDate").item(0).getTextContent());
            }
            if(element.getElementsByTagName("TransactionGroup").getLength() != 0){
                header.setFrom(element.getElementsByTagName("TransactionGroup").item(0).getTextContent());
            }
            if(element.getElementsByTagName("Priority").getLength() != 0){
                header.setFrom(element.getElementsByTagName("Priority").item(0).getTextContent());
            }
            if(element.getElementsByTagName("Market").getLength() != 0){
                header.setFrom(element.getElementsByTagName("Market").item(0).getTextContent());
            }

            return header;

        }else{
            throw new Exception("Header node is not an element node.");
        }
    }

    private static void validateInputFile(File file){

        printInfoMessage("Validating input file exists.");

        //TODO: validate input file exists and can be read in
        Validate.notNull(file, "File was not provided. Please provide an XML file to be processed.");
        Validate.isTrue(file.exists(), "Could not find file with file path {}", file.getAbsolutePath());

        //TODO: validate file is an XML

        printInfoMessage("File " + file.getName()+ " existence validation completed.");
    }

    private static void callOption(String option) throws IOException {

        //TODO: probably a cleaner way of doing this but this is fast
        if(option.equals(debug)){
            // get logger
            debugMode = true;
        }else if(option.equals(help)){
            // use logger
            printHelp();
        }else{
            //shouldn't get here
            throw new IOException();
        }
    }

    private static void printInfoMessage(String message){
        System.out.println("INFO: " + message);
    }

    private static void printDebugMessage(String message){
        if(debugMode){
            System.out.println("DEBUG: " + message);
        }
    }

    private static void printHelp(){
        //TODO: complete method
    }

}
