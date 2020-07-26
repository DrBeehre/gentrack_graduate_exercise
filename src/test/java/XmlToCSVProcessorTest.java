import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlToCSVProcessorTest {

    XmlToCSVProcessor xmlToCSVProcessor;

    @BeforeEach
    public void init() {
        this.xmlToCSVProcessor = new XmlToCSVProcessor();
    }

    @Test
    public void processData_test_with_no_header_data_in_xml() {
        File inputFile = new File("Resources/testfile_without_header.xml");

        try {
            this.xmlToCSVProcessor.processFile(inputFile, "");
        } catch (Exception e) {
            assertEquals("Something went wrong with retreiving the header data from the XML file.",
                    e.getMessage());
        }
    }

    @Test
    public void processData_test_with_no_transaction_in_xml() {
        File inputFile = new File("Resources/testfile_without_transactions.xml");

        try {
            this.xmlToCSVProcessor.processFile(inputFile, "");
        } catch (Exception e) {
            assertEquals("No transactions found.",
                    e.getMessage());
        }
    }

    @Test
    public void processData_test_with_no_transactionID_in_xml() {
        File inputFile = new File("Resources/testfile_no_transactionID.xml");

        try {
            this.xmlToCSVProcessor.processFile(inputFile, "");
        } catch (Exception e) {
            assertEquals("Transaction must contain a transactionID",
                    e.getMessage());
        }
    }

    @Test
    public void processData_test_with_no_transactionDate_in_xml() {
        File inputFile = new File("Resources/testfile_no_transactionDate.xml");

        try {
            this.xmlToCSVProcessor.processFile(inputFile, "");
        } catch (Exception e) {
            assertEquals("Transaction must contain a transactionDate",
                    e.getMessage());
        }
    }

    @Test
    public void processData_test_out_is_expected() throws IOException {
        File inputFile = new File("Resources/testfile.xml");
        String outputPath = "src/test/output";

        // files to compare
        File expectedCSVOne = new File("Resources/12345678901.csv");
        File expectedCSVTwo = new File("Resources/98765432109.csv");

        // actual outputfiles
        File actualCSVOne = new File("src/test/output/12345678901.csv");
        File actualCSVTwo = new File("src/test/output/98765432109.csv");

        this.xmlToCSVProcessor.processFile(inputFile, outputPath);

        try {
            assertEquals(FileUtils.readLines(expectedCSVOne), FileUtils.readLines(actualCSVOne));
            assertEquals(FileUtils.readLines(expectedCSVTwo), FileUtils.readLines(actualCSVTwo));
        } finally {
            actualCSVOne.delete();
            actualCSVTwo.delete();
        }
    }
}