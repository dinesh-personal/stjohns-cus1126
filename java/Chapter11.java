import java.lang.Math;

/*
 * This file implements the sample code for examples in Chapter 11 of the book
 * In order to execute it on the command line, invoke
 *      java Chapter11.java <example>
 * where <example> would be one of recursion, bisection, queens or help
 *
 */

public class Chapter11 {
    public static void main(String[] args) {
        String command = "help";
        String main_text = "java Chapter11.java ";
        String[] ValidCommands = {"help", "recursion", "bisection", "queens"};
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

            case "recursion":
                demo_recursion();
                break;
            case "bisection":
                demo_solver();
                break;
            case "queens":
                demo_queens();
                break;
            default:
                System.out.println(command + " is not a valid command: ");
                System.out.println("Use the system with one of the following commands");
                for (String text : ValidCommands) {
                    System.out.println(main_text + text);
                }

        }
    }

    static void demo_recursion() {
        int limit = 10;
        System.out.println("Iteration Sum " + new SumToN().compute(limit));
        System.out.println("Recursion Sum " + new SumToNRecursive().compute(limit));
        System.out.println("Iteration Prod " + new ProdToN().compute(limit));
        System.out.println("Recursion Sum " + new ProdToNRecursive().compute(limit));

    }

    static void demo_solver() {
        // Solve x^3 - 4x^2 + x + 1 = 0  (at x = 1 function is -1, at x = 4 is 3

        BisectionSolver solver = new BisectionSolver();
        double answer = solver.solve((x) -> x*x*x - 4*x*x + x + 1, 1, 4);
        System.out.println("Answer = " + answer);

        MyFunction fn = new MySpecificFunction();
        System.out.println("With Class Implementation Answer = " + solver.solve(fn, 1, 4));

    }

    static void demo_queens() {
        QueensSolver q = new QueensSolver();
        q.solve();

    }
}


abstract class SumIterationExample {
    int compute(int limit) {
        int answer = 0;
        for (int i = 0; i < limit; i++) {
            answer = answer + this.function(i);
        }
        return answer;
    }

    abstract int function(int value);

}

abstract class ProdIterationExample {
    int compute(int limit) {
        int answer = 1;
        for (int i = 1; i < limit; i++) {
            answer = answer * this.function(i);
        }
        return answer;
    }

    abstract int function(int value);

}

class SumToN extends SumIterationExample {

    int function(int x) {
        return x;
    }

}

class ProdToN extends ProdIterationExample {

    int function(int x) {
        return x;
    }

}

abstract class SumRecursionExample {
    int compute(int limit) {
        if (limit <= 0) {
            return this.function(0);
        } else {
            return this.function(limit) + this.compute(limit-1);
        }
    }

    abstract int function(int value);

}

abstract class ProdRecursionExample {
    int compute(int limit) {
        if (limit <= 1) {
            return this.function(1);
        } else {
            return this.function(limit) * this.compute(limit-1);
        }
    }

    abstract int function(int value);

}

class SumToNRecursive extends SumRecursionExample {

    int function(int x) {
        return x;
    }

}

class ProdToNRecursive extends ProdRecursionExample {
    int function(int x) {
        return x;
    }
}

interface MyFunction{
    double function(double value);
}

class BisectionSolver {
    double solve(MyFunction this_fn, double lower_bound, double upper_bound) {
        if (Math.abs(lower_bound-upper_bound) < 0.0001) {
            return lower_bound;
        }

        double lower = this_fn.function(lower_bound);
        double upper = this_fn.function(upper_bound);

        if (lower * upper > 0) {
            System.out.println("lower " + lower + " upper "+upper);
            throw new IllegalArgumentException("function did not have opposite values on boundaries " + lower_bound + " - " + upper_bound);
        }
        double middle_bound = (lower_bound+upper_bound)/2;
        double middle = this_fn.function(middle_bound);
        if (middle * lower < 0) {
            return solve(this_fn, lower_bound, middle_bound);
        } else {
            return solve(this_fn, middle_bound, upper_bound);
        }

    }


}

class MySpecificFunction implements MyFunction {
    public double function(double value) {
        return value*value*value - 4 * value*value + value + 1;
    }
}

class Board {
    boolean[][] pieces;

    Board() {
        this.pieces = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.pieces[i][j] = false;
            }
        }
    }

    void print() {
        this.print(true);
    }
    void print(boolean one_line) {
        if (one_line) {
            for (int i=0; i<8;i++) {
                for (int j = 0; j < 8; j++) {
                    if (this.pieces[i][j]) {
                        System.out.print( "(" + i + ","+ j+")");
                    }
                }
            }
            System.out.println();
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (this.pieces[i][j]) {
                        System.out.print("Q");
                    } else {
                        System.out.print("_");
                    }
                }
                System.out.println();
            }
        }


    }

    boolean viable(int row, int col) {
        // Assume that queens are placed in rows 0 .. row-1

        // check that there is no queen in same column
        for (int r = 0; r < row; r++) {
            if (this.pieces[r][col]) {
                return false;
            }
        }
        // check that there is no queen in diagonal to left
        for (int r =row-1, c=col-1; r>=0 && c>=0; r--,c--) {
                if (this.pieces[r][c]) {
                    return false;
                }
        }
        // check that there is no queen in diagonal to right
        for (int r =row-1, c=col+1; r>=0 && c<8; r--,c++) {
            if (this.pieces[r][c]) {
                return false;}
        }

        // All checks have passed - return true
        return true;
    }

    void place_queen(int row, int col) {
        this.pieces[row][col] = true;
    }

    void remove_queen(int row, int col) {
        this.pieces[row][col] = false;
    }
}
class QueensSolver {

    void solve() {
        Board b = new Board();
        Board solved = this.recursive_solve(b,0);
        if (solved != null) {
            solved.print(false);
        } else {
            System.out.println("Unable to find a solution");
        }
    }


    Board recursive_solve(Board b, int num_queens) {
        if (num_queens >=8) {
            return b;
        }

        for (int col=0; col < 8; col++) {
            if (b.viable(num_queens,col)) {
                b.place_queen(num_queens, col);
                Board new_b = recursive_solve(b, num_queens+1);
                if (new_b != null) {
                    return b;
                }
                b.remove_queen(num_queens, col); // remove the queen for next place attempt
            }

        }
        return null;
    }
}
