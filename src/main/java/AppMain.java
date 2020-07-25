import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

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

    private static void processFile(File file){

        printDebugMessage("Starting to process file.");



    }

    private static void validateInputFile(File file){

        printDebugMessage("Validating input file exists.");

        //TODO: validate input file exists and can be read in
        Validate.notNull(file, "File was not provided. Please provide an XML file to be processed.");
        Validate.isTrue(file.exists(), "Could not find file with file path {}", file.getAbsolutePath());

        //TODO: validate file is an XML

        printDebugMessage("File " + file.getName()+ " existence validation completed.");
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

    private static void printDebugMessage(String message){
        if(debugMode){
            System.out.println(message);
        }
    }

    private static void printHelp(){
        //TODO: complete method
    }

}
