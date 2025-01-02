/*
 * This file implements the sample code for examples in Chapter 13 of the book
 * In order to execute it on the command line, invoke
 *      java Chapter13.java <example>
 * where <example> would be one of bubble, selection_recursive, selection, insertion, merge, quick or help
 *
 */

public class Chapter13 {
    public static void main(String[] args) {
        String command = "help";
        String main_text = "java Chapter13.java ";
        String[] ValidCommands = {"help",  "bubble", "selection_recursive", "selection", "insertion", "merge", "quick"};
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

            case "bubble":
                bubble_sort(make_items());
                break;
            case "selection_recursive":
                selection_sort_recursive(make_items(), 0);
                break;
            case "insertion":
                insertion_sort(make_items());
                break;
            case "selection":
                selection_sort(make_items());
                break;
            case "merge":
                merge_sort_demo();
                break;
            case "quick":
                quick_sort_demo();
                break;
            default:
                System.out.println(command + " is not a valid command: ");
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }

        }
    }

    static int[] make_items() {
        int[] items = {12, 19, 18, 16, 25, 31};
        return items;
    }

    static void print_array(String msg, int[] items) {
        System.out.print(msg + "\t");
        for (int i = 0; i < items.length; i++) {
            System.out.print(items[i]);
            if (i != items.length - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }
    }

    public static void bubble_sort(int[] items) {
        print_array("Bubble Sort Input:  ", items);
        int count = items.length;
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (items[i] > items[j]) {
                    print_array("\tBefore Swapping " + i + " with " + j + " :", items);
                    int temp = items[i];
                    items[i] = items[j];
                    items[j] = temp;
                    print_array("\tAfter Swapping  " + i + " with " + j + " :", items);
                }
            }
        }
        print_array("Bubble Sort Output: ", items);
    }

    public static void selection_sort_recursive(int[] items, int start) {
        int end = items.length;
        if (start >= end) {
            print_array("Recursion stopped", items);
            return;
        }
        int pos = start;
        for (int i = start; i < end; i++) {
            if (items[pos] > items[i]) {
                pos = i;
            }
        }
        print_array("Identified smallest at step " + start + " as " + pos + "\t", items);
        int temp = items[start];
        items[start] = items[pos];
        items[pos] = temp;
        print_array("After swapping smallest at step " + start + "\t", items);
        selection_sort_recursive(items, start + 1);

    }

    public static void selection_sort(int[] items) {
        print_array("Selection Sort Input: ", items);
        int count = items.length;
        for (int i = 0; i < count - 1; i++) {
            int pos = i;
            for (int j = pos + 1; j < count; j++) {
                if (items[j] < items[pos]) {
                    pos = j;
                }
            }
            print_array("Minimum position is " + pos, items);
            print_array("\tBefore Swapping " + i + " with " + pos + " :", items);
            int temp = items[i];
            items[i] = items[pos];
            items[pos] = temp;
            print_array("\tAfter Swapping  " + i + " with " + pos + " :", items);
        }
        print_array("Selection Sort Output: ", items);
    }

    public static void insertion_sort(int[] items) {
        print_array("Insertion Sort Input: ", items);
        for (int k = 1; k < items.length; k++) {
            int key = items[k];
            print_array("Step number " + k + " with key = " + key + " : ", items);
            int j = k - 1;
            while (j > 0 && items[j] > key) {
                items[j + 1] = items[j];
                print_array("\t Shifted  loc " + j + " by 1 : ", items);
                j--;
            }
            items[j + 1] = key;
        }
        print_array("Insertion Sort Output: ", items);
    }

    static void merge_sort_demo() {
        int[] items = make_items();
        print_array("Original Array: ", items);
        int[] sorted_items = merge_sort(0, items, 0, items.length);
        print_array("Sorted Array: ", sorted_items);
    }

    static void print_nested(int level, String msg) {
        for (int i=0;i<level;i++) { System.out.print('\t');}
        System.out.print(msg);
    }
    static void print_nested(int level, String msg, int[] items, int start, int end) {
       print_nested(level, msg);
        for (int i = start; i < end; i++) {
            System.out.print(items[i]);
            if (i != end - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }

    }
    static int[] merge_sort(int level, int[] items, int start, int end) {
        print_nested(level, "Sorting array ", items, start, end);
        if (start >= end) {
            print_nested(level, "Returning empty list\n");
            return (new int[0]);
        }
        if (start == end-1) {
            print_nested(level, "Returning singleton array with " + items[start]+"\n");
            int [] answer = new int[1];
            answer[0] = items[start];
            return answer;
        }
        int mid = (start + end) / 2;

        print_nested(level, "Midpoint is "+mid+'\n');
        int[] sort_1 = merge_sort(level+1, items, start, mid);
        int[] sort_2 = merge_sort(level+1, items, mid, end);
        print_nested(level, "Sorted array 1: ", sort_1, 0, sort_1.length);
        print_nested(level, "Sorted array 1: ", sort_2, 0, sort_2.length);
        int[] answer = merge_lists(sort_1, sort_2);
        print_nested(level, "Returning Sorted Array",answer, 0, answer.length);
        return answer;

    }

    static int[] merge_lists(int[] list_1, int[] list_2) {
        int[] answer = new int[list_1.length + list_2.length];
        int ptr_1 = 0;
        int ptr_2 = 0;
        int ptr = 0;
        while (ptr_1 < list_1.length && ptr_2 < list_2.length) {
            if (list_1[ptr_1] < list_2[ptr_2]) {
                answer[ptr++] = list_1[ptr_1++];
            } else {
                answer[ptr++] = list_2[ptr_2++];
            }
        }
        while (ptr_1 < list_1.length) {
            answer[ptr++] = list_1[ptr_1++];
        }
        while (ptr_2 < list_2.length) {
            answer[ptr++] = list_2[ptr_2++];
        }
        return answer;
    }

    static void quick_sort_demo() {
        int[] items = make_items();
        print_array("Original Array: ", items);
        quick_sort(0, items, 0, items.length-1);
        print_array("Sorted Array: ", items);

    }
    static void quick_sort(int level, int items[], int start, int end) {
        print_nested(level,"Quick Sort input: ", items, start, end+1);
        if (start < end) {
            int p = quick_sort_partition(level, items, start, end);
            print_nested(level, "Partitioned at location " + p+"\n");
            print_nested(level,"Quick Sort partitions: ", items, start, end+1);
            quick_sort(level + 1, items, start, p-1 );
            quick_sort(level + 1, items, p, end);
        }
        print_nested(level,"Quick Sort output:", items, start, end+1);
    }

    static int quick_sort_partition(int level, int items[], int start, int end) {
        int pivot = items[end];
        print_nested(level, "Partitioning with pivot at location " + end + " with pivot value "+ pivot +": ", items, start, end);
        int boundary  = start -1;
        for (int pos = start; pos<end; pos++) {
            if (items[pos] <= pivot) {
                print_nested(level, "Incrementing boundary currently at " + boundary + ", ", items, start, end);
                boundary++;
                int temp = items[boundary];
                items[boundary] = items[pos];
                items[pos] = temp;
                print_nested(level, "Post increment and swapping with position " + pos+ ", ", items, start, end);
            }
        }
        print_nested(level, "Swapping pivot and boundary currently boundary at " + boundary, items, start, end);
        boundary++;
        int temp = items[end];
        items[end]=items[boundary];
        items[boundary]=temp;
        return boundary;
    }

}
