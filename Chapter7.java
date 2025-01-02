import java.util.*;

/*
 * This file implements the sample code for examples in Chapter 7 of the book
 * In order to execute it on the command line, invoke
 *      java Chapter7.java <example>
 * where <example> would be one of hangman, simulator or help
 *
 */

public class Chapter7 {
    public static void main(String[] args) {
        String command = "help";
        String main_text = "java Chapter7.java ";
        String[] ValidCommands = {"help", "hangman", "simulator"};
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

            case "hangman":
                drive_hangman();
                break;
            case "simulator":
                drive_simulator();
                break;

            default:
                System.out.println(command + " is not a valid command: ");
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }

        }
    }

    static void drive_hangman() {
        HangMan game = new HangMan();
        game.play();
    }

    static void drive_simulator() {
        EventGenerator servers = new EventGenerator(100,300);
        EventGenerator peak_customers = new EventGenerator(0,120);
        EventGenerator offtime_customers = new EventGenerator(0,600);

        CheckoutPlanner planner = new CheckoutPlanner(peak_customers,servers);
        int recommended = planner.calc_counters(120);
        System.out.println("Recommended for peak hours: " + recommended + " counters");

        planner = new CheckoutPlanner(offtime_customers,servers);
        recommended = planner.calc_counters(120);
        System.out.println("Recommended for off hours: " + recommended + " counters");

    }

}

class HangMan {
    String[] words = {"carnation", "daisy", "shoeflower", "rose", "tulip", "sunflower", "poppy", "azalea", "hydrangea", "dahlia", "orchid", "marigold", "begonia"};

    List<Character> guessed;
    List<Character> mistakes;
    int attempts;
    boolean solved;
    String answer;

    HangMan() {
        this.reset();
    }

    void reset() {
        this.guessed = new ArrayList<>();
        this.mistakes = new ArrayList<>();
        int index = (new Random()).nextInt(this.words.length);
        this.answer = this.words[index];
        this.attempts = 6;
        this.solved = false;
    }

    void show_player(int attempts) {
        switch (attempts) {
            case 0:
                System.out.println(' ');
                break;
            case 1:
                System.out.println('O');
                break;
            case 2:
                System.out.println(" 0");
                System.out.println('/');
                break;
            case 3:
                System.out.println(" 0");
                System.out.println("/ \\");
                break;
            case 4:
                System.out.println(" 0");
                System.out.println("/|\\");
                break;
            case 5:
                System.out.println(" 0");
                System.out.println("/|\\");
                System.out.println("/");
                break;
            case 6:
                System.out.println(" 0");
                System.out.println("/|\\");
                System.out.println("/ \\");
                break;
        }
    }

    boolean contains_char(List<Character> this_list, char this_char) {
        Character c = this_char;
        return this_list.contains(c);
    }

    String get_display_word() {
        char[] show = new char[this.answer.length()];
        for (int i = 0; i < this.answer.length(); i++) {
            if (this.contains_char(this.guessed, this.answer.charAt(i))) {
                show[i] = this.answer.charAt(i);
            } else {
                show[i] = '_';
            }
        }

        String show_string = new String(show);
        String display_word = show_string + "\t\t" + " Guessed: " + this.guessed;
        display_word = display_word + "\t Mistakes:" + this.mistakes;
        return display_word;

    }

    void print_first_line() {
        for (int i = 0; i < 15; i++) {
            System.out.print('-');
        }
        System.out.print("Guess this word with " + this.answer.length() + " letters");

        for (int i = 0; i < 25; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    void display_state() {
        this.print_first_line();
        this.show_player(6 - this.attempts);
        System.out.println(this.get_display_word());

    }

    void check_solved() {
        for (int i = 0; i < this.answer.length(); i++) {
            char letter = this.answer.charAt(i);
            if (!this.guessed.contains(letter)) {
                this.solved = false;
                return;
            }
        }
        this.solved = true;
    }


    void play_once(Scanner scanner) {
        while (!this.solved && this.attempts > 0) {
            this.display_state();
            System.out.print("Attempts left: " + this.attempts + " What is your Guess?");
            char letter = scanner.nextLine().charAt(0);
            if ((letter >= 'A') && (letter <= 'Z')) {
                letter = Character.toLowerCase(letter);
            }
            if ((letter >= 'a') && (letter <= 'z')) {
                if (this.contains_char(this.guessed, letter) || this.contains_char(this.mistakes, letter)) {
                    System.out.println("You have already guessed " + letter + " Try Again:");
                } else if (this.answer.indexOf(letter) < 0) {
                    this.mistakes.add(letter);
                    this.attempts--;
                } else {
                    this.guessed.add(letter);
                    this.check_solved();
                }


            } else {
                System.out.println("This is not a valid letter.");
                System.out.println(" Please enter a lower case letter between a and z");
            }

        }
        if (this.solved) {
            System.out.println("Congratulations -- You guessed it!! " + this.answer);
        } else {
            this.show_player(6);
            System.out.println("Sorry -- You lost!! Answer was -- " + this.answer);
        }

    }

    void play() {
        Scanner scanner = new Scanner(System.in);
        this.reset();
        play_once(scanner);
        boolean next_iter = true;
        while (next_iter) {
            System.out.print("Play another round? (Y/N)");
            char cont_game = scanner.nextLine().charAt(0);
            if (cont_game == 'Y' || cont_game == 'y') {
                this.reset();
                play_once(scanner);
            } else {
                next_iter = false;
            }
        }

    }

}

class CheckoutPlanner {
    int MAX_COUNTERS = 10;
    int MAX_SIMULATIONS = 1000;
    EventGenerator arrivals;
    EventGenerator services;

    CheckoutPlanner(EventGenerator arrivals, EventGenerator services) {
        this.arrivals = arrivals;
        this.services = services;
    }
    int calc_counters(double max_wait_time) {
        // max_wait_time is an upper bound on how many minutes a customer should wait -- the objective
        for (int option = 1; option <=MAX_COUNTERS; option++) {
            Averager agg = new Averager();
            for (int i=0; i< this.MAX_SIMULATIONS; i++) {
                Simulation sim = new Simulation(this.arrivals,this.services, option);
                double result = sim.estimate();
                agg.report(result);
            }
            double objective = agg.average();
            System.out.println("With " + option + " servers, wait time is: " + objective);
            if (objective < max_wait_time) {
                return option;
            }
        }
        return -1;
    }
}

class Averager {
    int reported_values;
    double sum_values;
    Averager() {
        this.reported_values = 0;
        this.sum_values = 0.0;

    }

    void report(double value) {
        this.sum_values = this.sum_values + value;
        this.reported_values++;
    }

    double average() {
        if (this.reported_values > 0) {
            return (this.sum_values/this.reported_values);
        }
        System.out.println("Called Average without any reports -- returning -1");
        return -1;
    }

}


class EventGenerator {
    int min_time;
    int max_time;

    Random random = new Random();

    EventGenerator(int min_time, int max_time) {
        this.min_time = min_time;
        this.max_time = max_time;
    }

    int instance() {
        return this.min_time + this.random.nextInt(max_time-min_time);
    }
}

class Clerk {
    boolean free;
    int next_free_time;
    EventGenerator gen;

    Clerk(EventGenerator gen) {
        this.gen = gen;
        this.free = true;
        this.next_free_time = 0;
    }

    void process_customer(int cur_time) {
        this.free = false;
        this.next_free_time = cur_time + gen.instance();
    }

    void update_status(int cur_time) {
        if ((!this.free) && (cur_time >= this.next_free_time)) {
            this.free = true;
        }
    }
}

class Customer {
    int arrival_time;

    Customer(int arrival_time) {
        this.arrival_time = arrival_time;
    }

    int wait_time(int service_time) {
        return (service_time - arrival_time);
    }
}

class Simulation {
    EventGenerator arrivals;
    EventGenerator services;
    int counters;

    int MAX_TIME = 4*60*60; // simulation run in seconds for 4 hours

    Simulation(EventGenerator arrivals, EventGenerator services, int counters) {
        this.arrivals = arrivals;
        this.services = services;
        this.counters = counters;
    }

    Clerk find_free_clerk(Clerk[] clerks) {
        for (Clerk c: clerks) {
            if (c.free) {
                return c;
            }
        }
        return null;
    }

    void print_status(int current_time, Queue<Customer> queue, int next_customer_arrival, Clerk[] clerks) {
        String msg = "Time: " + current_time;
        msg = msg + " Queue Size: " + queue.size();
        msg = msg + " next_arrival: " + next_customer_arrival;
        for (int i=0; i<clerks.length; i++) {
            msg = msg + " Clerk " + i;
            if (clerks[i].free) {
                msg = msg + " is free";
            } else {
                msg = msg + " busy - free at " + clerks[i].next_free_time;
            }
        }
        System.out.println(msg);
    }

    double estimate() {
        Queue<Customer> queue = new LinkedList<>();

        int next_customer_arrival = this.arrivals.instance();
        Clerk[] clerks = new Clerk[this.counters];
        for (int i=0; i < this.counters;i++) {
            clerks[i] = new Clerk(this.services);
        }

        Averager wait_average = new Averager();

        for (int current_time = 0; current_time < this.MAX_TIME; current_time++) {
            // this.print_status(current_time, queue, next_customer_arrival, clerks);
            if (current_time == next_customer_arrival) {
                // System.out.println("\tCustomer arrived at time " + current_time);
                queue.add(new Customer(next_customer_arrival));
                next_customer_arrival = current_time + this.arrivals.instance();
            }
            if (!queue.isEmpty()) {
                Clerk free_clerk = this.find_free_clerk(clerks);
                if (null != free_clerk) {
                    Customer customer = queue.poll();
                    wait_average.report(customer.wait_time(current_time));
                    // System.out.println("\tCustomer that arrived at time " + customer_arrival_time + " processed at " + current_time);
                    free_clerk.process_customer(current_time);
                }
            }
            for (Clerk c:clerks) {
                c.update_status(current_time);
            }
        }
        return wait_average.average();

    }
}

