/*
*   Written by: Katherine Hurst
*   02/13/2020
*   COP3503 - Hw02: Skip List Implementation
*   Session #1 (2/13): 12pm - 1pm = 1
*   Session #2 (2/16): 1pm - 3pm = 2
*   Session #3 (2/28): 9am - 5pm = 8
*   Session #4 (2/29): 5pm - 9pm = 4
*   Session #5 (3/1): 9:30 AM -
*   Session #6 ():
*   Session #7 ():
*   Session #8 ():
*   Session #9 ():
*   Session #10 ():
*/
import java.io.*;
import java.util.*;

class SkipNode {
    public String key;
    public Integer value;
    public SkipNode up, down, left, right;
    public int position;

    public static String negInf = new String("---infinity");
    public static String posInf = new String("+++infinity");

    public SkipNode(String k, Integer v) {
        key = k;
        value = v;
        up = down = left = right = null;
    }

    public String getKey() {
        return key;
    }

    public Integer getVal() {
        return value;
    }

    public boolean equals(Object obj) {
        SkipNode node;

        try {
            node = (SkipNode) obj;
        }
        catch(ClassCastException e) {
            return false;
        }

        return (node.getKey() == key) && (node.getVal() == value);
    }
}

class SkipList {
    public SkipNode head, tail;
    public int numEntriesInSkipList, height, lvl;
    public Random r;

    // Constructor method
    public SkipList() {
        SkipNode neg = new SkipNode(SkipNode.negInf, null);
        SkipNode pos = new SkipNode(SkipNode.posInf, null);

        // Link --Infinity and ++Infinity together
        neg.right = pos;
        pos.left = neg;

        head = neg;
        tail = pos;

        numEntriesInSkipList = 0;
        height = 0;
        lvl = 0;
        r = new Random(42);
    }

    // Constructor method for specified seeded random number generator
    public SkipList(boolean seeded) {
        SkipNode neg = new SkipNode(SkipNode.negInf, null);
        SkipNode pos = new SkipNode(SkipNode.posInf, null);

        // Link --Infinity and ++Infinity together
        neg.right = pos;
        pos.left = neg;

        head = neg;
        tail = pos;

        numEntriesInSkipList = 0;
        height = 0;
        lvl = 0;
        r = new Random();
    }

    public Integer randomNumGen() {
        return r.nextInt() % 2;
    }

    // find(k): find the largest key x <= k on the lowest level of the skip list
    public SkipNode find(String k) {
        SkipNode node = head;
        int nodeLenth = node.right.key.length();
        int kLength = k.length();

        while(true) {
            // Search right until a larger entry is found
            while( (node.right.key) != SkipNode.posInf && (node.right.key).compareTo(k) <= 0) {
                node = node.right;
            }

            // If possible, go down one level
            if(node.down != null)
                node = node.down;
            else
                break; // Lowest level reached
        }

        // If k is FOUND: return reference to entry containing key k
        // If k is NOT FOUND: return reference to next smallest entry of key k
        return node;
    }

    //SEARCH(): Calls find(k) and returns value associated with key k
    public Integer search(String k) {
        SkipNode node = find(k);

        if( k.equals(node.getKey()) ) {
            System.out.println(k + " found");
            return node.value;
        }
        else {
            System.out.println(k + " NOT FOUND");
            return null;
        }
    }

    // Insert(k, val)
    public Integer insert(String k, Integer val) {
        SkipNode newNode;
        SkipNode current = find(k);

        // Check if key is found
        if( k.equals(current.getKey()) ) {
            // Update the value
            Integer prevVal = current.value;
            current.value = val;
            return prevVal;
        }

        // Insert new entry into lowest level
        newNode = new SkipNode(k, val);
        newNode.left = current;
        newNode.right = current.right;
        current.right.left = newNode;
        current.right = newNode;

        while(randomNumGen() == 1) {
            // If top level has been reached, create a new empty top layer
            if(lvl >= height) {
                SkipNode neg = new SkipNode(SkipNode.negInf, null);
                SkipNode pos = new SkipNode(SkipNode.posInf, null);

                neg.right = pos;
                neg.down = head;

                pos.left = neg;
                pos.down = tail;

                head.up = neg;
                tail.up = pos;

                // Update head and tail
                head = neg;
                tail = pos;

                height += 1;
            }

            // Find first current with up link
            while(current.up == null)
                current = current.left;

            current = current.up; // Set current to point to Up current

            SkipNode node = new SkipNode(k, null); // Add node to the column
            node.left = current;
            node.right = current.right;
            node.down = newNode;

            current.right.left = node;
            current.right = node;
            newNode.up = node;

            newNode = node;
            lvl += 1;
        }

        numEntriesInSkipList += 1;
        return null;
    }

    // DELETE(): Removes the key-value pair with a specified key
    public Integer delete (String k) {
        SkipNode node = find(k);

        if( k.equals(node.getKey()) ) {
            while(node != null) {
                node.left.right = node.right;
                node.right.left = node.left;
                node = node.down;
            }
            System.out.println(k + " deleted");
            return 1;
        }
        else {
            System.out.println(k + " integer not found - delete not successful");
            return 0;
        }
    }

    public void printAll() {
        String s = "";
        SkipNode node = head;

        while(node.down != null)
            node = node.down;

        while(node != null) {
            s = getOneColumn(node);
            System.out.println(s);
            node = node.right;
        }
    }

    public String getOneColumn(SkipNode node) {
        String s = "";

        while(node != null) {
            if(node.key == SkipNode.negInf || node.key == SkipNode.posInf) {
                s = s + node.key;
                return s;
            }
            else
                s = s + " " + node.key + "; ";

            node = node.up;
        }

        return s;
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
    void runCommands(String[] strArray, SkipList skiplist) {
        String command = strArray[0];

        if (command.trim().equalsIgnoreCase("i")) {
            String key = strArray[1];
            int value = 0;
            value = Integer.parseInt(strArray[1]);
            skiplist.insert(key, value);
        }
        else if (command.trim().equalsIgnoreCase("d")) {
            String key = strArray[1];
            skiplist.delete(key);
        }
        else if (command.trim().equalsIgnoreCase("s")) {
            String key = strArray[1];
            skiplist.search(key);
        }
        else if (command.trim().equalsIgnoreCase("p")) {
            System.out.println("the current Skip List is shown below: ");
            skiplist.printAll();
            System.out.println("---End of Skip List---");
        }
    }
}


public class Hw02 {
    static void complexityIndicator() {
        // (NID;difficultyRating;hoursSpentOnAssignment)
        System.err.println("ka119724;0;15");
    }

    public static void main(String[] args) throws IOException {
        ProcessCommands process = new ProcessCommands();
        SkipList skiplist;
        boolean r = true;

        File file = new File(args[0]);
        String fileName = args[0];

        System.out.println("\nFor the input file named " + fileName);
        if(args.length == 2 && args[1].trim().equalsIgnoreCase("r")) {
            System.out.println("With the RNG seeded,");
            skiplist = new SkipList(r);
        }
        else {
            System.out.println("With the RNG unseeded,");
            skiplist = new SkipList();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileContents = "";
            int val;

            while((val = br.read()) != -1)
                fileContents = fileContents + (char) val;

            br.close();

            List<String> lines = process.getCommands(fileName);
            for(String line : lines) {
                String[] strArray = line.split(" ");
                process.runCommands(strArray, skiplist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        complexityIndicator();
    }

}