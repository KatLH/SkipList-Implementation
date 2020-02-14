/*
*   Written by: Katherine Hurst
*   02/13/2020
*   COP3503 - Hw02: Skip List Implementation
*   Session #1 (2/13): 12pm - 1pm
*   Session #2 ():
*   Session #3 ():
*   Session #4 ():
*   Session #5 ():
*/
import java.io.*;
import java.util.*;


class SkipList {
    static private Random val = new Random();

    static int randomNumGen(int n) {
        return Math.abs(val.nextInt()) % n;
    }

    void insert() {}
    void promote() {}
    void delete() {}
    void search() {}
    void printAll() {}
}


class ProcessCommands {
    /* Function that opens the file and stores commands into an array List */
    List<String> getCommands(String fileName) {
        try {
            if (fileName == null)
            return new ArrayList<String>(0);

            File file = new File(fileName);

            if ( !(file.exists() && file.canRead()) ) {
                System.err.println("File cannot be opened.");
                return new ArrayList<String>(0);
            }

            List<String> commandsList = new ArrayList<String>(32);
            Scanner in = new Scanner(file);

            while(in.hasNextLine()) {
                commandsList.add(in.nextLine());
            }

            in.close();
            return commandsList;
        }
        catch ( Exception e ) {
			e.printStackTrace();
        }
        return null;
    }

    /* Run commands one by one from array List */
    void runCommands(String[] commandArgs, SkipList skiplist) {

        String command = commandArgs[0];

        if (command.trim().equalsIgnoreCase("i")) {}
        else if (command.trim().equalsIgnoreCase("d")) {}
        else if (command.trim().equalsIgnoreCase("s")) {}
        else if (command.trim().equalsIgnoreCase("p")) {}
    }


}


public class Hw02 {
    static void complexityIndicator() {
        System.err.println("ka119724;0;0");
    }

    public static void main(String[] args) throws IOException {
        SkipList skiplist = new SkipList();
        ProcessCommands process = new ProcessCommands();

        String fileName = args[0];
        
        System.out.println("For the input file named " + args[0]);

        String seedRandomNumGen = args[1];

        if(args[1].trim().equalsIgnoreCase("r"))
            System.out.println("With the RNG seeded");
        else
            System.out.println("With the RNG unseeded");

        System.out.println("the current Skip List is shown below:");


        File file = new File(args[0]);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileContent = "";
            int val;

            while((val = br.read()) != -1) {
                fileContent = fileContent + (char) val;
            }
            System.out.print(fileContent);
            br.close();

            List<String> lines = process.getCommands(fileName);
            for(String line : lines) {
                String[] commandArgs = line.split(" ");
                process.runCommands(commandArgs, skiplist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        complexityIndicator();
    }

}