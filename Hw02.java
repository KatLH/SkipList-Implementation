/*
*   Written by: Katherine Hurst
*   02/13/2020
*   COP3503 - Hw02: Skip List Implementation
*   Session #1 (2/13): 12pm - 1pm
*   Session #2 (2/16): 1pm - 3pm
*   Session #3 (2/28): 9am - 5pm
*   Session #4 ():
*   Session #5 ():
*/
import java.io.*;
import java.util.*;

class SkipNode {
    public String key;
    public Integer value;
    public SkipNode up, down, left, right;
    public int position;

    public static String negInf = new String("--infinity"); 
    public static String posInf = new String("++infinity");

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

    public boolean equals(Object o) {
        SkipNode node;

        try {
            node = (SkipNode) o;
        }
        catch(ClassCastException e) {
            return false;
        }

        return (node.getKey() == key) && (node.getVal() == value);
    }
}

class SkipList {
    public SkipNode head;
    public SkipNode tail;
    public int numEntriesInSkipList;
    public int height;
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
        r = new Random();
    }

    public Integer randomNumGen(boolean seeded) {
        if(seeded == false) {
            r.setSeed(42);
            return r.nextInt() % 2;
        }
        else
            return r.nextInt() % 2;
    }

    // find(k): find the largest key x <= k on the lowest level of the skip list
    public SkipNode find(String k) {
        SkipNode node = head;

        while(true) {
            // Search right until a larger entry is found
            while( (node.right.key) != SkipNode.posInf && (node.right.key).compareTo(k) <= 0 ) {
                node = node.right;
            }

            // If possible, go down one level
            if(node.down != null)
                node = node.down;
            else
                break; // Lowest level reached
        }

        /*
         * If k is FOUND: return reference to entry containing key k
         * If k is NOT FOUND: return reference to next smallest entry of key k
         */ 
        return node;
    }

    //SEARCH(): Calls find(k) and returns value associated with key k
    public Integer search(String k) {
        SkipNode node = find(k);

        if( k.equals(node.getKey()) )
            return node.value;
        else
            return null;
    }

    // Insert(k, val)
    public Integer insert(String k, Integer val, boolean seeded) {
        SkipNode p, q;
        int lvl = 0;

        // Check if key is found
        p = find(k);
        if( k.equals(p.getKey()) ) {
            return val;
            // Update the value
            /*
            Integer prevVal = p.value;
            p.value = val;
            return prevVal;
            */
        }

        // Insert new entry into lowest level
        q = new SkipNode(k, val);
        q.left = p;
        q.right = p.right;
        p.right.left = q;
        p.right = q;

        while(randomNumGen(seeded) == 1) {
            // If top level has been reached, create a new empty top layer
            if(lvl >= height) {
                System.out.println("\tCreate Empty Top Layer:\nlvl: " + lvl + " >= height: " + height); // FIXME

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

                System.out.println("\theight = " + height); // FIXME
            }

            // Find first node with up link
            while(p.up == null)
                p = p.left;

            p = p.up; // Set p to point to Up node

            SkipNode node = new SkipNode(k, null); // Add node to the column
            node.left = p;
            node.right = p.right;
            node.down = q;

            p.right.left = node;
            p.right = node;
            q.up = node;

            q = node;
            lvl += 1;
            System.out.println("\tlvl = " + lvl); // FIXME
        }

        numEntriesInSkipList += 1;

        return null; 
    }

    // DELETE(): Removes the key-value pair with a specified key
    public Integer delete (String key) {
        return null;
    }

    public void printAll() {
        String s = "";
        SkipNode p = head;

        while(p.down != null)
            p = p.down;

        while(p != null) {
            s = getOneColumn(p);
            System.out.println(s);
            p = p.right;
        }
    }

    public String getOneColumn(SkipNode p) {
        String s = "";

        if(p.up == null)
            s = s + p.key;
        else {
            while(p != null) {
            //s = " " + p.key;
            s = s + p.key + "; ";
            p = p.up;
            }
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
    void runCommands(String[] strArray, SkipList skiplist, boolean seeded) {
        String command = strArray[0];

        if (command.trim().equalsIgnoreCase("i")) {
            String key = strArray[1];
            int value = 0;
            value = Integer.parseInt(strArray[1]);
            skiplist.insert(key, value, seeded);
        }
        else if (command.trim().equalsIgnoreCase("d")) {
            //int value = 0;
            //value = Integer.parseInt(strArray[1]);
            //result = skiplist.delete(key);
        }
        else if (command.trim().equalsIgnoreCase("s")) {
            String key = strArray[1];
            skiplist.search(key);
        }
        else if (command.trim().equalsIgnoreCase("p")) {
            skiplist.printAll();
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
        boolean seeded = false;

        File file = new File(args[0]);
        String fileName = args[0];

        System.out.println("\nFor the input file named " + fileName);
        if(args.length == 2 && args[1].trim().equalsIgnoreCase("r")) {
            System.out.println("With the RNG seeded,");
            seeded = true;
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
                String[] strArray = line.split(" ");
                process.runCommands(strArray, skiplist, seeded);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        complexityIndicator();
    }

}