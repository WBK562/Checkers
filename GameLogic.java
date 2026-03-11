package Warcaby;

import javax.swing.*;
import java.awt.*;

public class GameLogic {
    private WarcabyBoard warcabyBoard;
    private final JButton[][] board;
    private final Player player1;
    private final Player player2;
    private final int size;
    private int[] selected = null;
    private JButton enemy = null;
    private JButton selectedButton = null;
    private JButton pawn = null;
    private boolean isEmpty,isDiagonal,isDiagonalEmpty,isForward,isEnemy;
    private final JLabel turnLabel;


    public GameLogic(JButton[][] board, int size, Player player1, Player player2, JLabel turnLabel, WarcabyBoard warcabyBoard) {
        this.warcabyBoard = warcabyBoard;
        this.board = board;
        this.size = size;
        this.player1 = player1;
        this.player2 = player2;
        this.turnLabel = turnLabel;
        player1.startTimer();
        updateTurn();
    }

    public void gameLog(int row, int col) {
        pawn = board[row][col];
        Player current = player1.getPlayerTurn() ? player1 : player2;
        Player opponent = player1.getPlayerTurn() ? player2 : player1;

        if (selected == null) {
            if (pawn.getIcon() == current.getPlayerIcon() || pawn.getIcon() == current.getQueenIcon()) {
                selected = new int[2];
                selected[0] = row;
                selected[1] = col;
                pawn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
            }
            return;
        }
        int sel_row = selected[0];
        int sel_col = selected[1];
        if (selected[0] == row && selected[1] == col) {
            board[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selected = null;
            return;
        }
        selectedButton = board[sel_row][sel_col];
        boolean isQueen = selectedButton.getIcon() == current.getQueenIcon();
        boolean moved;
        if(!isQueen){
            moved = pawnsMoves(sel_row,sel_col,row,col,current,opponent,selectedButton,pawn);
        }
        else{
            moved = queenMoves(sel_row,sel_col,row,col,current,opponent,selectedButton,pawn);
        }

        if(!moved){
            selectedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        selected = null;
        isQueenNow(row, col);
        endGame(current,opponent);
    }

    public boolean pawnsMoves(int sel_row, int sel_col, int row, int col, Player current, Player opponent, JButton selectedButton,JButton pawn){
        int midRow = (sel_row + row) / 2;
        int midCol = (sel_col + col) / 2;
        enemy = board[midRow][midCol];
        pawnMovesBoolean(sel_row,sel_col,row,col,current,opponent);
        if(isDiagonalEmpty && isEmpty && isEnemy) {
            switchPos(selectedButton,pawn);
            destroyEnemy(enemy);
            opponent.removePiece();
            if (!isAbleToHit(row, col,opponent,current)) {
                switchTurn();
            }
            return true;
        }
        else if(isDiagonal && isEmpty && isForward && !mustHit(current, opponent)){
            switchPos(selectedButton,pawn);
            switchTurn();
            return true;
        }
        return false;
    }

    public boolean queenMoves(int sel_row, int sel_col, int row, int col, Player current, Player opponent, JButton selectedButton, JButton pawn){
        int rowStep = Math.abs(sel_row - row);
        int colStep = Math.abs(sel_col - col);
        if(rowStep != colStep) return false;
        int rowAdd = (row - sel_row) > 0 ? 1 : -1;
        int colAdd = (col - sel_col) > 0 ? 1 : -1;
        int r = sel_row + rowAdd;
        int c = sel_col + colAdd;
        boolean isEmpty = pawn.getIcon() == null;
        boolean enemyFound = false;
        int[] enemyCoords = null;
        while(r != row && c != col){
            if(board[r][c].getIcon() == current.getPlayerIcon() || board[r][c].getIcon() == current.getQueenIcon()){
                return false;
            }
            else if(board[r][c].getIcon() == opponent.getPlayerIcon() || board[r][c].getIcon() == opponent.getQueenIcon()){
                if(enemyFound){
                    break;
                }
                enemyFound = true;
                enemyCoords = new int[]{r,c};
                enemy = board[r][c];
            }
            else{
                break;
            }
            c += colAdd;
            r += rowAdd;
        }
        if(enemyCoords != null && isEmpty) {
            switchPos(selectedButton,pawn);
            destroyEnemy(enemy);
            opponent.removePiece();
            if(!isAbleToHit(row,col,opponent,current)){
                switchTurn();
            }
            return true;
        }
        if(isEmpty && mustHit(current, opponent)) {
            switchPos(selectedButton,pawn);
            switchTurn();
            return true;
        }
        return false;
    }

    public boolean isAbleToHit(int row,int col,Player enemy,Player current){
        boolean isQueen = board[row][col].getIcon() == current.getQueenIcon();
        if (isQueen) {
            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] dir : directions) {
                int r = row + dir[0];
                int c = col + dir[1];
                boolean enemyFound = false;
                while (r >= 0 && c >= 0 && r < size && c < size) {
                    Icon figure = board[r][c].getIcon();
                    if (figure == null) {
                        if (enemyFound) {
                            return true;
                        }
                    } else if (figure == enemy.getPlayerIcon() || figure == enemy.getQueenIcon()) {
                        if (enemyFound) break;
                        enemyFound = true;
                    } else {
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        }
        else {
            for (int i = -2; i <= 2; i += 4) {
                for (int j = -2; j <= 2; j += 4) {
                    int midCol = col + j / 2;
                    int midRow = row + i / 2;
                    int blankCol = col + j;
                    int blankRow = row + i;
                    if (midCol >= 0 && midRow >= 0 && midCol < size && midRow < size && (board[midRow][midCol].getIcon() == enemy.getPlayerIcon() ||
                            board[midRow][midCol].getIcon() == enemy.getQueenIcon())) {
                        if (blankCol >= 0 && blankRow >= 0 && blankCol < size && blankRow < size && board[blankRow][blankCol].getIcon() == null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean mustHit(Player current,Player enemy){
        for(int i = 0;i < size;i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j].getIcon() == current.getPlayerIcon() || board[i][j].getIcon() == current.getQueenIcon()){
                    if(isAbleToHit(i,j,enemy,current)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void isQueenNow(int row, int col) {
        Icon text = board[row][col].getIcon();

        if (text == player2.getPlayerIcon() && row == 0) {
            board[row][col].setIcon(player2.getQueenIcon());
        } else if (text == player1.getPlayerIcon() && row == board.length - 1) {
            board[row][col].setIcon(player1.getQueenIcon());
        }
    }

    public void switchPos(JButton selectedButton,JButton pawn){
        pawn.setIcon(selectedButton.getIcon());
        pawn.setForeground(selectedButton.getForeground());
        selectedButton.setIcon(null);
        selectedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void destroyEnemy(JButton enemy){
        enemy.setIcon(null);
        enemy.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void pawnMovesBoolean(int sel_row,int sel_col, int row, int col,Player current,Player opponent){
        isDiagonal = Math.abs(sel_row - row) == 1 && Math.abs(sel_col - col) == 1;
        isDiagonalEmpty = Math.abs(sel_row - row) == 2 && Math.abs(sel_col - col) == 2;
        isEmpty = pawn.getIcon() == null;
        isForward = current == player1 ? (row > sel_row) : (row < sel_row);
        isEnemy = enemy.getIcon() == opponent.getPlayerIcon() || enemy.getIcon() == opponent.getQueenIcon();
    }

    public void updateTurn() {
        Player current = player1.getPlayerTurn() ? player1 : player2;
        turnLabel.setText("Tura gracza: " + current.getPlayerName());
    }

    public void switchTurn() {
        if (player1.getPlayerTurn()) {
            player1.stopTimer();
            player2.startTimer();
            player1.toggleTurn();
            player2.toggleTurn();
        } else {
            player2.stopTimer();
            player1.startTimer();
            player2.toggleTurn();
            player1.toggleTurn();
        }
        updateTurn();
    }

    public void endGame(Player current,Player opponent){
        if(current.getHowManyPiecies() == 0){
            new EndScreen(opponent.getPlayerName(),this.warcabyBoard).setVisible(true);
        }
        if(opponent.getHowManyPiecies() == 0){
            new EndScreen(current.getPlayerName(),this.warcabyBoard).setVisible(true);
        }
    }
}
