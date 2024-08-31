import java.util.Scanner;

public class SudokuSolver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] board = new char[9][9];

        System.out.println("Enter the Sudoku board (9x9 grid). Use '.' for empty cells:");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = scanner.next().charAt(0);
            }
        }

        System.out.println("Board entered: ");
        display(board);

        if (solve(board)) {
            System.out.println("Solved board: ");
            display(board);
        } else {
            System.out.println("Cannot solve the Sudoku...");
        }

        scanner.close();
    }

    public static boolean solve(char[][] board) {
        int n = board.length;
        int row = -1;
        int col = -1;

        boolean emptyLeft = true;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == '.') {
                    row = i;
                    col = j;
                    emptyLeft = false;
                    break;
                }
            }
            if (!emptyLeft) {
                break;
            }
        }

        if (emptyLeft) {
            return true;
        }

        for (char number = '1'; number <= '9'; number++) {
            if (isSafe(board, row, col, number)) {
                board[row][col] = number;
                if (solve(board)) {
                    return true;
                } else {
                    board[row][col] = '.';
                }
            }
        }

        return false;
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

    private static boolean isSafe(char[][] board, int row, int col, char num) {
        for (int i = 0; i < board.length; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        for (char[] nums : board) {
            if (nums[col] == num) {
                return false;
            }
        }

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
}

