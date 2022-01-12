import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final TrieSET dictionary = new TrieSET();
    private final int[] SCORES = {0, 0, 0, 1, 1, 2, 3, 5, 11};

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException();
        }
        for (String word : dictionary) {
            this.dictionary.add(word);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rowCount = board.rows();
        int colCount = board.cols();
        Set<String> result = new HashSet<>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                boolean[][] marked = new boolean[rowCount][colCount];
                visit(board, i, j, marked, "", result);
            }
        }
        return result;
    }

    private void visit(BoggleBoard board, int row, int col, boolean[][] marked, String prefix, Set<String> words) {
        if (row < 0 || col < 0 || row >= board.rows() || col >= board.cols()) {
            return;
        }
        if (marked[row][col]) {
            return;
        }
        char letter = board.getLetter(row, col);
        String word = prefix + letter;
        if (letter == 'Q') {
            word += 'U';
        }
        if (!dictionary.hasPrefix(word)) {
            return;
        }
        if (word.length() >= 3 && dictionary.contains(word)) {
            words.add(word);
        }
        marked[row][col] = true;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i == row && j == col) {
                    continue;
                }
                visit(board, i, j, marked, word, words);
            }
        }
        marked[row][col] = false;
    }

    public int scoreOf(String word) {
        if (dictionary.contains(word)) {
            return SCORES[Math.min(word.length(), 8)];
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
