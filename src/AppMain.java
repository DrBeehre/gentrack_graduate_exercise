import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class AppMain {

    private static String help = "-h";
    private static String debug = "-d";

    private static ArrayList<String> options = (ArrayList<String>) Arrays.asList("-d", "-h");

    private static boolean debugMode = false;

    private static String fileRequestString = "Please provide an XML file path:";

    public static void main(String[] args) {

        File inputFile = null;

        // if user has passed in some args, lets see what they are
        if(args.length > 0){
            for (String arg: args) {
                // for each arg, check if its an option or file path
                if(arg.substring(1).equals("-")){
                    // if here, then we are dealing with a potential option
                    if(options.contains(arg)){
                        callOption(arg);
                    }else{
                        callOption(help);
                    }
                }else{
                    // if here, then we aren't dealing with an option and can see if its a file

                    inputFile = new File(arg);
                    // add validation to check file exists
                    // validate file exists
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
        //TODO: process file
    }

    private static void validateInputFile(File file){
        //TODO: validate input file exists and can be read in

        //TODO: validate file is an XML
    }

    private static void callOption(String option){

        //TODO: probably a cleaner way of doing this but this is fast
        if(option.equals(debug)){
            // get logger
            debugMode = true;
        }else if(option.equals(help)){
            // use logger
            printHelp();
        }else{
            //TODO: throw error to user
        }
    }

    private static void printHelp(){
        //TODO: complete method
    }

}
