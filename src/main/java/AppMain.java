import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
                        if (i + 1 != args.length) {
                            callOption(args[i], args[i + 1]);
                        } else {
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

        Validate.notNull(file, "File was not provided. Please provide an XML file to be processed.");
        Validate.isTrue(file.exists(), "Could not find file with file path {}", file.getAbsolutePath());

        printInfoMessage("File " + file.getName() + " existence validation completed.");
    }

    private static void callOption(String option, String path) throws IOException {

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
        StringBuilder helpStringBuilder = new StringBuilder();
        helpStringBuilder.append("Welcome to the ultimate XML to CSV java processing app! (super custom)").append(System.lineSeparator());
        helpStringBuilder.append(System.lineSeparator());
        helpStringBuilder.append("This app can be ran in 2 ways. Either all arguements can be passed in when calling through command line.").append(System.lineSeparator());
        helpStringBuilder.append("Or you can run it and pass in the require input and output directories when asked.").append(System.lineSeparator());
        helpStringBuilder.append(System.lineSeparator());
        helpStringBuilder.append("Example - java AppMain -i [xml file path] -o [output directory]").append(System.lineSeparator());
        helpStringBuilder.append(System.lineSeparator());
        helpStringBuilder.append("Option you can use are:").append(System.lineSeparator());
        helpStringBuilder.append(System.lineSeparator());
        helpStringBuilder.append("-i [xml file path]").append(System.lineSeparator());
        helpStringBuilder.append("-o [output directory]").append(System.lineSeparator());
        helpStringBuilder.append("-d - this enables extra debugging info that is printed to the console. Not fully implemented").append(System.lineSeparator());
        helpStringBuilder.append("-h - for this lovely menu").append(System.lineSeparator());

        System.out.println(helpStringBuilder.toString());
    }

    private static void printInfoMessage(String message) {
        System.out.println("INFO: " + message);
    }

}
