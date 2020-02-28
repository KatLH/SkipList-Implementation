/*
*   Written by: Katherine Hurst
*   02/13/2020
*   COP3503 - Hw02: Skip List Implementation
*   Session #1 (2/13): 12pm - 1pm
*   Session #2 (2/16): 1pm - 3pm
*   Session #3 ():
*   Session #4 ():
*   Session #5 ():
*/
import java.io.*;
import java.util.*;

class NewEntry {
    public String key;
    public Integer val;
    public int position;
    public NewEntry up, down, left, right;

    public static String negInf = new String("--infinity"); 
    public static String posInf = new String("++infinity");

    public NewEntry(String k, Integer v) {
        key = k;
        val = v;
        up = down = left = right = null;
    }

    public Integer getVal() {
        return val;
    }

    public String getKey() {
        return key;
    }

    public Integer setVal(Integer newVal) {
        Integer oldVal = val;
        val = newVal;
        return oldVal;
    }

    public boolean equals(Object obj) {
        NewEntry entry;

        try {
            entry = (NewEntry) obj;
        }
        catch (ClassCastException ex) {
            return false;
        }
        return (entry.getKey() == key) && (entry.getVal() == val);
    }

    public String convertToString() {
        return "(" + key + "," + val + ")";
    }
    
}

class SkipList {
    public NewEntry head;
    public NewEntry tail;
    public int numEntriesInSkipList;
    public int height;
    public Random r;

    public int randomNumGen(boolean seedBool) {
        if(seedBool)
            return Math.abs(r.nextInt()) % 2;
        else {
            r.setSeed(42);
            return Math.abs(r.nextInt()) % 2;
        }
    }

    // Constructor method
    public SkipList() {
        NewEntry neg, pos;
        NewEntry n1 = new NewEntry(NewEntry.negInf, null);
        NewEntry n2 = new NewEntry(NewEntry.posInf, null);

        head = n1;
        tail = n2;

        n1.right = n2;
        n2.left = n1;

        numEntriesInSkipList = 0;
        height = 0;
        r = new Random();
    }

    // Returns the number of entries in the table
    public int getSize() {
        return numEntriesInSkipList;
    }

    // Returns if the table is empty or not
    public boolean empty() {
        return(numEntriesInSkipList == 0);
    }

    // Finds the largest key on the lowest level
    public NewEntry find(String key) {
        NewEntry current = head;

        while(true) {
            while(current.right.key != NewEntry.posInf && current.right.key.compareTo(key) <= 0) {
                current = current.right;
                System.out.println(current.key); // FIXME
            }

            if(current.down != null) {
                current = current.down;
                System.out.println(current.key); // FIXME
            }
            else
                break;
        }
        return(current);
    }

    // Returns value associated with key
    public Integer search(String key) {
        NewEntry current = find(key);
        if(key.equals(current.getKey()))
            return(current.val);
        else
            return(null);
    }


    public NewEntry promote(NewEntry a, NewEntry b, String key) {
        NewEntry node = new NewEntry(key, null);

        node.left = a;
        node.right = a.right;
        node.down = b;

        a.right.left = node;
        a.right = node;
        b.up = node;

        return node;
    }

    public Integer insert(String key, Integer val) {
        NewEntry a, b;
        int i;
        a = find(key);
        System.out.println("find(" + key + ") returns: " + a.key); // FIXME

        if(key.equals(a.getKey())) {
            Integer old = a.val;
            a.val = val;
            return(old);
        }

        // Inserts new entry
        b = new NewEntry(key, val);
        b.left = a;
        b.right = a.right;
        a.right.left = b;
        a.right = b;

        i = 0;
        while(r.nextDouble() < 0.5) {

            if(i >= height) {
                NewEntry neg, pos;
                height = height + 1;

                neg = new NewEntry(NewEntry.negInf, null);
                pos = new NewEntry(NewEntry.posInf, null);

                neg.right = pos;
                neg.down  = head;

                pos.left = neg;
                pos.down = tail;

                head.up = neg;
                tail.up = pos;

                head = neg;
                tail = pos;
            }

            while(a.up == null) {
                a = a.left;
            }
            a = a.up;

            NewEntry node = new NewEntry(key, null);

            node.left = a;
            node.right = a.right;
            node.down = b;

            a.right.left = node;
            a.right = node;
            b.up = node;

            b = node;
            i = i + 1;
        }

        numEntriesInSkipList = numEntriesInSkipList + 1;
        return null;
    }

    public Integer delete(String key) {
        return null;
    }

    public void printHorizontal() {
        String str = "";
        int i;
        NewEntry current = head;

        while(current.down != null) {
            current = current.down;
        }

        i = 0;
        while(current != null) {
            current.position = i++;
            current = current.right;
        }

        current = head;
        while(current != null) {
            str = getOneRow(current);
            System.out.println(str);
            current = current.down;
        }
    }

    public void printVertical() {
        String s = "";
        NewEntry p = head;

        while ( p.down != null )
            p = p.down;

        while ( p != null ) {
            s = getOneColumn( p );
            System.out.println(s);

            p = p.right;
        }
    }

    public String getOneColumn(NewEntry p) {
        String s = "";

        while ( p != null ) {
            s = s + " " + p.key;
            p = p.up;
        }

        return(s);
    }

    public String getOneRow(NewEntry current) {
        String str;
        int a, b, i;

        a = 0;
        str = "" + current.key;
        current = current.right;

        while(current != null) {
            NewEntry x;
            x = current;

            while(x.down != null) {
                x = x.down;
            }

            b = x.position;
            str = str + " <-";

            for(i = a + 1; i < b; i++)
                str = str + "-------";

            str = str + "> " + current.key;
            a = b;
            current = current.right;
            
        }

        return str;
    }
}

class ProcessCommands {

    // Opens the file and stores commands into an array List
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

    // Runs commands one by one from array List
    void runCommands(String[] commandArgs, SkipList skiplist) {

        String command = commandArgs[0];
        String key = commandArgs[1];
        Integer value = Integer.parseInt(commandArgs[1]);
        int result = 0;

        if (command.trim().equalsIgnoreCase("i")) {
            skiplist.insert(key, value);
        }
        else if (command.trim().equalsIgnoreCase("d")) {
            //result = skiplist.delete(key);
        }
        else if (command.trim().equalsIgnoreCase("s")) {
            skiplist.search(key);
        }
        else if (command.trim().equalsIgnoreCase("p")) {
            skiplist.printHorizontal();
            skiplist.printVertical();
        }
    }
}


public class Hw02 {
    static void complexityIndicator() {
        System.err.println("ka119724;0;0");
    }

    public static void main(String[] args) throws IOException {
        SkipList skiplist = new SkipList();
        ProcessCommands process = new ProcessCommands();
        boolean seedBool = false;

        File file = new File(args[0]);
        String fileName = args[0];

        System.out.println("\n\nFor the input file named " + fileName);
        if(args.length == 2 && args[1].trim().equalsIgnoreCase("r")) {
            System.out.println("With the RNG seeded,");
            seedBool = true;
            randomNumGen(seedBool);
        }
        else
            System.out.println("With the RNG unseeded,");
        System.out.println("the current Skip List is shown below:");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String fileContents = "";
            int val;

            while((val = br.read()) != -1)
                fileContents = fileContents + (char) val;

            //System.out.print(fileContents);
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