package Warcaby;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Player {
    private final String PlayerName;
    private boolean PlayerMove;
    private int pieces;
    private long totalTime = 0;
    private Timer timer;
    private TimerTask timerTask;
    private final ImageIcon playerIcon;
    private final ImageIcon queenIcon;

    public Player(ImageIcon playerIcon, ImageIcon queenIcon,int pawns, boolean PlayerMove, String PlayerName){
        this.pieces = pawns;
        this.PlayerMove = PlayerMove;
        this.PlayerName = PlayerName;
        this.playerIcon = playerIcon;
        this.queenIcon = queenIcon;

    }

    public ImageIcon getPlayerIcon(){ return this.playerIcon; }
    public ImageIcon getQueenIcon() { return this.queenIcon; }
    public boolean getPlayerTurn(){ return this.PlayerMove; }
    public void removePiece() { if (pieces > 0) pieces--; }
    public void toggleTurn() { PlayerMove = !PlayerMove; }
    public int getHowManyPiecies() { return this.pieces; }
    public String getPlayerName(){ return PlayerName; }

    public String getTotalTime() {
        long seconds = totalTime / 1000;
        return seconds + " s";
    }

    public void startTimer() {
        if (timer != null) timer.cancel();

        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                totalTime += 1000;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public void stopTimer() {
        if (timerTask != null) timerTask.cancel();
        if (timer != null) timer.cancel();
    }

}
