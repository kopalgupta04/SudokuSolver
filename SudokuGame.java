import java.util.Random;
import java.util.Scanner;

public class SudokuGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Define three Sudoku problems
        char[][][] sudokuProblems = {
            {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
            },
            {
                {'.', '.', '.', '.', '8', '.', '.', '.', '7'},
                {'.', '9', '.', '.', '.', '.', '6', '1', '.'},
                {'.', '.', '7', '.', '9', '.', '5', '.', '.'},
                {'.', '.', '8', '6', '.', '4', '.', '.', '2'},
                {'4', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'9', '.', '.', '8', '.', '1', '.', '.', '.'},
                {'.', '.', '9', '.', '5', '.', '8', '.', '.'},
                {'.', '8', '6', '.', '.', '.', '.', '4', '.'},
                {'3', '.', '.', '.', '7', '.', '.', '.', '.'}
            },
            {
                {'.', '.', '5', '.', '.', '3', '.', '7', '.'},
                {'.', '.', '.', '.', '8', '.', '9', '.', '.'},
                {'.', '7', '.', '.', '.', '2', '.', '.', '8'},
                {'1', '.', '.', '.', '.', '.', '.', '9', '.'},
                {'.', '9', '.', '8', '.', '7', '.', '2', '.'},
                {'.', '4', '.', '.', '.', '.', '.', '.', '5'},
                {'7', '.', '.', '4', '.', '.', '.', '3', '.'},
                {'.', '.', '8', '.', '2', '.', '.', '.', '.'},
                {'.', '1', '.', '7', '.', '.', '8', '.', '.'}
            }
        };

        // Randomly select a Sudoku problem
        Random random = new Random();
        int selectedPuzzle = random.nextInt(sudokuProblems.length);
        char[][] board = sudokuProblems[selectedPuzzle];

        System.out.println("Sudoku Puzzle:");
        display(board);

        int attempts = 3;
        boolean isSolved = false;

        while (attempts > 0 && !isSolved) {
            System.out.println("\nEnter your solution for the empty cells (row column value):");

            // Copy the initial board to the user's board
            char[][] userBoard = new char[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    userBoard[i][j] = board[i][j];
                }
            }

            // Take user input for empty cells
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] == '.') {
                        System.out.print("Enter value for cell (" + (i + 1) + ", " + (j + 1) + "): ");
                        userBoard[i][j] = sc.next().charAt(0);
                    }
                }
            }

            // Check if the user's solution is correct
            if (isValidSolution(userBoard)) {
                System.out.println("Solution you entered: ");
                display(userBoard);
                System.out.println("Congratulations! You solved the Sudoku.");
                isSolved = true;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Incorrect solution. You have " + attempts + " attempts left.");
                } else {
                    System.out.println("You lost! No attempts left.");
                    System.out.println("Correct Solution:");
                    if (solve(board)){
                        display(board);
                    }else{
                        System.out.println("This sudoku cannot be solved...");
                    }
                }
            }
        }

        sc.close();
    }

    // to solve the board if user is unable to find the solution
    public static boolean solve(char[][] board) {
        int n = board.length;
        int row = -1;
        int col = -1;

        boolean emptyLeft = true;

        //this is how we are replacing r,c from arguments
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == '.') { //found an empty item
                    row = i;
                    col = j;
                    emptyLeft = false; //found an empty place to place digit, therefore change value of variable emptyLeft to false
                    break;
                }
            }
            //if you found an empty element in row, then break this for loop(and place a digit at this empty position)
            if (!emptyLeft) {
                break;
            }
        }

        if (emptyLeft) {  //means no empty item found because initial value of emptyLeft did not change
            return true;
            //means sudoku is solved
        }

        for (char number = '1'; number <= '9'; number++) {
            if (isSafe(board, row, col, number)) {
                board[row][col] = number;
                if (solve(board)) {  //if the function for next recursive call gives true
                    //found the ans for that particular place
                    return true;
                } else {
                    //backtrack
                    board[row][col] = '.';
                }
            }
        }

        return false;
    }

    private static boolean isValidSolution(char[][] board) {
        // Check rows, columns, and 3x3 sub-grids
        for (int i = 0; i < 9; i++) {
            if (!isValidRow(board, i) || !isValidColumn(board, i) || !isValidSubgrid(board, i)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidRow(char[][] board, int row) {
        boolean[] seen = new boolean[9]; // Array to track numbers that have been encountered
        
        for (int col = 0; col < 9; col++) {
            char num = board[row][col]; // Get the current number in the row
            
            if (num != '.') { // If the cell is not empty
                int index = num - '1'; // Convert the character to an index (0-8)
                
                if (seen[index]) { // If this number has already been seen in the row
                    return false; // The row is invalid, so return false
                }
                
                seen[index] = true; // Mark this number as seen
            }
        }
        
        return true; // If no duplicates were found, the row is valid
    }
    

    private static boolean isValidColumn(char[][] board, int col) {
        boolean[] seen = new boolean[9];
        for (int row = 0; row < 9; row++) {
            char num = board[row][col];
            if (num != '.') {
                int index = num - '1';
                if (seen[index]) return false;
                seen[index] = true;
            }
        }
        return true;
    }

    private static boolean isValidSubgrid(char[][] board, int subgrid) {
        boolean[] seen = new boolean[9];
        int rowStart = (subgrid / 3) * 3;
        int colStart = (subgrid % 3) * 3;
        for (int r = rowStart; r < rowStart + 3; r++) {
            for (int c = colStart; c < colStart + 3; c++) {
                char num = board[r][c];
                if (num != '.') {
                    int index = num - '1';
                    if (seen[index]) return false;
                    seen[index] = true;
                }
            }
        }
        return true;
    }

    private static boolean isSafe(char[][] board, int row, int col, char num) {
        // for checking that number doesn't repeat within a row
        for (int i = 0; i < board.length; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // for checking that number doesn't repeat within a col
        for (char[] nums : board) {
            if (nums[col] == num) {
                return false;
            }
        }

        // for checking that number doesn't repeat within a 3x3 box
        int rowStart = row - (row % 3);
        int colStart = col - (col % 3);
        for (int r = rowStart; r < rowStart + 3; r++) {
            for (int c = colStart; c < colStart + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void display(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("+++++++++++++++++++"); // Horizontal divider after every 3 rows
            }

            for (int j = 0; j < board[i].length; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("|"); // Vertical divider after every 3 columns
                }
                System.out.print(board[i][j]);

                if (j < board[i].length - 1) {
                     System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
