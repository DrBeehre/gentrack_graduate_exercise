import javafx.util.Pair;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppMain {

    private static String help = "-h";
    private static String debug = "-d";
    private static String input = "-i";
    private static String output = "-o";

    private static List<String> options = Arrays.asList(help, debug, input, output);

    private static boolean debugMode = false;

    private static String fileRequestString = "Please provide an XML file path: ";
    private static String fileOutputRequestString = "Please provide out csv path: ";

    private static File inputFile = null;
    private static String outputLocation = null;

    public static void main(String[] args) throws IOException {

        // if user has passed in some args, lets see what they are
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                // for each arg, check if its an option or file path
                if (args[i].startsWith("-")) {
                    // if here, then we are dealing with a potential option
                    if (options.contains(args[i])) {
                        if(i + 1 != args.length) {
                            callOption(args[i], args[i + 1]);
                        }else{
                            callOption(args[i], "");
                        }
                    } else {
                        callOption(help, "");
                    }
                }
            }
        }

        if (inputFile == null) {
            // if input file here is null, then we do not have an input file and
            // therefore we need to request one

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(fileRequestString);

            try {
                inputFile = new File(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outputLocation == null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(fileOutputRequestString);

            try {
                outputLocation = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        validateInputFile(inputFile);
        validateOutputDirectory(outputLocation);

        XmlToCSVProcessor xmlToCSVProcessor = new XmlToCSVProcessor();

        xmlToCSVProcessor.processFile(inputFile, outputLocation);
    }

    private static void validateOutputDirectory(String output) {
        File file = new File(output);

        Validate.isTrue(file.exists(), "Output directory provided does not exist.");
    }


    private static void validateInputFile(File file) {

        printInfoMessage("Validating input file exists.");

        //TODO: validate input file exists and can be read in
        Validate.notNull(file, "File was not provided. Please provide an XML file to be processed.");
        Validate.isTrue(file.exists(), "Could not find file with file path {}", file.getAbsolutePath());

        //TODO: validate file is an XML

        printInfoMessage("File " + file.getName() + " existence validation completed.");
    }

    private static void callOption(String option, String path) throws IOException {

        //TODO: probably a cleaner way of doing this but this is fast
        if (option.equals(debug)) {
            // get logger
            debugMode = true;
        } else if (option.equals(help)) {
            // use logger
            printHelp();
        } else if (option.equals(input)) {
            inputFile = new File(path);
        } else if (option.equals(output)) {
            outputLocation = path;
        } else {
            //shouldn't get here
            throw new IOException();
        }
    }

    private static void printHelp() {
        //TODO: complete method
    }

    private static void printInfoMessage(String message) {
        System.out.println("INFO: " + message);
    }

}
