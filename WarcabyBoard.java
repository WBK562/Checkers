package Warcaby;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WarcabyBoard extends JFrame {

    private final String game_title = "Warcaby";
    private final int size = 8;
    private final JButton[][] board = new JButton[size][size];
    private final GameLogic logic;
    private final Player player1;
    private final Player player2;
    private JLabel player1Time;
    private JLabel player2Time;
    private JLabel turnLabel;
    private String player1Name = "Gracz 1";
    private String player2Name = "Gracz 2";
    private int amountOfPawns = 12;
    private Timer timer;
    private final Color lightColor = new Color(240, 217, 181);
    private final Color darkColor = new Color(181, 136, 99);
    private final int iconWidth = 40;
    private final int iconHeight = 40;
    private ImageIcon scaledWhiteIcon = scaleIcon(new ImageIcon("res/X.png"), iconWidth, iconHeight);
    private ImageIcon scaledBlackIcon = scaleIcon(new ImageIcon("res/O.png"), iconWidth, iconHeight);
    private ImageIcon scaledWhiteQueenIcon = scaleIcon(new ImageIcon("res/XQ.png"), iconWidth, iconHeight);
    private ImageIcon scaledBlackQueenIcon = scaleIcon(new ImageIcon("res/OQ.png"), iconWidth, iconHeight);


    public WarcabyBoard(){
        setTitle(game_title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panel_gry = new JPanel(new GridLayout(size,size));
        panel_gry.setBorder(new EmptyBorder(50, 50, 50, 50));
        panel_gry.setBackground(Color.WHITE);

        turnLabel = setupLabels("Tura gracza: ");
        turnLabel.setPreferredSize(new Dimension(200, 30));

        player1Time = setupLabels("");
        player2Time = setupLabels("");

        player2 = new Player(scaledWhiteIcon,scaledWhiteQueenIcon,amountOfPawns,false,player2Name);
        player1 = new Player(scaledBlackIcon,scaledBlackQueenIcon,amountOfPawns,true,player1Name);

        JPanel infoPanel = createInfoPanel();

        logic = new GameLogic(board, size, player1, player2, turnLabel,this);
        timer = new Timer(1000, e -> updatePlayerTimes());
        timer.start();

        boardInit(panel_gry);
        add(panel_gry,BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        setSize(1200, 800);
        setResizable(false);
        setVisible(true);
    }

    public void boardInit(JPanel panel_gry){
        for(int i = 0;i < size;i++){
            for(int j = 0;j < size;j++){
                JButton button = new JButton();
                boolean dark = (i + j) % 2 != 0;
                button.setBackground(dark ? lightColor : darkColor);
                if (dark && i < 3) {
                    button.setIcon(player1.getPlayerIcon());
                    button.setForeground(Color.BLACK);
                    button.setFont(new Font("Arial", Font.BOLD, 48));
                } else if (dark && i >= 5) {
                    button.setIcon(player2.getPlayerIcon());
                    button.setForeground(Color.WHITE);
                    button.setFont(new Font("Arial", Font.BOLD, 48));
                }
                else{
                    button.setIcon(null);
                }
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                final int row = i;
                final int col = j;
                button.addActionListener(e -> logic.gameLog(row, col));
                board[i][j] = button;
                panel_gry.add(button);
            }
        }
    }

    private void updatePlayerTimes() {
        player1Time.setText("Czas gracza 1: " + player1.getTotalTime());
        player2Time.setText("Czas gracza 2: " + player2.getTotalTime());
    }

    private ImageIcon scaleIcon(ImageIcon originalIcon, int width, int height) {
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public JPanel createInfoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(5, 20, 50, 50));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(turnLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(player1Time);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(player2Time);
        return infoPanel;
    }

    public JLabel setupLabels(String title){
        JLabel Label = new JLabel(title);
        Label.setFont(new Font("Arial", Font.PLAIN, 18));
        return Label;
    }
}

