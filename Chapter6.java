import java.util.Random;

/*
 * This file implements the sample code for examples in Chapter 6 of the book
 * In order to execute it on the command line, invoke
 *      java Chapter6.java <example>
 * where <example> would be one of obfuscator, encryptor, cache or help
 *
 */
public class Chapter6 {
    public static void main(String[] args) {
        String command = "help";
        String main_text = "java Chapter6.java ";
        String[] ValidCommands = {"help", "obfuscator", "encryptor", "cache"};
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

            case "obfuscator":
                drive_obfuscator();
                break;
            case "encryptor":
                drive_encryptor();
                break;
            case "cache":
                drive_cache();
                break;
            default:
                System.out.println(command + " is not a valid command: ");
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }
        }
    }

    static void drive_obfuscator() {
        Obfuscator obfs = new Obfuscator(6);
        System.out.println("**** Obfuscator Driver ****");

        String test_string = "THIS IS CRAZY!!";
        System.out.println("Original:" + test_string);
        String hidden_text = obfs.scramble(test_string);
        System.out.println("Scrambled:" + hidden_text);
        String inverted = obfs.unscramble(hidden_text);
        System.out.println("Unscrambled:" + inverted);


    }

    static void drive_encryptor() {
        Encryptor enc = new Encryptor();
        System.out.println("**** Encryptor Driver ****");

        String test_string = "THIS IS CRAZY!!";
        System.out.println("Original:" + test_string);
        String cipher_text = enc.encrypt(test_string);
        System.out.println("Encrypted:" + cipher_text);
        String inverted = enc.decrypt(cipher_text);
        System.out.println("Decrypted:" + inverted);

    }

    static void drive_cache() {
        Cache cache = new Cache(4);
        for (int item = 0; item < 10; item++) {
            cache.add_item(item);
            System.out.println("On adding Item " + item + " Cache is: " + cache);
            for (int check = 0; check <= item; check++) {
                System.out.println("\t Item: " + check + " presence: " + cache.search_item(check));
            }
        }
    }

}

class Obfuscator {
    char[] secret_key;
    char[] invert_key;
    int rotation_distance;

    Obfuscator(int rotation_distance) {
        this.rotation_distance = rotation_distance;
        this.secret_key = new char[26];
        this.invert_key = new char[26];
        for (int i = 0; i < 26; i++) {
            int shifted = (i + rotation_distance) % 26;
            char rotated = (char) ('A' + shifted);
            char original = (char) ('A' + i);
            this.secret_key[i] = rotated;
            this.invert_key[shifted] = original;
        }
    }

    @Override
    public String toString() {
        String answer = "MyKeys: " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + '\n';
        answer = answer + "Secret: " + String.valueOf(this.secret_key) + '\n';
        answer = answer + "Invert: " + String.valueOf(this.invert_key) + '\n';
        return answer;
    }


    String scramble(String plain_text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plain_text.length(); i++) {
            char c = plain_text.charAt(i);
            if ((c >= 'A') && (c <= 'Z')) {
                char c1 = this.secret_key[c - 'A'];
                sb.append(c1);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    String unscramble(String encrypted_text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encrypted_text.length(); i++) {
            char c = encrypted_text.charAt(i);
            if ((c >= 'A') && (c <= 'Z')) {
                char c1 = this.invert_key[c - 'A'];
                sb.append(c1);
            } else sb.append(c);
        }
        return sb.toString();
    }


}

class Encryptor {
    char[] secret_key;
    char[] invert_key;

    Encryptor(char[] secret_key) {
        this.secret_key = new char[26];
        this.invert_key = new char[26];
        System.out.println("Secret Key:" + String.valueOf(secret_key));
        for (int i = 0; i < 26; i++) {
            int index = secret_key[i] - 'A';
            char original = (char) ('A' + i);
            this.secret_key[i] = secret_key[i];
            this.invert_key[index] = original;
        }
    }

    Encryptor() {
        this(Encryptor.gen_random());
    }

    static char[] gen_random() {
        Random random = new Random();
        char[] buffer = new char[26];
        for (int i = 0; i < 26; i++) {
            buffer[i] = (char) (i + 'A');
        }
        for (int i = 26; i > 0; i--) {
            int j = random.nextInt(i);
            char temp = buffer[i - 1];
            buffer[i - 1] = buffer[j];
            buffer[j] = temp;
        }
        return buffer;
    }

    @Override
    public String toString() {
        String answer = "MyKeys: " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + '\n';
        answer = answer + "Secret: " + String.valueOf(this.secret_key) + '\n';
        answer = answer + "Invert: " + String.valueOf(this.invert_key) + '\n';
        return answer;
    }


    String encrypt(String plain_text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plain_text.length(); i++) {
            char c = plain_text.charAt(i);
            if ((c >= 'A') && (c <= 'Z')) {
                char c1 = this.secret_key[c - 'A'];
                sb.append(c1);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    String decrypt(String encrypted_text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encrypted_text.length(); i++) {
            char c = encrypted_text.charAt(i);
            if ((c >= 'A') && (c <= 'Z')) {
                char c1 = this.invert_key[c - 'A'];
                sb.append(c1);
            } else sb.append(c);
        }
        return sb.toString();
    }


}

class Cache {
    int[] cache_items;
    int items;
    int start;
    int end;

    Cache(int capacity) {
        this.cache_items = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            this.cache_items[i] = -1;
        }
        this.items = 0;
        this.start = -1;
        this.end = 0;
    }

    boolean is_full() {
        return this.start == this.end;
    }

    boolean is_empty() {
        return -1 == this.start;
    }

    int next_value(int value) {
        return (value + 1) % this.cache_items.length;
    }

    void add_item(int item) {
        if (this.is_full()) {
            this.cache_items[this.start] = item;
            this.start = this.next_value(this.start);
            this.end = this.next_value(this.end);
        } else {
            if (this.is_empty()) {
                this.start = 0;
            }
            this.cache_items[this.end] = item;
            this.end = this.next_value(this.end);
        }
    }

    boolean search_item(int item) {
        for (int i = 0; i < this.cache_items.length; i++) {
            int index = (this.start + i) % this.cache_items.length;
            if (item == this.cache_items[index]) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String answer = "Start: " + this.start + "End: " + this.end + "items: [";
        for (int i = 0; i < this.cache_items.length; i++) {
            answer = answer + cache_items[i] + ", ";
        }
        answer = answer + "]";
        return answer;
    }


}

