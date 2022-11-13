import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GamePanel implements ActionListener {
    private final int dimension = 4;
    private final int boardSize = dimension * dimension;
    private final JButton[][] board = new JButton[dimension][dimension];
    private int emptySquare = dimension * dimension;
    private JFrame frame = new JFrame("15-spel");
    private JPanel panel = new JPanel();
    private final int width = 400; //board width
    private final int height = 400; //board height
    private final JButton newGame = new JButton("Nytt Spel");
    private final JButton showGameFinished = new JButton("Visa Vunnet Spel");
    private final JPanel newGamePanel = new JPanel();
    private String[] win = new String[boardSize - 1];
    ArrayList<Integer> initialList = new ArrayList<>(boardSize);
    private int nbTiles = dimension * dimension - 1;

    public GamePanel() {
        panel.setLayout(new GridLayout(dimension, dimension));
        frame.add(newGamePanel);
        frame.setLayout(new GridLayout());
        newGamePanel.setLayout(new GridLayout(1, 1));
        newGamePanel.add(newGame);
        newGamePanel.add(showGameFinished);
        newGame.addActionListener(this);
        showGameFinished.addActionListener(this);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeBoard();
        panel.setPreferredSize(new Dimension(width, height));
        frame.setSize(500, 500);
        frame.pack();

        for (int i = 1; i < boardSize; i++) {
            win[i - 1] = Integer.toString(i);

        }

        System.out.println("Såhär vinner du:" + (Arrays.toString(win)));


    }


    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            initialList.add(i, i);

        }
        Collections.shuffle(initialList);
        for (int i = 0; i < boardSize; i++) {
            int rows = i / dimension;
            int columns = i % dimension;
            int maxBlankSquares = 0;
            board[rows][columns] = new JButton(String.valueOf(initialList.get(i)));
            if (initialList.get(i) == 0) {
                emptySquare = i;
                if(maxBlankSquares == 0) {
                    maxBlankSquares++;
                    board[rows][columns].setVisible(false); //för att visa blanka rutan
                }
            }
            board[rows][columns].addActionListener(this);
            panel.add(board[rows][columns]);

        }
    }

    public void actionPerformed(ActionEvent event) throws IllegalArgumentException {
        JButton buttonPressed = (JButton) event.getSource();
        int index = indexOf(buttonPressed.getText());
        if (event.getSource() == newGame) {
            initialList.clear();
            panel.removeAll();
            initializeBoard();
            newGame();
            panel.revalidate();
            panel.repaint();


        }

        if (event.getSource() == showGameFinished) {  //fixa
            panel.removeAll();
            initializeBoard();
            Collections.sort(initialList);
            panel.repaint();
        }

        int row = index / dimension;
        int column = index % dimension;

        makeMove(row, column);

        // om spelet är vunnet, visa texten
        if (isGameFinished()) {
            JOptionPane.showMessageDialog(null, "Grattis! Du vann!");
        }
    }

    private boolean makeMove(int row, int column) {
        int emptyRow = emptySquare / dimension;  // blank ruta på row
        int emptyCol = emptySquare % dimension;   // blank ruta  på column
        int rowDiff = emptyRow - row;
        int colDiff = emptyCol - column;
        boolean isInRow = (row == emptyRow);
        boolean isInCol = (column == emptyCol);
        boolean isNotDiagonal = (isInRow || isInCol);

        if (isNotDiagonal) {
            int diff = Math.abs(colDiff);

            // move row left
            if (colDiff < 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol + i].setText(
                            board[emptyRow][emptyCol + (i + 1)].getText());
                }

            } //move row right
            else if (colDiff > 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol - i].setText(
                            board[emptyRow][emptyCol - (i + 1)].getText());
                }
            }

            diff = Math.abs(rowDiff);

            //  move column up
            if (rowDiff < 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow + i][emptyCol].setText(
                            board[emptyRow + (i + 1)][emptyCol].getText());
                }

            } //  move column down
            else if (rowDiff > 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow - i][emptyCol].setText(
                            board[emptyRow - (i + 1)][emptyCol].getText());
                }
            }

            // Swap the empty square with the given square
            board[emptyRow][emptyCol].setVisible(true);
            board[row][column].setText(Integer.toString(0));
            board[row][column].setVisible(false);
            emptySquare = getIndex(row, column);
        }

        return true;
    }


    private int getIndex(int i, int j) {
        return ((i * dimension) + j);  // i * 4 + j

    }

    private int indexOf(String squareNumber) {
        for (int rows = 0; rows < board.length; rows++) {
            for (int columns = 0; columns < board[rows].length; columns++) {
                if (board[rows][columns].getText().equals(squareNumber)) {
                    return (getIndex(rows, columns));
                }
            }
        }
        return -1;

    }

    private boolean isGameFinished() {
        //kollar ifall siffrorna är på rätt plats
        for (int i = win.length - 1; i >= 0; i--) {
            String number = board[i / dimension][i % dimension].getText();
            if (!number.equals(win[i])) {
                return false;       // returnerar false ifall inte matchningen stämmer! annars true

            }
        }
        return true;
    }

    private void shuffle() {
        int n = nbTiles;

        while (n > 1) {
            Random random = new Random();
            int r = random.nextInt(n--);
            int tmp = initialList.get(r);
            initialList.set(r, initialList.get(n));
            initialList.set(n, tmp);
        }
    }

    private void newGame() {
            reset(); // reset
            shuffle(); // shuffle
    }

        private void reset() {
            for (int i = 0; i < initialList.size(); i++) {
                initialList.set(i, (i + 1) % board.length);
            }

            // set blank at last
            emptySquare = board.length - 1;
        }

    }

