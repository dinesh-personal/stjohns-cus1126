import java.util.HashMap;

/*
 * This file implements the sample code for examples in Chapter 9 of the book
 * In order to execute it on the command line, invoke
 *      java Chapter9.java <example>
 * where <example> would be one of frequency, frequency2 or help
 *
 */
public class Chapter9 {
    public static void main(String[] args) {
        String[] words = {"carnation", "daisy", "shoeflower", "rose", "tulip", "sunflower", "poppy", "azalea", "hydrangea", "dahlia", "orchid", "marigold", "begonia"};
        String command = "help";
        String main_text = "java Chapter9.java ";
        String[] ValidCommands = {"help", "frequency", "frequency2"};
        if (args.length > 0) {
            command = args[0];
        }
        command = command.toLowerCase().trim();

        switch (command) {
            case "help":
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }
                System.out.println("The help command prints out this message");
                System.out.println("The other commands demonstrate corresponding examples from the chapter");
                break;

            case "frequency":
                drive_frequency_counts(words);
                break;
            case "frequency2":
                drive_frequency_counts_2(words);
                break;

            default:
                System.out.println(command + " is not a valid command: ");
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }

        }

    }

    static void drive_frequency_counts(String [] words) {

        FrequencyCounter counter = new FrequencyCounter();
        counter.populate(words);
        counter.print();

    }
    static void drive_frequency_counts_2(String [] words) {
        FrequencyCounter2 counter = new FrequencyCounter2();
        counter.populate(words);
        counter.print();

    }

}

class FrequencyCounter {

    HashMap<Character,Integer> counts;

    FrequencyCounter() {
        this.counts = new HashMap<>();
    }

    void count(String word) {
        Character key = word.charAt(0);
        if (this.counts.containsKey(key)) {
            this.counts.put(key,1+this.counts.get(key));
        } else {
            this.counts.put(key, 1);
        }
    }
    void populate(String[] words) {
        for (String word:words) {
            this.count(word);
        }
    }

    void print() {
        for (Character name: this.counts.keySet()) {
            String key = name.toString();
            String value = this.counts.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

}

class FrequencyCounter2 {

    HashMap<String,Integer> counts;

    FrequencyCounter2() {
        this.counts = new HashMap<>();
    }

    void count(String word) {
        String key;
        if (word.length() >=2) {
            key=word.substring(0,2);
        } else {
            key = word.substring(0,1);
        }
        if (this.counts.containsKey(key)) {
            this.counts.put(key,1+this.counts.get(key));
        } else {
            this.counts.put(key, 1);
        }
    }
    void populate(String[] words) {
        for (String word:words) {
            this.count(word);
        }
    }

    void print() {
        for (String name: this.counts.keySet()) {
            String key = name.toString();
            String value = this.counts.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

}
